package com.xmxe.config;

import com.xmxe.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	/**
	 * 用户真正的信息
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * 校验header是否携带token
	 */
	@Autowired
	private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

	/**
	 * 校验时的加密算法
	 * @param authenticationManagerBuilder
	 * @throws Exception
	 */
	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());

	}
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().
				// ALWAYS 总是创建HttpSession. IF_REQUIRED Spring Security只会在需要时创建一个HttpSession
				// NEVER Spring Security不会创建HttpSession，但如果它已经存在，将可以使用HttpSession
				// STATELESS Spring Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
				sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers("/", "/login**").permitAll()
				.anyRequest().authenticated()
				// .and().formLogin().loginPage("/") // 自定义登录页
				.and().cors()
				.and().headers().cacheControl();
		// 校验header是否携带token并解析
		http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
		// 接收json格式的前端请求
		http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		// ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
		// registry.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

	}

	/**
	 * /login请求交由CustomAuthenticationFilter处理
	 * @return
	 * @throws Exception
	 */
	@Bean
	CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
		CustomAuthenticationFilter filter = new CustomAuthenticationFilter(jwtTokenUtil);
		// filter.setAuthenticationSuccessHandler(new SuccessHandler());
		// filter.setAuthenticationFailureHandler(new FailureHandler());
		filter.setFilterProcessesUrl("/login");
		filter.setAuthenticationManager(authenticationManagerBean());
		return filter;
	}

	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration cors = new CorsConfiguration();
		cors.setAllowCredentials(true);
		cors.addAllowedOrigin("*");
		cors.addAllowedHeader("*");
		cors.addAllowedMethod("*");
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", cors);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
