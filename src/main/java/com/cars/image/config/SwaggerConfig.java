package com.cars.image.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cars.image.Application;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	// Basic SpringFox swagger2 config config.
	// See http://springfox.github.io/springfox/docs/current/ for more details
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage(Application.class.getPackage().getName())).paths(PathSelectors.any())
				.build().apiInfo(getApiInfo());
	}

	public ApiInfo getApiInfo() {
		return new ApiInfo("image-processing-service REST API", "Used for image processing", "1.0",
				"Terms of service URL (May be a confluence page)",
				new Contact("n", "", "bparthasarathy@cars.com"), "Cars Internal License", "Link to Cars Internal License");
	}
}
