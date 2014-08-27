package at.domkog.KVote;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VoteProperty {

	private Vote vote;
	
	private int votesRequired;
	
	private ArrayList<String> yesVotes = new ArrayList<String>();
	private ArrayList<String> noVotes = new ArrayList<String>();
	
	public VoteProperty(Vote vote) {
		this.vote = vote;
		this.votesRequired = (KVote.config.getRequiredVotes() * Bukkit.getOnlinePlayers().length) / 100;
	}
	
	public Vote getVote() {
		return this.vote;
	}
	
	public int getRequiredVotes() {
		return this.votesRequired;
	}
	
	public int calcRequiredVotes() {
		return (this.getRequiredVotes() + this.getNoVotes());
	}
	
	public void vote(Player p, boolean arg) {
		if(this.hasVoted(p)) return;
		if(arg) this.yesVotes.add(p.getUniqueId().toString());
		else this.noVotes.add(p.getUniqueId().toString());
	}
	
	public boolean hasVoted(Player p) {
		return (this.yesVotes.contains(p.getUniqueId().toString()) || this.noVotes.contains(p.getUniqueId().toString()));
	}
	
	public int getYesVotes() {
		return this.yesVotes.size();
	}
	
	public int getNoVotes() {
		return this.noVotes.size();
	}
	
	public boolean result() {
		return ((this.yesVotes.size() - this.noVotes.size()) >= this.votesRequired);
	}
	
}
