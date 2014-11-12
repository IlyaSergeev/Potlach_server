package com.ilya.sergeev.potlach.client;

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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private String owner;
	
	@NotNull
	private String title;
	private String message = null;
	private String contentType = "image/jpg";
	
	@NotNull
	private long date = System.currentTimeMillis();
	
	public Gift()
	{
		
	}
	
	public Gift(String title, String message)
	{
		this.title = title;
		this.message = message;
	}
	
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
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getOwner(), getTitle(), getMessage());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof Gift)
				&& Objects.equal(getOwner(), ((Gift) obj).getOwner())
				&& Objects.equal(getTitle(), ((Gift) obj).getTitle())
				&& Objects.equal(getMessage(), ((Gift) obj).getMessage());
	}
	
	public long getDate()
	{
		return date;
	}
	
	public void setDate(long date)
	{
		this.date = date;
	}
	
	public String getOwner()
	{
		return owner;
	}
	
	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}
}
