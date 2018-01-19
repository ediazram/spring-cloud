package com.rabbitMQ.receptor.receptorRabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ReceptorRabbitApplication {
			
	public static void main(String[] args) {		
		SpringApplication.run(ReceptorRabbitApplication.class, args);		
	}		
	
}
