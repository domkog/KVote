package at.domkog.KVote;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DayVote extends Vote {

	public DayVote(VoteCycle cycle, World w, Player p) {
		super(cycle, w, p);
	}

	@Override
	public void onVoteAdopted() {
		for(Player p: this.getWorld().getPlayers()) {
			p.sendMessage("");
			p.sendMessage(ChatColor.GOLD + "The voting for day was adopted.");
			p.sendMessage(ChatColor.GOLD + "Yes: " + ChatColor.YELLOW + this.getProperty().getYesVotes() + ChatColor.GOLD + " No: " + ChatColor.YELLOW + this.getProperty().getNoVotes());
			p.sendMessage(ChatColor.GOLD + "Votes required: " + ChatColor.YELLOW + this.getProperty().calcRequiredVotes());
		}
		this.getWorld().setTime(0);
	}

	@Override
	public void onVoteRejected() {
		for(Player p: this.getWorld().getPlayers()) {
			p.sendMessage("");
			p.sendMessage(ChatColor.GOLD + "The voting for day was rejected.");
			p.sendMessage(ChatColor.GOLD + "Yes: " + ChatColor.YELLOW + this.getProperty().getYesVotes() + ChatColor.GOLD + " No: " + ChatColor.YELLOW + this.getProperty().getNoVotes());
			p.sendMessage(ChatColor.GOLD + "Votes required: " + ChatColor.YELLOW + this.getProperty().calcRequiredVotes());
		}
	}

	@Override
	public void onVoteStart() {
		for(Player p: this.getWorld().getPlayers()) {
			p.sendMessage(ChatColor.GOLD + this.getPlayer() + " started a new day voting.");
		}
	}

}
