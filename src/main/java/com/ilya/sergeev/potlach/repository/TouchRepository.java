package com.ilya.sergeev.potlach.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ilya.sergeev.potlach.client.Touch;

@Repository
public interface TouchRepository extends CrudRepository<Touch, Long>
{
	public Touch findOneByUserNameAndGiftId(String userName, long giftId);
	
	public Collection<Touch> findByGiftId(long giftId);
	
	public Collection<Touch> findByUserName(String userName);
}
