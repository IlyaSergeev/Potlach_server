package com.ilya.sergeev.potlach.repository;

public class VoteInfo
{
	private int votesUp;
	private int votesDown;
	
	private Vote userVote;

	public int getVotesUp()
	{
		return votesUp;
	}

	public void setVotesUp(int votesUp)
	{
		this.votesUp = votesUp;
	}

	public int getVotesDown()
	{
		return votesDown;
	}

	public void setVotesDown(int votesDown)
	{
		this.votesDown = votesDown;
	}

	public Vote getUserVote()
	{
		return userVote;
	}

	public void setUserVote(Vote userVote)
	{
		this.userVote = userVote;
	}
}
