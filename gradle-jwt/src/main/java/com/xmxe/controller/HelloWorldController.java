package com.xmxe.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
public class HelloWorldController {

//	@PreAuthorize("hasRole('admin') or hasRole('ROLE_ADMIN')")
	@RequestMapping({ "/hello" })
	public String hello() {
		return "Hello World";
	}

}
