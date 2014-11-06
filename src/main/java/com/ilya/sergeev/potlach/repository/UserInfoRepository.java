package com.ilya.sergeev.potlach.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserInfoRepository  extends CrudRepository<UserInfo, Long>
{
	public UserInfo findByName(String name);
	
	@Query("select u from UserInfo u order by u.rating DESC")
	public Collection<UserInfo> getTopRate();
}
