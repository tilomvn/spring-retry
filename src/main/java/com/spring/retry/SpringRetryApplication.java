package com.spring.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SpringRetryApplication {

	Logger log = LoggerFactory.getLogger(getClass());
	
	public static void main(String[] args) {
		SpringApplication.run(SpringRetryApplication.class, args);
	}

	
	@Bean
	ApplicationRunner run(IService service) {
		return args -> {
			service.retryService("num");
		};
	}
	
	@Bean
	ApplicationRunner run1(IService service) {
		return args -> {
			log.info(service.retryTemplateService("num"));
			log.info(service.retryTemplateService("NUM"));
		};
	}
}
