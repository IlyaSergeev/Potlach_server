package com.ilya.sergeev.potlach;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import retrofit.http.Multipart;
import retrofit.http.Streaming;

import com.google.common.collect.Lists;
import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.GiftInfo;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.GiftsFileManager;
import com.ilya.sergeev.potlach.client.ImageStatus;
import com.ilya.sergeev.potlach.client.ImageStatus.ImageState;
import com.ilya.sergeev.potlach.repository.GiftRepository;
import com.ilya.sergeev.potlach.repository.UserInfoRepository;
import com.ilya.sergeev.potlach.repository.VoteRepository;

@Controller
public class GiftController
{
	@Autowired
	UserInfoRepository mUserRepository;
	
	@Autowired
	VoteRepository mVoteRepository;
	
	@Autowired
	GiftRepository mGiftRepository;
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = GiftSvcApi.ALL_GIFT_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<GiftInfo> getAllGifts(Principal principal)
	{
		return reverse(mGiftRepository.findAll(), principal.getName());
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = GiftSvcApi.MY_GIFT_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<GiftInfo> getMyGifts(Principal principal)
	{
		String userName = principal.getName();
		return reverse(mGiftRepository.findByOwner(userName), userName);
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = GiftSvcApi.GIFT_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<GiftInfo> getGiftsByUserName(@RequestParam(GiftSvcApi.USER_PARAM) String userName, Principal principal)
	{
		return reverse(mGiftRepository.findByOwner(userName), principal.getName());
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = GiftSvcApi.SEARCH_GIFT_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<GiftInfo> searchGifts(@RequestParam(GiftSvcApi.TAG_PARAM) String tag, Principal principal)
	{
		return reverse(mGiftRepository.findByTitleContainingIgnoreCase(tag), principal.getName());
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = GiftSvcApi.SINGLE_GIFT_PATH, method = RequestMethod.GET)
	public @ResponseBody
	GiftInfo getGift(@PathVariable(GiftSvcApi.ID_PARAM) long giftId, Principal principal)
	{
		return new GiftInfo(mGiftRepository.findOne(giftId), mVoteRepository.findByUserNameAndGiftId(principal.getName(), giftId));
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = GiftSvcApi.CREATE_GIFT_PATH, method = RequestMethod.POST)
	public @ResponseBody
	Gift createGift(@RequestBody Gift gift, Principal principal)
	{
		if (gift.getId() > 0)
		{
			throw new IllegalArgumentException();
		}
		gift.setOwner(principal.getName());
		gift = mGiftRepository.save(gift);
		if (gift.getId() > 0)
		{
			return mGiftRepository.save(gift);
		}
		return null;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = GiftSvcApi.SAVE_GIFT_PATH, method = RequestMethod.POST)
	public @ResponseBody
	Gift saveGift(@RequestBody Gift gift, Principal principal)
	{
		if (gift.getId() == 0)
		{
			throw new IllegalArgumentException();
		}
		
		return mGiftRepository.save(gift);
	}
	
	@Multipart
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = GiftSvcApi.GIFT_DATA_PATH, method = RequestMethod.POST)
	public @ResponseBody
	ImageStatus setImageData(@PathVariable(GiftSvcApi.ID_PARAM) long giftId, @RequestParam(GiftSvcApi.DATA_PARAMETER) MultipartFile giftData)
	{
		Gift gift = mGiftRepository.findOne(giftId);
		if (gift != null)
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
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = GiftSvcApi.GIFT_DATA_PATH, method = RequestMethod.GET)
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
	
	private List<GiftInfo> reverse(Iterable<Gift> gifts, String userName)
	{
		List<Gift> giftsList = Lists.newArrayList(gifts);
		Collections.reverse(giftsList);
		
		List<GiftInfo> giftsInfoList = Lists.newArrayList();
		for (Gift gift : giftsList)
		{
			giftsInfoList.add(new GiftInfo(gift, mVoteRepository.findByUserNameAndGiftId(userName, gift.getId())));
		}
		
		return giftsInfoList;
	}
}
