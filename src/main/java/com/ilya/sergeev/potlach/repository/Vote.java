package com.ilya.sergeev.potlach.repository;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;
import com.sun.istack.internal.NotNull;

@Entity
public class Vote implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private long userId;
	
	@NotNull
	private long giftId;
	
	private int vote;
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public int getVote()
	{
		return vote;
	}
	
	public void setVote(int vote)
	{
		this.vote = vote;
	}
	
	public long getUserId()
	{
		return userId;
	}
	
	public void setUserId(long userId)
	{
		this.userId = userId;
	}
	
	public long getGiftId()
	{
		return giftId;
	}
	
	public void setGiftId(long giftId)
	{
		this.giftId = giftId;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getUserId(), getGiftId(), getVote());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof Vote)
				&& getGiftId() == ((Vote) obj).getGiftId()
				&& getUserId() == ((Vote) obj).getUserId()
				&& getVote() == ((Vote) obj).getVote();
	}
}
