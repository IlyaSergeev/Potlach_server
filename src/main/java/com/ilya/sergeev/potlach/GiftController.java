package com.ilya.sergeev.potlach;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import retrofit.http.Multipart;
import retrofit.http.Query;
import retrofit.http.Streaming;

import com.google.common.collect.Lists;
import com.ilya.sergeev.potlach.ImageStatus.ImageState;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.repository.Gift;
import com.ilya.sergeev.potlach.repository.GiftRepository;
import com.ilya.sergeev.potlach.repository.UserInfoRepository;

//TODO make limits in futures

@Controller
public class GiftController
{
	@Autowired
	UserInfoRepository mUserRepository;
	
	@Autowired
	GiftRepository mGiftRepository;
	
	@PreAuthorize("hasRole('USER'")
	@RequestMapping(value = GiftSvcApi.NEW_GIFT_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<Gift> getNewGifts()
	{
		return Lists.newArrayList(mGiftRepository.findAllOrderByDateAsc());
	}
	
	@PreAuthorize("hasRole('USER'")
	@RequestMapping(value = GiftSvcApi.MY_GIFT_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<Gift> getMyGifts(Principal principal)
	{
		return Lists.newArrayList(mGiftRepository.findByUserNameOrderByDateAsc(principal.getName()));
	}
	
	@PreAuthorize("hasRole('USER'")
	@RequestMapping(value = GiftSvcApi.CREATE_GIFT_PATH, method = RequestMethod.POST)
	public @ResponseBody
	Gift createGift(@Query(GiftSvcApi.TITLE_PARAM) String title, @Query(GiftSvcApi.MESSAGE_PARAM) String message, Principal principal)
	{
		Gift gift = new Gift();
		gift.setTitle(title);
		gift.setMessage(message);
		gift.setUserName(principal.getName());
		gift = mGiftRepository.save(gift);
		if (gift.getId() > 0)
		{
			gift.setUrl(getPhotGiftoUrl(gift.getId()));
			return mGiftRepository.save(gift);
		}
		return null;
	}
	
	private static String getPhotGiftoUrl(long giftId)
	{
		return String.format("%s/%d/data", GiftSvcApi.GIFT_PATH, giftId);
	}

	@Multipart
	@PreAuthorize("hasRole('USER'")
	@RequestMapping(value=GiftSvcApi.GIFT_DATA_PATH, method=RequestMethod.POST)
	public ImageStatus setImageData(@PathVariable(GiftSvcApi.ID_PARAM) long giftId, @RequestParam(GiftSvcApi.DATA_PARAMETER) MultipartFile giftData)
	{
		Gift gift = mGiftRepository.findOne(giftId);
		if (gift == null)
		{
			try
			{
				GiftsFileManager.get().saveGiftData(gift, giftData.getInputStream());
				return new ImageStatus(ImageState.READY);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Streaming
	@PreAuthorize("hasRole('USER'")
	@RequestMapping(value=GiftSvcApi.GIFT_DATA_PATH, method = RequestMethod.GET)
	public void getData(@PathVariable(GiftSvcApi.ID_PARAM) long giftId, HttpServletResponse response)
	{
		Gift gift = mGiftRepository.findOne(giftId);
		if (gift == null)
		{
			throw new ResourceNotFoundException();
		}
		try
		{
			GiftsFileManager giftFileManager = GiftsFileManager.get();
			if (giftFileManager.hasGiftData(gift))
			{
				giftFileManager.copyGiftData(gift, response.getOutputStream());
				response.setHeader("MIME", "image/jpeg");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new ResourceNotFoundException();
		}
	}
}
