package at.domkog.KVote;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigHandler {

	private File f;
	private FileConfiguration config;
	
	public ConfigHandler(KVote instance) {
		this.f = new File(instance.getDataFolder(), "config.yml");
		this.config = YamlConfiguration.loadConfiguration(this.f);
		this.addDefaults();
	}
	
	public int getRequiredVotes() {
		return this.config.getInt("requiredVotes");
	}
	
	public int getVoteTime() {
		return this.config.getInt("voteTime");
	}
	
	public void addDefaults() {
		this.config.options().header("requiredVotes in % ; voteTime in seconds");
		this.config.addDefault("requiredVotes", 50);
		this.config.addDefault("voteTime", 60);
		this.config.options().copyDefaults(true);
		this.config.options().copyHeader(true);
		this.saveConfig();
	}
	
	public void saveConfig() {
		try {
			this.config.save(this.f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
