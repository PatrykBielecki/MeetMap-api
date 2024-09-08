package com.meetmap;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "MeetMap API", version = "0.1", description = "MeetMap API"))
@EnableScheduling
public class MeetMapApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeetMapApplication.class, args);
    }
}
