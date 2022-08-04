package com.example.moview.moview.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = "com.example.moview.moview")
@EnableWebMvc
public class SpringConfig {
}
