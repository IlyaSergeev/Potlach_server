package com.ilya.sergeev.potlach.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;
import com.sun.istack.internal.NotNull;

@Entity
public class Gift 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@NotNull
	private String userName;
	
	@NotNull
	private String title;
	private String message;
	private String url;
	
	@NotNull
	private Date createDate = new Date();
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
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
		return Objects.hashCode(getUserName(), getTitle(), getMessage(), getUrl());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof Gift)
				&& Objects.equal(getUserName(), ((Gift)obj).getUserName())
				&& Objects.equal(getTitle(), ((Gift)obj).getTitle())
				&& Objects.equal(getMessage(), ((Gift)obj).getMessage())
				&& Objects.equal(getUrl(), ((Gift)obj).getUrl());
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
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
