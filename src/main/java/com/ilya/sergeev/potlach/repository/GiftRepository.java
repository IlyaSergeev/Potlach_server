package com.ilya.sergeev.potlach.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ilya.sergeev.potlach.client.Gift;

@Repository
public interface GiftRepository extends CrudRepository<Gift, Long>
{
	//search by key word in title
	public Collection<Gift> findByTitleContainingIgnoreCase(String title);
	public Collection<Gift> findByOwner(String owner);
}
