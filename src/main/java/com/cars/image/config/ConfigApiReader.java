package com.cars.image.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import com.cars.framework.config.impl.AbstractResourceConfigReader;

public class ConfigApiReader extends AbstractResourceConfigReader {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private RestTemplate restTemplate;

	public ConfigApiReader(String resourceUrl, RestTemplate restTemplate) {
		setResourceUrl(resourceUrl);
		this.restTemplate = restTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> doRead(String resourceUrl) {
		try {
			return this.restTemplate
					.exchange(RequestEntity.get(new URI(resourceUrl)).accept(MediaType.APPLICATION_JSON).build(),
							Map.class)
					.getBody();
		} catch (URISyntaxException e) {
			logger.error("Unable to read config data from [" + resourceUrl + "]", e);
		}
		return null;
	}

}
