package com.xmxe.config;

import com.xmxe.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 校验header是否携带token并解析
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		// 获取请求头
		String authHeader = httpServletRequest.getHeader(jwtTokenUtil.getHeader());
		if (authHeader != null && !StringUtils.isEmpty(authHeader)) {
			// 根据header解析用户名
			String username = jwtTokenUtil.getUsernameFromToken(authHeader);
			// 如果未登录去做token解析，登陆的话就不需要做token解析了
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// 真正正确的用户信息
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				// 校验
				if (jwtTokenUtil.validateToken(authHeader, userDetails)) {
					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
					// 将用户放入上下文
					SecurityContextHolder.getContext().setAuthentication(authentication);

				}
			}
		}
		// 继续走下一个过滤链
		filterChain.doFilter(httpServletRequest, httpServletResponse);

	}
}