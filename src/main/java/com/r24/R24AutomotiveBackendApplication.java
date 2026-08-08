package com.r24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class R24AutomotiveBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(R24AutomotiveBackendApplication.class, args);
    }

}