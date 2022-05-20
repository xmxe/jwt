package com.xmxe;

import com.xmxe.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class SecurityJwtAuthcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtAuthcServerApplication.class, args);
    }

}


