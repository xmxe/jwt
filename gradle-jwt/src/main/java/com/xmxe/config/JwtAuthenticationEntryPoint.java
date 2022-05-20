package com.xmxe.config;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 当用户请求了一个受保护的资源，但是用户没有通过认证，那么抛出异常，AuthenticationEntryPoint. Commence(..)就会被调用
 * AuthenticationEntryPoint是Spring Security Web一个概念模型接口，顾名思义，他所建模的概念是:“认证入口点”。
 * 它在用户请求处理过程中遇到认证异常时，被ExceptionTranslationFilter用于开启特定认证方案(authentication schema)的认证流程。
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		logger.info("认证失败---->{}",authException.getMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}
