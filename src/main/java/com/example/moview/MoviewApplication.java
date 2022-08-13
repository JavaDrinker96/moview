package com.example.moview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MoviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviewApplication.class, args);
    }
}