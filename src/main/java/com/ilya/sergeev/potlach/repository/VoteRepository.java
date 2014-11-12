package com.ilya.sergeev.potlach.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ilya.sergeev.potlach.client.Vote;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long>
{
	public Vote findByUserNameAndGiftId(String userName, long giftId);
}
