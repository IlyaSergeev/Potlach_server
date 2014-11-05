package com.ilya.sergeev.potlach.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long>
{
	public Collection<Vote> findByUserId(long userId);
	public Collection<Vote> findByGiftId(long giftId);
	public Vote findByUserIdAndGiftId(long userId, long giftId);
}
