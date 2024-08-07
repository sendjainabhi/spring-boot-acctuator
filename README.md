
## Deployment guide of Spring Boot Application Deployment on TKGs and Discovery of existing spring app on Tanzu Platform-K8(TPK8)

This guide walks you through the process of deploying a Spring Boot application on TKGs(k8) cluster and then discovery of your spring apps on Tanzu Platform-K8(TPK8).

### Pre-requisites/Environment configuration used in this guide 
  - Tanzu Platform SM Installed in your lab.
  - TKGs workload cluster with load balancer capabilities installed.
  - Image Registry (like Harbor etc) installed.
  - Should have internet access or app dependencies repo(Jfrog etc) configured for airgap environment.

## Deployment Steps 

- Apply changes in your app source code to make it discoverable in tanzu platform. Please refer [spring app changes here](Springapp-on-TP.md).
 

Spring actuator app demo - 

Url - http://localhost:8080/example

Run spring app. - ./mvnw spring-boot:run

Actuator Endpoints -

http://localhost:8080/actuator/env
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info

