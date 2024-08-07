### Convert existing Spring application to discoverable in Tanzu Platform
 You should do following changes in your existing spring app to make it discoverable in Tanzu platform.
 
 1. Add the actuator to a Maven-based project, add the following starter dependency in project `pom.xml`.
```
#To add actuator dependency for actuator.
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-actuator</artifactId>
	</dependency>
</dependencies>

# To add the git commit id plugin into your plugins section of your pom.xml file (Spring application 3.x)
<plugin>
    <groupId>io.github.git-commit-id</groupId>
    <artifactId>git-commit-id-maven-plugin</artifactId>
    <configuration>
        <verbose>true</verbose>
    </configuration>
</plugin>

```
2. Add following properties on Project `Application.properties` to enable actuator end points and git info.
```
spring.application.name=<name of your application>
management.security.enabled=false
management.endpoints.web.exposure.include=*
management.endpoint.env.show-values=ALWAYS
management.info.env.enabled=true
management.info.build.enabled=true
management.info.git.enabled=true
management.info.java.enabled=true
management.info.os.enabled=true
management.info.git.mode=full
info.app.version="@project.version@"
info.spring.boot.version="@project.parent.version@"

```
If you are using `application.yaml` in your project, add following properties in `application.yaml`.

```
management:
    endpoints:
        web:
            exposure:
                include: "info"
management:
    endpoints:
        web:
            exposure:
                include: "info,health"
    endpoint:
        health:
            show-details: ALWAYS

management:
    endpoints:
        web:
            exposure:
                include: "info,env"
    endpoint:
        env:
            show-values: ALWAYS 

management:
    info:
        git:
            mode: full
management:
    info:
        env:
            enabled: true
        java:
            enabled: true
info:
    app:
        version: "@project.version@"
    spring:
        boot:
            version: "@project.parent.version@"                                   
```

3. To add SBOM info for your Spring applications in Tanzu platform.
 - Add the CycloneDX plugin into your plugins section of your `pom.xml` file.
```
<plugin>
    <groupId>org.cyclonedx</groupId>
    <artifactId>cyclonedx-maven-plugin</artifactId>
    <version>2.7.1</version>
    <executions>
        <execution>
            <phase>validate</phase>
            <goals>
                <goal>makeAggregateBom</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <outputFormat>json</outputFormat>
        <outputName>classes/bom</outputName>
    </configuration>
</plugin>
```
- Implement the following class required by the CycloneDX plugin.
```
import java.io.InputStream;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import brave.internal.Nullable;

@Component
class CycloneDxInfoContributor implements InfoContributor, InitializingBean {
    private final Resource bomFile;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private @Nullable JsonNode bom;

    CycloneDxInfoContributor(@Value("classpath:bom.json") Resource bomFile) {
        this.bomFile = bomFile;
    }

    @Override
    public void contribute(Info.Builder builder) {
        if (bom != null) {
            builder.withDetail("bom", bom);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (bomFile.exists()) {
            try (InputStream is = bomFile.getInputStream()) {
                this.bom = objectMapper.readTree(is);
            }
        }
    }
}
```
