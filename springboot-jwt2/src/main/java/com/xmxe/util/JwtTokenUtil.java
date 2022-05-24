package com.xmxe.util;

import com.xmxe.entity.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class JwtTokenUtil implements Serializable {
	// 密钥（盐值）
	private String secret;
	// 过期时间
	private Long expiration;
	// 请求头
	private String header;

	@PostConstruct
	public void initValue(){
		this.expiration = 1000 * 60L;
		this.secret = "xmxe";
		this.header = "Authentication";
	}

	/**
	 * 生成token
	 * @param claims
	 * @return
	 */
	private String generateToken(Map<String, Object> claims) {
		Date expirationDate = new Date(System.currentTimeMillis() + expiration);
		return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * 解析token
	 * @param token
	 * @return
	 */
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	/**
	 * 根据userDetails生成token
	 * @param userDetails
	 * @return
	 */
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>(2);
		claims.put("sub", userDetails.getUsername());
		claims.put("created", new Date());
		return generateToken(claims);
	}

	/**
	 * 根据token获取用户名
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {
		String username;
		try {
			Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();

		} catch (Exception e) {
			username = null;

		}
		return username;
	}

	/**
	 * 验证token是否过期
	 * @param token
	 * @return
	 */
	public Boolean isTokenExpired(String token) {
		try {
			Claims claims = getClaimsFromToken(token);
			Date expiration = claims.getExpiration();
			return expiration.before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 刷新token
	 * @param token
	 * @return
	 */
	public String refreshToken(String token) {
		String refreshedToken;
		try {
			Claims claims = getClaimsFromToken(token);
			claims.put("created", new Date());
			refreshedToken = generateToken(claims);

		} catch (Exception e) {
			refreshedToken = null;

		}
		return refreshedToken;
	}

	/**
	 * 校验token和userdetails是否匹配
	 * @param token
	 * @param userDetails
	 * @return
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		JwtUser user = (JwtUser) userDetails;
		String username = getUsernameFromToken(token);
		return (username.equals(user.getUsername()) && !isTokenExpired(token));
	}

}
