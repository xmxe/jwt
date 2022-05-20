package com.xmxe;

import com.xmxe.config.RsaKeyProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@MapperScan("com.oxygen.demo.mapper")
@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class SecurityJwtAuthcServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(SecurityJwtAuthcServerApplication.class, args);
    }

}


