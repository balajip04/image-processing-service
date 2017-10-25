package com.cars.image.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cars.framework.config.ConfigService;

/**
 * Controller to refresh config from the Config Service API.
 */
@RestController
public class ConfigController {

	@Autowired
	private ConfigService configService;

	@RequestMapping(value = "/config/refresh", method = RequestMethod.GET)
	public void refresh() {
		this.configService.refresh();
	}

	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public ResponseEntity<String> config() {
		String config = this.configService.asJson();
		if (StringUtils.isEmpty(config)) {
			return new ResponseEntity<String>("This application has no config setup", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<String>(config, HttpStatus.OK);
		}

	}

}
