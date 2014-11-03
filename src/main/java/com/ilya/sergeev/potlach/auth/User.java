package com.ilya.sergeev.potlach.auth;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static UserDetails create(String username, String password,
			String... authorities)
	{
		return new User(username, password, authorities);
	}
	
	private final Collection<GrantedAuthority> mAuthorities;
	private final String mPassword;
	private final String mUsername;
	
	@SuppressWarnings("unchecked")
	private User(String username, String password)
	{
		this(username, password, Collections.EMPTY_LIST);
	}
	
	private User(String username, String password, String... authorities)
	{
		mUsername = username;
		mPassword = password;
		mAuthorities = AuthorityUtils.createAuthorityList(authorities);
	}
	
	private User(String username, String password, Collection<GrantedAuthority> authorities)
	{
		super();
		mUsername = username;
		mPassword = password;
		mAuthorities = authorities;
	}
	
	public Collection<GrantedAuthority> getAuthorities()
	{
		return mAuthorities;
	}
	
	public String getPassword()
	{
		return mPassword;
	}
	
	public String getUsername()
	{
		return mUsername;
	}
	
	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}
	
	@Override
	public boolean isEnabled()
	{
		return true;
	}
	
}
