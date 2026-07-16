package com.as.crichub.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.as.crichub.entity.User;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final User user;

	public CustomUserDetails(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Long getUserId() {
		return user.getUserId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (user.getRole() == null || user.getRole().getRoleName() == null) {
			return List.of();
		}
		return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
