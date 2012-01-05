package com.noheroes.LoginReward;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;

/*
 * Now uses MiniDB by Nijkokun.
 * TODO: Item bonus
 * TODO: per group cumulative/unique
 */

public class LoginReward extends JavaPlugin {
    
	private static final Logger logr = Logger.getLogger("Minecraft");
        
	private static Balance iConomy = null;
        private static PlayerStorage ps = null;
	private static Server server = null;
        private static LoginReward db;
        
        public static boolean cumulative = false;
        
	private final LRPlayerListener playerListener = new LRPlayerListener(this);
        private final LRPluginListener pluginListener = new LRPluginListener(this);
        
        private BonusGroup[] bonusGroups;
	 
	@Override
	public void onDisable() {
		LoginReward.slog("Plugin Disabled");
	}

	@Override
	public void onEnable() {
            PluginManager pm = getServer().getPluginManager();
            pm.registerEvent(Type.PLAYER_JOIN, playerListener, Event.Priority.Monitor, this);
            pm.registerEvent(Type.PLUGIN_ENABLE, pluginListener, Event.Priority.Monitor, this);
            pm.registerEvent(Type.PLUGIN_DISABLE, pluginListener, Event.Priority.Monitor, this);
            
            server = getServer();
            LoginReward.db = this;
            
            //Try once here, otherwise just listen for it.
            if(pm.isPluginEnabled("iConomy")){
                if(pm.getPlugin("iConomy").getClass().getName().equals("com.iCo6.iConomy")) {
                    iConomy = new iConomy6Balance(this, (com.iCo6.iConomy)pm.getPlugin("iConomy"));
                    LoginReward.slog("Hooked iConomy 6.");
                } 
                
                else if(pm.getPlugin("iConomy").getClass().getName().equals("com.iConomy.iConomy")) {
                    iConomy = new iConomy5Balance(this, (com.iConomy.iConomy)pm.getPlugin("iConomy"));
                    LoginReward.slog("Hooked iConomy 5.");
                }
            } else {
                iConomy = new DummyBalance(this);
                LoginReward.slog("No economy plugin found. Using Dummy economy.");
            }
            
            this.loadconfig(this.getConfig());
            this.saveConfig();
            
            for(BonusGroup b : bonusGroups){
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
        return iConomy;
    }
    
    public static void setBalanceHandler(Balance handler) {
        iConomy = handler;
    }
    
    public static PlayerStorage getPlayerStorage(){
        return ps;
    }
    
    public static LoginReward get(){
        return LoginReward.db;
    }
    
    public static void slog(String message){
        logr.log(Level.INFO, "[DailyBonusNH] " + message);
    }
    
    public static void log(Level level, String message){
        logr.log(level, message);
    }
    
    public BonusGroup[] getGroups(){
        return bonusGroups;
    }
    
    private void loadconfig(Configuration dbConfig){
        dbConfig.options().copyDefaults(true);
        LoginReward.cumulative = dbConfig.getBoolean("Cumulative");
        
        List<BonusGroup> bg = new ArrayList<BonusGroup>();
        
        String name = "";
        String msg = "";
        double amt = 0.0;
        int rank = 0;
        
        Set<String> keys = dbConfig.getConfigurationSection("BonusGroups").getKeys(false);
        Iterator<String> it = keys.iterator();
        while (it.hasNext()){
            name = it.next();
            msg = dbConfig.getString("BonusGroups." + name + ".Message", "You earned $amount$ for logging in today!");
            amt = dbConfig.getDouble("BonusGroups." + name + ".Amount", 0.0);
            rank = dbConfig.getInt("BonusGroups." + name + ".Rank", 0);
            bg.add(new BonusGroup(name, amt, msg, rank));
        }
        bonusGroups = new BonusGroup[bg.size()];
        bonusGroups = bg.toArray(bonusGroups);
    }
}