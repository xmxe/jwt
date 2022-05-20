package com.xmxe.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.xmxe.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * javax.servlet.Filter原生接口，Spring的OncePerRequestFilter类实际上是一个实现了Filter接口的抽象类。
 * spring对Filter进行了一些封装处理。 OncePerRequestFilter是在一次外部请求中只过滤一次。对于服务器内部之间的forward等请求，
 * 不会再次执行过滤方法。实现Filter接口，也会在一次请求中只过滤一次, 实际上，OncePerRequestFilter是为了兼容不同的web 容器，
 * 也就是说其实不是所有的容器都过滤一次。Servlet版本不同，执行的过程也不同。例如：在Servlet2.3中，Filter会过滤一切请求，
 * 包括服务器内部使用forward和<%@ include file=/login.jsp%>的情况，但是在servlet2.4中，Filter默认只会过滤外部请求
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		/*
		 * postman中设置请求头Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE2MjU2NjMyOTAsImlhdCI6MTYyNTY0NTI5MH0.Kd66eaEAFCWi6Mu4IK1etDmSYx_09QTEcR5IAid07XRgyMQMGb04z7xDtH86kdNMUQSpsce-F_SVZ4W7gyA5BQ
		 */
		// 获取请求头
		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;
		// 根据token获取用户名 删除token中的Bearer 来获取真正的token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token--无法获取token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired -- token过期");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String --- token不是以Bearer开头");
		}

		//得到token后校验
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// 获取用户名密码等信息
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			// 如果令牌有效，请配置 Spring Security 以手动设置身份验证
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// 在上下文中设置 Authentication 后，我们指定当前用户已通过身份验证。所以它成功地通过了 Spring Security 配置。
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

}
