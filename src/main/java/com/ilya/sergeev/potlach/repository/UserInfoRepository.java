package com.ilya.sergeev.potlach.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository  extends CrudRepository<UserInfo, Long>
{
	public UserInfo findByName(String name);
}
