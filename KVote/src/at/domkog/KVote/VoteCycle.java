package at.domkog.KVote;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class VoteCycle implements Runnable {

	public enum Time {
		DAY(0), NIGHT(13000);
		
		private int ticks;
		
		Time(int ticks) {
			this.ticks = ticks;
		}
		
		public int getTicks() {
			return this.ticks;
		}
		
	}
	
	private World w;
	
	private int schedulerTask;
	
	private Vote currentVote;
	
	private ArrayList<String> playersTime = new ArrayList<String>();
	private ArrayList<String> playersWeather = new ArrayList<String>();
	
	private Time time;
	private WeatherType weather;
	
	public VoteCycle(World w) {
		this.w = w;
		if(w.getTime() >= 13000) this.time = Time.NIGHT;
		else this.time = Time.DAY;
		if(w.hasStorm()) this.weather = WeatherType.DOWNFALL;
		else this.weather = WeatherType.CLEAR;
		this.startScheduler();
	}

	public World getWorld() {
		return this.w;
	}
	
	public Time getTime() {
		return this.time;
	}
	
	public WeatherType getWeather() {
		return this.weather;
	}
	
	@Override
	public void run() {
		if(this.time == Time.DAY) {
			if(this.w.getTime() >= 13000) {
				this.time = Time.NIGHT;
				this.playersTime.clear();
			}
		} else {
			if(this.w.getTime() < 13000) {
				this.time = Time.DAY;
				this.playersTime.clear();
			}
		}
		if(this.weather == WeatherType.DOWNFALL) {
			if(!this.w.hasStorm()) {
				this.weather = WeatherType.CLEAR;
				this.playersWeather.clear();
			}
		} else {
			if(this.w.hasStorm()) {
				this.weather = WeatherType.DOWNFALL;
				this.playersWeather.clear();
			}
		}
	}
	
	public boolean hasStartedTimeVoting(Player p) {
		return this.playersTime.contains(p.getUniqueId().toString());
	}
	
	public boolean hasStartedWeatherVoting(Player p) {
		return this.playersWeather.contains(p.getUniqueId().toString());
	}
	
	public void startNewVote(Player p, boolean type) {
		if(this.hasRunningVote()) return;
		if(type) {
			if(this.playersTime.contains(p.getUniqueId().toString())) return;
			if(this.time == Time.DAY) {
				this.currentVote = new NightVote(this, this.getWorld(), p);
			} else {
				this.currentVote = new DayVote(this, this.getWorld(), p);
			}
			this.playersTime.add(p.getUniqueId().toString());
		} else {
			if(this.playersWeather.contains(p.getUniqueId().toString())) return;
			if(this.weather == WeatherType.CLEAR) {
				this.currentVote = new StormVote(this, this.getWorld(), p);
			} else {
				this.currentVote = new SunVote(this, this.getWorld(), p);
			}
			this.playersWeather.add(p.getUniqueId().toString());
		}
	}
	
	public Vote getCurentVote() {
		return this.currentVote;
	}
	
	public void resetRunningVote() {
		this.currentVote = null;
	}
	
	public boolean hasRunningVote() {
		return !(this.currentVote == null);
	}
	
	public void startScheduler() {
		this.schedulerTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(KVote.instance, this, 0, 10);
	}
	
	public void stopScheduler() {
		Bukkit.getServer().getScheduler().cancelTask(this.schedulerTask);
	}
	
}
