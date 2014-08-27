package at.domkog.KVote;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class Vote implements Runnable {

	private VoteProperty property;
	
	private VoteCycle cycle;
	private World w;
	private String player;
	
	private int schedulerTask;
	
	private int timeCounter = KVote.config.getVoteTime();
	
	public Vote(VoteCycle cycle, World w, Player p) {
		this.cycle = cycle;
		this.w = w;
		this.player = p.getName();
		this.property = new VoteProperty(this);
		this.startScheduler();
	}
	
	public VoteProperty getProperty() {
		return this.property;
	}
	
	public VoteCycle getVoteCycle() {
		return this.cycle;
	}
	
	public World getWorld() {
		return this.w;
	}

	public String getPlayer() {
		return this.player;
	}
	
	public abstract void onVoteStart();
	public abstract void onVoteAdopted();
	public abstract void onVoteRejected();
	
	public void startScheduler() {
		this.onVoteStart();
		this.schedulerTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(KVote.instance, this, 0L, 20L);
	}
	
	@Override
	public void run() {
		boolean result = this.property.result();
		String resultString;
		if(result) resultString = "adopted";
		else resultString = "rejected";
		if((this.timeCounter % 20) == 0 || this.timeCounter == 5) {
			for(Player p: this.w.getPlayers()) {
				p.sendMessage("");
				p.sendMessage(ChatColor.GOLD + "" + this.timeCounter + " seconds left.");
				p.sendMessage(ChatColor.GOLD + "Yes: " + ChatColor.YELLOW + this.property.getYesVotes() + ChatColor.GOLD + " No: " + ChatColor.YELLOW + this.property.getNoVotes() + ChatColor.GOLD + " Result: " + ChatColor.YELLOW + resultString);
				p.sendMessage(ChatColor.GOLD + "Required yes votes: " + ChatColor.YELLOW + this.property.calcRequiredVotes());
			}
		}
		this.timeCounter--;
		if(this.timeCounter == 0) {
			if(result) this.onVoteAdopted();
			else this.onVoteRejected();
			this.getVoteCycle().resetRunningVote();
			Bukkit.getServer().getScheduler().cancelTask(this.schedulerTask);
		}
	}
	
}
