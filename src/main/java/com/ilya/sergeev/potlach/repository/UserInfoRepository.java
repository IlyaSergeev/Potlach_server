package com.ilya.sergeev.potlach.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ilya.sergeev.potlach.client.UserInfo;


@Repository
public interface UserInfoRepository  extends CrudRepository<UserInfo, Long>
{
	public UserInfo findByName(String name);
	
	//return top rate users
	@Query("select u from UserInfo u order by u.rating DESC")
	public Collection<UserInfo> getTopRate();
}
