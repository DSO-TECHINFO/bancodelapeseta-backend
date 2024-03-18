package com.banco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan("com.banco")
@EntityScan("com.banco")
@EnableJpaRepositories("com.banco")
@SpringBootApplication
@EnableScheduling
public class BancodelapesetaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancodelapesetaApplication.class, args);
    }

}
