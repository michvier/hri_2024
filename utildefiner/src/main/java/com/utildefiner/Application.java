package com.utildefiner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.helpers.LaunchUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@EntityScan(basePackages = {"net.rw.utilitydef.*", "com.utildefiner.data.entity"})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }
}
