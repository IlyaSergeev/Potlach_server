package com.ilya.sergeev.potlach.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftRepository extends CrudRepository<Gift, Long>
{
	public Collection<Gift> findAllOrderByDateAsc();
	public Collection<Gift> findByTitleContainingIgnoreCaseOrderByDateAsc(String title);
	public Collection<Gift> findByUserNameOrderByDateAsc(String userName);
}
