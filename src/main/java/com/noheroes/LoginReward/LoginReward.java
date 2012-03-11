package com.noheroes.LoginReward;

import com.noheroes.LoginReward.economy.Balance;
import com.noheroes.LoginReward.economy.DummyBalance;
import com.noheroes.LoginReward.economy.VaultBalance;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * Now uses MiniDB by Nijkokun.
 * TODO: Item bonus
 * TODO: per group cumulative/unique
 */

public class LoginReward extends JavaPlugin {
    
        private static LoginReward instance;
        
	private static Balance econ = null;
        private static PlayerStorage ps = null;
	private static Server server = null;
        private static LoginReward db;
        
        public static boolean cumulative = false;
        
	private final LRPlayerListener playerListener = new LRPlayerListener(this);
        private final LRPluginListener pluginListener = new LRPluginListener(this);
        
        private RewardGroup[] bonusGroups;
	 
	@Override
	public void onDisable() {
		LoginReward.slog("Plugin Disabled");
	}

	@Override
	public void onEnable() {
            instance = this;
            
            getServer().getPluginManager().registerEvents(playerListener, this);
            getServer().getPluginManager().registerEvents(pluginListener, this);
            
            server = getServer();
            LoginReward.db = this;
            
            log(Level.INFO, "Attempting to connect to Vault.");
            if (getServer().getPluginManager().getPlugin("Vault") != null) {
                RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
                econ = new VaultBalance(this, rsp.getProvider());
            }
            
            if(econ == null){
                econ = new DummyBalance(this);
                LoginReward.slog("Vault not found or it cannot connect to economy plugin. Using Dummy economy.");
            }
            
            this.loadconfig(this.getConfig());
            this.saveConfig();
            
            for(RewardGroup b : bonusGroups){
                LoginReward.slog("Found group: " + b.getName() + " with a " + b.getAmount() + " bonus. Permission needed to use: " + b.getPerm());
            }
            
            ps = new PlayerStorage(this);
            
            PluginDescriptionFile pdf = this.getDescription();	
            LoginReward.slog(pdf.getName() + " version " + pdf.getVersion() + " is enabled.");
	}
        
    
    public static Server getBukkitServer() {
        return server;
    }

    public static Balance getBalanceHandler() {
        return econ;
    }
    
    public static void setBalanceHandler(Balance handler) {
        econ = handler;
    }
    
    public static PlayerStorage getPlayerStorage(){
        return ps;
    }
    
    public static LoginReward get(){
        return LoginReward.db;
    }
    
    public static void slog(String message){
        instance.getLogger().log(Level.INFO, message);
    }
    
    public static void log(Level level, String message){
        instance.getLogger().log(level, message);
    }
    
    public RewardGroup[] getGroups(){
        return bonusGroups;
    }
    
    private void loadconfig(Configuration dbConfig){
        dbConfig.options().copyDefaults(true);
        LoginReward.cumulative = dbConfig.getBoolean("Cumulative");
        
        List<RewardGroup> bg = new ArrayList<RewardGroup>();
        
        String name = "";
        String msg = "";
        double amt = 0.0;
        int rank = 0;
        
        Set<String> keys = dbConfig.getConfigurationSection("RewardGroups").getKeys(false);
        Iterator<String> it = keys.iterator();
        while (it.hasNext()){
            name = it.next();
            msg = dbConfig.getString("RewardGroups." + name + ".Message", "You earned $amount$ for logging in today!");
            amt = dbConfig.getDouble("RewardGroups." + name + ".Amount", 0.0);
            rank = dbConfig.getInt("RewardGroups." + name + ".Rank", 0);
            bg.add(new RewardGroup(name, amt, msg, rank));
        }
        bonusGroups = new RewardGroup[bg.size()];
        bonusGroups = bg.toArray(bonusGroups);
    }
}