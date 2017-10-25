package com.cars.image.exception.controller;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import springfox.documentation.annotations.ApiIgnore;


@Controller
@RequestMapping("/error")
@ApiIgnore
public class ExceptionController implements ErrorController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);
	
    @Autowired
    private ErrorAttributes errorAttributes;

	
	@RequestMapping(produces = "text/html")
	public void error(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> model = getErrorAttributes(request,
				true);
		response.setStatus((Integer)model.get("status"));
		try {
			for (Entry<String, Object> e : model.entrySet()){
				response.getWriter().write("key: " + e.getKey() + " value: " + e.getValue() + "\n");
			}
		} catch (Exception e){
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		
	}

	@RequestMapping(produces = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request,
				true);
		Integer status = (Integer)body.get("status");
		return new ResponseEntity<Map<String, Object>>(body, HttpStatus.valueOf(status));
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

}
