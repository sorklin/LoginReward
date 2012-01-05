/*
 * Copyright (C) 2011 Sorklin <sorklin at gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.noheroes.LoginReward;

import org.bukkit.entity.Player;

/**
 *
 * @author Sorklin <sorklin at gmail.com>
 */
public class BonusGroup {
    
    private String groupName;
    private double groupAmount;
    private String groupPerm;
    private String groupMessage;
    private int rank;
    
    public BonusGroup(String name, double amount, String message, int rank){
        this.groupName = name;
        this.groupAmount = amount;
        this.groupPerm = "dailybonus.get." + name.toLowerCase();
        this.groupMessage = message.replace("$amount$", LoginReward.getBalanceHandler().format(amount));
        this.rank = rank;
    }
    
    public String getName(){
        return this.groupName;
    }
    
    public double getAmount(){
        return this.groupAmount;
    }
    
    public String getPerm(){
        return this.groupPerm;
    }
    
    public String getMessage(){
        return this.groupMessage;
    }
    
    public int getRank(){
        return this.rank;
    }
    
    public boolean giveBonus(Player player){
        return player.hasPermission(this.groupPerm);
    }
}
