package com.sc.auth.entity;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;


public class OAuthUser extends org.springframework.security.core.userdetails.User {
	private User user;

	public OAuthUser(User user) {
		super(user.getUsername(), user.getPassword(), true, true, true, true, Collections.EMPTY_SET);
		this.user = user;
	}
	
	public OAuthUser(String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
