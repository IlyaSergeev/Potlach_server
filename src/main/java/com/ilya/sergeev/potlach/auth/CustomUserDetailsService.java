package com.ilya.sergeev.potlach.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ilya.sergeev.potlach.client.UserInfo;
import com.ilya.sergeev.potlach.repository.UserInfoRepository;

public class CustomUserDetailsService implements UserDetailsService
{
	private final UserInfoRepository mUserRepository;
	
	public CustomUserDetailsService(UserInfoRepository userRepository)
	{
		mUserRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		switch (username)
		{
			case "admin":
				return User.create("admin", "12345", "ADMIN");
			
			case "not_user":
				return User.create("not_user", "not_user", "NOT_USER");
				
			case "user":
				return User.create("user", "11111", "USER");
				
			default:
				UserInfo userInfo = mUserRepository.findByName(username);
				return User.create(userInfo.getName(), userInfo.getPassword(), "USER");
		}
	}
}
