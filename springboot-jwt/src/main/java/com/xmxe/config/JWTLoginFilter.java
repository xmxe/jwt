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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWTLoginFilter 这个Filter比较简单，除了构造函数需要重写三个方法。
 * attemptAuthentication - 登录时需要验证时候调用
 * successfulAuthentication - 验证成功后调用
 * unsuccessfulAuthentication - 验证失败后调用，这里直接灌入500错误返回，由于同一JSON返回，HTTP就都返回200了
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	public JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	/**
	 * 登陆验证时调用
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException{
		// JSON反序列化成AccountCredentials
		AccountCredentials creds = new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class);
		// authenticate()⽅法中，根据UserDetailsService.loadUserByUsername()的真实用户信息进⾏了密码校验，校验成功就构造⼀个认证过的UsernamePasswordAuthenticationToken对象放⼊SecurityContext
		// 此方法返回用户校验成功后即为successfulAuthentication()方法的Authentication参数
		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword())
		);
	}

	/**
	 * 验证成功后调用
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth){
		// 用户校验成功后根据用户名生成token 通过res(HttpServletResponse)返回token
		TokenAuthenticationService.addAuthentication(res, auth.getName());
	}


	/**
	 * 校验失败后调用
	 */
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getOutputStream().println(JSONResult.fillResultString(500, "Internal Server Error!!!", null));
	}
}