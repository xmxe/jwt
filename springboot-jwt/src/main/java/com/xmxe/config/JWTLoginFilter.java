package com.xmxe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmxe.entity.AccountCredentials;
import com.xmxe.entity.JSONResult;
import com.xmxe.service.TokenAuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 下面实现JWTLoginFilter 这个Filter比较简单，除了构造函数需要重写三个方法。
 * attemptAuthentication - 登录时需要验证时候调用
 * successfulAuthentication - 验证成功后调用
 * unsuccessfulAuthentication - 验证失败后调用，这里直接灌入500错误返回，由于同一JSON返回，HTTP就都返回200了
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	public JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(
			HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {

		// JSON反序列化成 AccountCredentials
		AccountCredentials creds = new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class);

		// 返回一个验证令牌
		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
						creds.getUsername(),
						creds.getPassword()
				)
		);
	}

	@Override
	protected void successfulAuthentication(
			HttpServletRequest req,
			HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		TokenAuthenticationService.addAuthentication(res, auth.getName());
	}


	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getOutputStream().println(JSONResult.fillResultString(500, "Internal Server Error!!!", null));
	}
}