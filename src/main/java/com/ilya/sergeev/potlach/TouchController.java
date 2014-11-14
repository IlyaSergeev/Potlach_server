package com.ilya.sergeev.potlach;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.Touch;
import com.ilya.sergeev.potlach.client.TouchSvcApi;
import com.ilya.sergeev.potlach.client.UserInfo;
import com.ilya.sergeev.potlach.repository.GiftRepository;
import com.ilya.sergeev.potlach.repository.TouchRepository;
import com.ilya.sergeev.potlach.repository.UserInfoRepository;

@Controller
public class TouchController
{
	@Autowired
	TouchRepository mTouchRepository;
	
	@Autowired
	UserInfoRepository mUserRepository;
	
	@Autowired
	GiftRepository mGiftRepository;
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = TouchSvcApi.TOUCH_PATH, method = RequestMethod.POST)
	public @ResponseBody
	Touch touch(@RequestParam(TouchSvcApi.GIFT_ID_PARAM) long giftId, Principal principal)
	{
		String userName = principal.getName();
		Touch touch = mTouchRepository.findOneByUserNameAndGiftId(userName, giftId);
		if (touch == null)
		{
			touch = new Touch(userName, giftId);
			touch = mTouchRepository.save(touch);
		}
		
		Gift gift = mGiftRepository.findOne(giftId);
		gift.setRating(gift.getRating()+1);
		mGiftRepository.save(gift);
		
		UserInfo user = mUserRepository.findByName(gift.getOwner());
		user.setRating(user.getRating() + 1);
		mUserRepository.save(user);
		
		return touch;
	}
}
