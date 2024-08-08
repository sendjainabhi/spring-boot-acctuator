package com.pve.springacctuator;

import java.io.InputStream;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.common.lang.Nullable;



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