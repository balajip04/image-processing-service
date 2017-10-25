package com.cars.image.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.cars.framework.config.ConfigService;
import com.cars.framework.config.feature.FeatureFlag;
import com.cars.framework.config.feature.impl.FeatureFlagImpl;
import com.cars.framework.config.impl.ConfigServiceImpl;

@Configuration
public class ConfigApiConfig {

	@Bean
	public ConfigService configService(@Value("${config-api.url}") String configApiUrl) {
		ConfigServiceImpl service = new ConfigServiceImpl();
		service.setReader(new ConfigApiReader(configApiUrl, restTemplate()));
		service.setWriter(new ConfigApiWriter());
		return service;
	}

	@Bean
	public FeatureFlag featureFlag(ConfigService configService) {
		return new FeatureFlagImpl(configService);
	}

	// Configure RestTemplate as per your project needs
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
