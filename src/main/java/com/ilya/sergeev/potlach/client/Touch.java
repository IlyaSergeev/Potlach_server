package com.ilya.sergeev.potlach.client;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;

@Entity
public class Touch
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String userName;
	private long giftId;
	
	public Touch()
	{
		
	}
	
	public Touch(String userName, long giftId)
	{
		this.userName = userName;
		this.giftId = giftId;
	}
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public long getGiftId()
	{
		return giftId;
	}
	
	public void setGiftId(long giftId)
	{
		this.giftId = giftId;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getGiftId(), getUserName());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof Touch) &&
				getGiftId() == ((Touch) obj).getGiftId() &&
				Objects.equal(getUserName(), ((Touch) obj).getUserName());
	}
}
