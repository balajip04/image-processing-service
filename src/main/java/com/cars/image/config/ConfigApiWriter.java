package com.cars.image.config;

import java.io.IOException;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cars.framework.config.ConfigJsonWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigApiWriter implements ConfigJsonWriter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public String write(Object object) {
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, object);
		} catch (IOException e) {
			logger.error("Unable to write object [" + object.toString() + "] using mapper [" + mapper.toString() + "]",
					e);
		}
		return writer.toString();
	}

}
