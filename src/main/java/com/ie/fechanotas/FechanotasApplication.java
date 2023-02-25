package com.ie.fechanotas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan
public class FechanotasApplication {

	public static void main(String[] args) {
		SpringApplication.run(FechanotasApplication.class, args);
	}

}
