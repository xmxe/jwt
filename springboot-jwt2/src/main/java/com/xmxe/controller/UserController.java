package com.xmxe.controller;

import com.xmxe.service.UserDetailsServiceImpl;
import com.xmxe.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

	private final JwtTokenUtil jwtTokenUtil;

	private final UserDetailsServiceImpl userDetailsService;

	@Autowired
	public UserController(JwtTokenUtil jwtTokenUtil, UserDetailsServiceImpl userDetailsService) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.userDetailsService = userDetailsService;
	}

	@RequestMapping("/")
	public String index(HttpServletRequest request){
		// 需要启用session才可以通过SecurityContextHolder.getContext()获取spring security上下文
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		String name = authentication.getName();
		Object userdetail = authentication.getPrincipal();
		Object name2 = authentication.getCredentials();

		SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");

		UserDetails userDetails = userDetailsService.loadUserByUsername(name);
		String token = jwtTokenUtil.generateToken(userDetails);
		return token;
	}

	@GetMapping("t1")
	public String t1(){
		return "t1";
	}
}
