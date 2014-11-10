package com.ilya.sergeev.potlach.client;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;

@Entity
public class UserInfo
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(unique=true, nullable=false)
	private String name;
	private String password;
	
	private int rating = 0;
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getName());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof UserInfo) && Objects.equal(getName(), ((UserInfo)obj).getName());
	}

	public int getRating()
	{
		return rating;
	}

	public void setRating(int rating)
	{
		this.rating = rating;
	}
}
