package ru.sonder.carsharing.configs;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.sonder.carsharing.repositories")
@EntityScan(basePackages = "ru.sonder.carsharing.models")
@ComponentScan(basePackages = "ru.sonder.carsharing")
public class AppConfig {}
