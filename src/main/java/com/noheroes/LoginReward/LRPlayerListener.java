package com.noheroes.LoginReward;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

//Can this be async?  If it doesn't use any bukkit calls, then it can raise an event when its done.

public class LRPlayerListener implements Listener {
	
    private LoginReward plugin;
    
    public LRPlayerListener(LoginReward instance) {
        plugin = instance;
    }
	
    @EventHandler(priority= EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        LoginReward.slog("Player joined: " + event.getPlayer().getName());
        
        Player player = event.getPlayer();
                
        if(!LoginReward.getPlayerStorage().loggedOnToday(player)){
            RewardGroup[] groups = plugin.getGroups();
            RewardGroup highestRank = null;
            
            for(RewardGroup bg : groups){
                if(bg.giveBonus(player)){
                    if(LoginReward.cumulative){
                        giveBonus(player, bg);
                    } else {
                        LoginReward.slog("Not cumulative");
                        if(highestRank != null){
                            LoginReward.slog("bg rank: " + bg.getRank() + ", highestrank: " + highestRank.getRank());
                            if(bg.getRank() >= highestRank.getRank()){
                                LoginReward.slog("overwrite: highestrank = bg");
                                highestRank = bg;
                            }
                        } else {
                            LoginReward.slog("Was null.  now = bg.");
                            highestRank = bg;
                        }
                    }
                }
            }
            
            if((!LoginReward.cumulative) && highestRank != null)
                giveBonus(player, highestRank);
            
        } else {
            LoginReward.slog("Player " + player.getName() + " has already received a bonus for today.");
        }
    }
    
    private void giveBonus(Player player, RewardGroup bg){
        LoginReward.getBalanceHandler().add(player, bg.getAmount());
        LoginReward.getPlayerStorage().recordBonus(player);
        player.sendMessage(ChatColor.GREEN + bg.getMessage());
        LoginReward.slog(player.getName() + " received a daily bonus of " + bg.getAmount() 
                + " for being in the " + bg.getName() + " group");
    }
}