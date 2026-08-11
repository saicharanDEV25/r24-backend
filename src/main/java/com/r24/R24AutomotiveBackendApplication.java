package com.r24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
public class R24AutomotiveBackendApplication {

    public static void main(String[] args) {
        // Render runs the server in UTC by default, but LocalDateTime.now() (used for
        // every createdAt/visitedAt timestamp) has no timezone info attached when saved.
        // The frontend parses it as local (IST) time, so without this it's off by 5:30.
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        SpringApplication.run(R24AutomotiveBackendApplication.class, args);
    }

}