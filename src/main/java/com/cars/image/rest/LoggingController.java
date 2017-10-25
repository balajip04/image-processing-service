package com.cars.image.rest;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.swagger.annotations.ApiParam;

@RestController
public class LoggingController {

	@RequestMapping(method = RequestMethod.PUT, value = "/logging/set/{name}/{level}")
	public ResponseEntity<String> setLevel(@PathVariable("name") String name,
			@ApiParam(allowableValues = "INFO,DEBUG,TRACE,ERROR,ALL,WARN,OFF", defaultValue = "ERROR") @PathVariable("level") String level) {
		// SLF4J does not provide an api to change log level programmatically.
		// So this is relying on the log back api to change log level. If the
		// project is change to use some other logging implementation, this
		// method won't work as expected.
		LoggerContext loggerFactory = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = loggerFactory.exists(name);
		if (logger != null) {
			Level lvl = Level.toLevel(level.toUpperCase(), Level.ERROR);
			logger.setLevel(lvl);
			return new ResponseEntity<String>("Log level changed to [" + lvl + "] for logger " + name, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("[" + name + "] is not a known logger to this application.",
					HttpStatus.BAD_REQUEST);
		}
	}

}
