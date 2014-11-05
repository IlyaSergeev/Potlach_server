package com.ilya.sergeev.potlach;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilya.sergeev.potlach.client.VoteSvcApi;
import com.ilya.sergeev.potlach.repository.Vote;
import com.ilya.sergeev.potlach.repository.VoteInfo;
import com.ilya.sergeev.potlach.repository.VoteRepository;

public class VoteController
{
	@Autowired
	VoteRepository mVoteRepository;
	
	@PreAuthorize("hasRole('USER'")
	@RequestMapping(value = VoteSvcApi.VOTE_PATH)
	public @ResponseBody
	VoteInfo getVote(@PathVariable(VoteSvcApi.ID_PARAM) long giftId, Principal principal)
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
				downVotes--;
			}
		}
		voteInfo.setVotesDown(downVotes);
		voteInfo.setVotesUp(upVotes);
		return voteInfo;
	}
	
	@PreAuthorize("hasRole('USER'")
	@RequestMapping(value = VoteSvcApi.VOTE_UP_PATH, method = RequestMethod.POST)
	public @ResponseBody
	Vote sendVoteUp(@PathVariable(VoteSvcApi.ID_PARAM) long giftId, Principal principal)
	{
		return vote(giftId, principal.getName(), 1);
	}
	
	@PreAuthorize("hasRole('USER'")
	@RequestMapping(value = VoteSvcApi.VOTE_DOWN_PATH, method = RequestMethod.POST)
	public @ResponseBody
	Vote sendVoteDown(@PathVariable(VoteSvcApi.ID_PARAM) long giftId, Principal principal)
	{
		return vote(giftId, principal.getName(), -1);
	}
	
	private Vote vote(long giftId, String userName, int voteValue)
	{
		Vote vote = mVoteRepository.findByUserNameAndGiftId(userName, giftId);
		if (vote == null)
		{
			vote = new Vote();
			vote.setGiftId(giftId);
			vote.setUserName(userName);
		}
		vote.setVote(voteValue);
		return mVoteRepository.save(vote);
	}
}
