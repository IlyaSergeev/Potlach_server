package com.ilya.sergeev.potlach.client;

import java.util.Collection;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import com.ilya.sergeev.potlach.repository.Gift;

public interface GiftSvcApi
{
	public static final String GIFT_PATH = "/gifts";
	
	public static final String NEW_GIFT_PATH = GIFT_PATH + "/new";
	public static final String MY_GIFT_PATH = GIFT_PATH + "/my";
	public static final String CREATE_GIFT_PATH = GIFT_PATH + "/create";
	
	public static final String TITLE_PARAM = "title";
	public static final String MESSAGE_PARAM = "msg";
	
	@GET(NEW_GIFT_PATH)
	public Collection<Gift> getNewGifts();
	
	@GET(MY_GIFT_PATH)
	public Collection<Gift> getMyGifts();
	
	@POST(CREATE_GIFT_PATH)
	public Gift createGift(@Query(TITLE_PARAM)String title, @Query(MESSAGE_PARAM)String message);
}
