package com.aarestu.controller;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/example")// storage is the name of controller
public class SimpleexampleWebService {

	//private String status_successfull = "{\"status\":\"success\"}";
	private String status_failure = "{\"status\":\"fail\"}";
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)// "get" is the name of service.
	public String index(ModelMap model) {

		model.addAttribute("message", "Hello Spring Web MVC!");

		return "hello";
	}

	

	@RequestMapping(value = "/put", method = RequestMethod.GET)
	public @ResponseBody
	String registerProvider(@RequestParam(value = "dc") String dc, @RequestParam(value = "size") Integer size) {
		try {

			  Random rnd = new Random(size);
			  return Integer.toString(rnd.nextInt());//Integer.toString(rnd.nextInt());
						
		 } catch (Exception e) {
			
			e.printStackTrace();
			return status_failure;
		}
	}
	
}