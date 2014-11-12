package com.ilya.sergeev.potlach.client;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Touch
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id; 
	
	private String userName;
	private long giftId;
	
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
}
