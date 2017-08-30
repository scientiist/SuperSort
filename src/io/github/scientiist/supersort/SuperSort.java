package io.github.scientiist.supersort;

import org.bukkit.plugin.java.JavaPlugin;


public class SuperSort extends JavaPlugin {
	
	public static SuperSort pl;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new PlayerPressEvent(), this);
		pl = this;
		//getConfig().options().copyDefaults(true);
        //saveConfig();
		this.saveDefaultConfig();
	}
	
	@Override
	public void onDisable() {
		
	}
}
