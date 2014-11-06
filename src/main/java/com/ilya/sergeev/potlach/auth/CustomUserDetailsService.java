package com.ilya.sergeev.potlach.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ilya.sergeev.potlach.repository.UserInfo;
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
		if ("admin".equals(username))
		{
			return User.create("admin", "12345", "ADMIN", "USER");
		}
		else if ("user".equals(username))
		{
			return User.create("user", "11111", "USER");
		}
		else
		{
			UserInfo userInfo = mUserRepository.findByName(username);
			return User.create(userInfo.getName(), userInfo.getPassword(), "USER");
		}
	}
}
