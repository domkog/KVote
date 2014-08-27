package at.domkog.KVote;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.plugin.java.JavaPlugin;

import at.domkog.KVote.Listener.PlayerInteractListener;

public class KVote extends JavaPlugin {
	
	public static KVote instance;

	public static ConfigHandler config;
	
	public static HashMap<String, VoteCycle> voteCycles = new HashMap<String, VoteCycle>();
	
	@Override
	public void onEnable() {
		instance = this;
		this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
		config = new ConfigHandler(this);
		this.initVoteCycles();
		this.getLogger().info(" enabled.");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info(" disabled.");
	}
	
	public void initVoteCycles() {
		for(World w: Bukkit.getWorlds()) {
			if(w.getEnvironment() == Environment.NORMAL) voteCycles.put(w.getUID().toString(), new VoteCycle(w));
		}
	}
	
}
