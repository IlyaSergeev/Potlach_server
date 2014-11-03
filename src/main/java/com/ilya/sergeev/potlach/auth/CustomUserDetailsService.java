package com.ilya.sergeev.potlach.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ilya.sergeev.potlach.repository.UserInfo;
import com.ilya.sergeev.potlach.repository.UserInfoRepository;

public class CustomUserDetailsService implements UserDetailsService
{
	@Autowired
	private UserInfoRepository mUserInfoRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		if ("admin".equals(username))
		{
			return User.create("admin", "12345", "ADMIN", "USER");
		}
		else
		{
			UserInfo userInfo = mUserInfoRepository.findByName(username);
			return User.create(userInfo.getName(), userInfo.getPassword(), "USER");
		}
	}
}
