package at.domkog.KVote.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import at.domkog.KVote.KVote;
import at.domkog.KVote.VoteCycle;
import at.domkog.KVote.VoteCycle.Time;
import at.domkog.KVote.SunVote;
import at.domkog.KVote.StormVote;

public class PlayerInteractListener implements Listener {

	@SuppressWarnings("unused")
	private KVote instance;
	
	public PlayerInteractListener(KVote plugin) {
		this.instance = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType() != Material.BED_BLOCK && e.getClickedBlock().getType() != Material.IRON_BLOCK) return;
			Player p = e.getPlayer();
			World w = p.getWorld();
			VoteCycle cycle = KVote.voteCycles.get(w.getUID().toString());
			if(cycle == null) {
				return;
			}
			if(cycle.hasRunningVote()) {
				if(cycle.getCurentVote() instanceof SunVote || cycle.getCurentVote() instanceof StormVote) {
					if(e.getClickedBlock().getType() != Material.IRON_BLOCK) return;
				} else {
					if(e.getClickedBlock().getType() != Material.BED_BLOCK) return;
				}
				if(p.getItemInHand().getType() == Material.TORCH) {
					if(!cycle.getCurentVote().getProperty().hasVoted(p)) {
						cycle.getCurentVote().getProperty().vote(p, true);
						p.sendMessage(ChatColor.GREEN + "You've voted for " + ChatColor.DARK_GREEN + "yes");
					}
					else p.sendMessage(ChatColor.RED + "You've already voted.");
				} else {
					if(!cycle.getCurentVote().getProperty().hasVoted(p)) {
						cycle.getCurentVote().getProperty().vote(p, false);
						p.sendMessage(ChatColor.GREEN + "You've voted for " + ChatColor.DARK_RED + "no");
					}
					else p.sendMessage(ChatColor.RED + "You've already voted.");
				}
			} else {
				if(p.getItemInHand().getType() == Material.TORCH) {
					if(e.getClickedBlock().getType() == Material.IRON_BLOCK) {
						if(Bukkit.getOnlinePlayers().length == 1) {
							if(cycle.getWeather() == WeatherType.CLEAR) w.setStorm(true);
							else w.setStorm(false);
						} else {
							if(!cycle.hasStartedWeatherVoting(p)) cycle.startNewVote(p, false);
							else p.sendMessage(ChatColor.RED + "You've already started a weather-voting.");
						}
					} else {
						if(Bukkit.getOnlinePlayers().length == 1) {
							if(cycle.getTime() == Time.DAY) w.setTime(Time.NIGHT.getTicks());
							else w.setTime(Time.DAY.getTicks());
						} else {
							if(!cycle.hasStartedTimeVoting(p)) cycle.startNewVote(p, true);
							else p.sendMessage(ChatColor.RED + "You've already started a time-voting in this daytime.");
						}
					}
				}
			}
		}
	}
	
}
