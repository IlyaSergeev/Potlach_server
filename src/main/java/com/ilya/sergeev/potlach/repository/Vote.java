package com.ilya.sergeev.potlach.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;
import com.sun.istack.internal.NotNull;

@Entity
public class Vote
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private String userName;
	
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
	
	public String getUserName()
	{
		return userName;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
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
		return Objects.hashCode(getUserName(), getGiftId(), getVote());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof Vote)
				&& getGiftId() == ((Vote) obj).getGiftId()
				&& getUserName() == ((Vote) obj).getUserName()
				&& getVote() == ((Vote) obj).getVote();
	}
}
