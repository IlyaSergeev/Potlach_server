package com.ilya.sergeev.potlach;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilya.sergeev.potlach.client.VoteSvcApi;
import com.ilya.sergeev.potlach.repository.Vote;
import com.ilya.sergeev.potlach.repository.VoteInfo;
import com.ilya.sergeev.potlach.repository.VoteRepository;

@Controller
public class VoteController
{
	@Autowired
	VoteRepository mVoteRepository;
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = VoteSvcApi.VOTE_PATH, method = RequestMethod.GET)
	public @ResponseBody
	VoteInfo getVoteOfGift(@RequestParam(VoteSvcApi.GIFT_ID_PARAM) long giftId, Principal principal)
	{
		VoteInfo voteInfo = new VoteInfo();
		voteInfo.setUserVote(mVoteRepository.findByUserNameAndGiftId(principal.getName(), giftId));
		Collection<Vote> allVotes = mVoteRepository.findByGiftId(giftId);
		if (allVotes == null)
		{
			return voteInfo;
			
		}
		int downVotes = 0;
		int upVotes = 0;
		for (Vote vote : allVotes)
		{
			if (vote.getVote() > 0)
			{
				upVotes++;
			}
			else if (vote.getVote() < 0)
			{
				downVotes++;
			}
		}
		voteInfo.setVotesDown(downVotes);
		voteInfo.setVotesUp(upVotes);
		return voteInfo;
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
		else
		{
			voteValue = 0;
		}
		vote.setVote(voteValue);
		return mVoteRepository.save(vote);
	}
}
