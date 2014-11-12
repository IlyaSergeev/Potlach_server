package com.ilya.sergeev.potlach;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilya.sergeev.potlach.client.Vote;
import com.ilya.sergeev.potlach.client.VoteSvcApi;
import com.ilya.sergeev.potlach.repository.VoteRepository;

@Controller
public class VoteController
{
	@Autowired
	VoteRepository mVoteRepository;
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = VoteSvcApi.VOTE_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Vote getVoteOfGift(@RequestParam(VoteSvcApi.GIFT_ID_PARAM) long giftId, Principal principal)
	{		
		return mVoteRepository.findByUserNameAndGiftId(principal.getName(), giftId);
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = VoteSvcApi.SINGLE_VOTE_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Vote getVote(@PathVariable(VoteSvcApi.ID_PARAM) long voteId)
	{
		return mVoteRepository.findOne(voteId);
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = VoteSvcApi.SINGLE_VOTE_PATH, method = RequestMethod.POST)
	public @ResponseBody
	Vote sendVote(@PathVariable(VoteSvcApi.ID_PARAM) long giftId, @RequestParam(VoteSvcApi.VOTE_PARAM) int voteValue, Principal principal)
	{
		String userName = principal.getName();
		Vote vote = mVoteRepository.findByUserNameAndGiftId(userName, giftId);
		
		if (vote == null)
		{
			vote = new Vote();
			vote.setGiftId(giftId);
			vote.setUserName(userName);
		}
		
		if (voteValue > 0)
		{
			voteValue = 1;
		}
		else if (voteValue < 0)
		{
			voteValue = -1;
		}
		vote.setVote(voteValue);
		
		return mVoteRepository.save(vote);
	}
}
