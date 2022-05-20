package com.xmxe;

import com.xmxe.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SecurityJwtServiceResourcesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtServiceResourcesApplication.class, args);
    }
}
