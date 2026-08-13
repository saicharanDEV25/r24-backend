package com.r24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
public class R24AutomotiveBackendApplication {

    public static void main(String[] args) {
        // Render runs in UTC, but LocalDateTime.now() has no timezone attached when saved,
        // and the frontend parses it as IST — without this timestamps are off by 5:30.
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        SpringApplication.run(R24AutomotiveBackendApplication.class, args);
    }

}