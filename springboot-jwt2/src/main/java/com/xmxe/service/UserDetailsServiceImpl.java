package com.xmxe.service;

import com.xmxe.entity.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		// User user = userMapper.selectByUserName(s);
		// if (user == null) {
		// 	throw new UsernameNotFoundException(String.format("'%s'.这个用户不存在", s));
		//
		// }
		// List<SimpleGrantedAuthority> collect = user.getRoles().stream().map(Role::getRolename).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		if(s.equals("admin")){
			List<SimpleGrantedAuthority> auth = new ArrayList<>();
			auth.add(new SimpleGrantedAuthority("ADMIN"));
			return new JwtUser(s, passwordEncoder.encode("123"), auth);
		}
		return null;

	}
}