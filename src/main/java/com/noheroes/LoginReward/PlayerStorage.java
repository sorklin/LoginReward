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

import com.mini.Arguments;
import com.mini.Mini;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.entity.Player;

/**
 *
 * @author Sorklin <sorklin at gmail.com>
 */
public class PlayerStorage {
    private LoginReward db;
    
    private Mini miniPS;
    private File mini_file;
    
    
    public PlayerStorage(LoginReward instance){
        this.db = instance;
        this.initFile();
    }
    
    private void initFile() {
        try {
            mini_file = new File(db.getDataFolder(), "DailyBonus.mini");
            
            if(!mini_file.exists()){
                mini_file.createNewFile();
            }
        
            miniPS = new Mini(mini_file.getParent(), mini_file.getName());
        } catch (IOException ioe) {
            LoginReward.slog("Could not find/create/connect to DailyBonus.mini.");
            ioe.printStackTrace();
        }
    }
    
    public boolean loggedOnToday(Player player){
//        LoginReward.slog("loggedOnToday started.");
        
        if(miniPS == null){
            LoginReward.log(Level.WARNING, "Minidb is null. No bonuses will be given until it is fixed.");
            return true;
        }
        
        String name = player.getName();
        
        Arguments entry = null;
        if(miniPS.hasIndex(name))
            entry = miniPS.getArguments(name);
        else 
            return false;
        
        if(entry == null){
            LoginReward.slog("Could not retrieve argument player. No bonus given.");
            return true;
        }
        
        Calendar now = Calendar.getInstance();
        Calendar lastLoggedIn = stringToDate(entry.getValue("last"));
        
        if(now.after(lastLoggedIn)){
//            LoginReward.slog("Logging in after recorded date: " + entry.getValue("last"));
//            LoginReward.slog("now: " + now.get(Calendar.DAY_OF_YEAR) 
//                    + ", last: " + lastLoggedIn.get(Calendar.DAY_OF_YEAR));
            return (now.get(Calendar.DAY_OF_YEAR) == lastLoggedIn.get(Calendar.DAY_OF_YEAR))
                    ? true : false;
        }
        
        return false;
    }
    
    public void recordBonus(Player player){
        
        if(miniPS == null){
            LoginReward.log(Level.WARNING, "Minidb is null. Cannot record reward.");
            return;
        }
            
        Calendar now = Calendar.getInstance();
        String name = player.getName();
        String date = new SimpleDateFormat("MM/dd/yy").format(now.getTime());
        
        Arguments entry = new Arguments(name);
        entry.setValue("last", date);
        miniPS.addIndex(entry.getKey(), entry);
        miniPS.update();        
    }
    
    // Convert from String to date
    private Calendar stringToDate(String date) {
        try {
            Date dt = new SimpleDateFormat("MM/dd/yy").parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            return cal;
        } catch (ParseException e) {
            LoginReward.log(Level.WARNING, "Exception parsing date: " + e.getMessage());
            return null;
        }
    }
}
