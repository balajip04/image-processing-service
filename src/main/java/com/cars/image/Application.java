package com.cars.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.cars.image.config.*;

@SpringBootApplication
@Import(value = { SwaggerConfig.class, ConfigApiConfig.class })
public class Application {

	// TODO Add your application beans here or use @Import as above
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}