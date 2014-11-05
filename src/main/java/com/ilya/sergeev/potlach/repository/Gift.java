package com.ilya.sergeev.potlach.repository;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;

public class Gift
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private UserInfo owner;
	private String title;
	private String message;
	private String url;
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}

	public UserInfo getOwner()
	{
		return owner;
	}

	public void setOwner(UserInfo owner)
	{
		this.owner = owner;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getOwner(), getTitle(), getMessage(), getUrl());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof Gift)
				&& Objects.equal(getOwner(), ((Gift)obj).getOwner())
				&& Objects.equal(getTitle(), ((Gift)obj).getTitle())
				&& Objects.equal(getMessage(), ((Gift)obj).getMessage())
				&& Objects.equal(getUrl(), ((Gift)obj).getUrl());
	}
}
