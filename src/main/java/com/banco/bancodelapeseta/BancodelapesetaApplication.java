package com.banco.bancodelapeseta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("com.banco")
@EntityScan("com.banco")
@EnableJpaRepositories("com.banco")
@SpringBootApplication
public class BancodelapesetaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancodelapesetaApplication.class, args);
    }

}
