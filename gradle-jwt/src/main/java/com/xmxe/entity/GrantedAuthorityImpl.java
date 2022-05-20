package com.xmxe.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * 权限 角色 实体类
 */
public class GrantedAuthorityImpl implements GrantedAuthority {
	private String authority;

	public GrantedAuthorityImpl(String authority) {
		this.authority = authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return this.authority;
	}
}
