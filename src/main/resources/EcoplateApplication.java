package com.ecoplate.canteen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class EcoplateApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EcoplateApplication.class, args);
    }
}
