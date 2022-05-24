package com.xmxe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmxe.entity.JwtUser;
import com.xmxe.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 接收json格式的前端请求
 * 只处理/login请求
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private JwtTokenUtil jwtTokenUtil;

	public CustomAuthenticationFilter(JwtTokenUtil jwtTokenUtil){
		this.jwtTokenUtil = jwtTokenUtil;
	}
	/**
	 * 登录验证时调用
	 * @param request
	 * @param response
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String contentType = request.getContentType();
		if(MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType) || MediaType.APPLICATION_JSON_VALUE.equals(contentType)){

			ObjectMapper mapper = new ObjectMapper();
			UsernamePasswordAuthenticationToken authRequest = null;
			try (InputStream is = request.getInputStream()){
				JwtUser authenticationBean = mapper.readValue(is,JwtUser.class);
				authRequest = new UsernamePasswordAuthenticationToken(authenticationBean.getUsername(), authenticationBean.getPassword());
			}catch (IOException e) {
				e.printStackTrace();
				authRequest = new UsernamePasswordAuthenticationToken(
						"", "");
			}finally {
				setDetails(request, authRequest);
				// 调用此方法开始一系列校验，最后调⽤了 UserDetailsService.loadUserByUsername()并进⾏了密码校验
				return this.getAuthenticationManager().authenticate(authRequest);
			}
		}else {
			return super.attemptAuthentication(request, response);
		}
	}

	/**
	 * 验证成功后调用
	 * @param req
	 * @param res
	 * @param chain
	 * @param auth
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
	                                        Authentication auth) throws IOException, ServletException {
		// 默认跳转到"/" ,并记录为登陆状态，所以要使用token去做是否登录校验的话不能使用下面的这个父类的方法，而是直接返回token
		// super.successfulAuthentication(req,res,chain,auth);

		String token = jwtTokenUtil.generateToken((UserDetails) auth.getPrincipal());
		res.getWriter().write(token);

	}


	/**
	 * 校验失败后调用
	 * @param request
	 * @param response
	 * @param failed
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		log.error("---失败--{}",failed.getMessage());
		super.unsuccessfulAuthentication(request,response,failed);
	}
}
