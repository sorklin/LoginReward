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

import com.noheroes.LoginReward.economy.DummyBalance;
import com.noheroes.LoginReward.economy.iConomy5Balance;
import com.noheroes.LoginReward.economy.iConomy6Balance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Sorklin <sorklin at gmail.com>
 */
public class LRPluginListener implements Listener {
    
    private LoginReward db;
    
    public LRPluginListener(LoginReward instance){
        this.db = instance;
    }

    @EventHandler(priority= EventPriority.MONITOR)
    public void onPluginEnable(PluginEnableEvent event) {
        Plugin p = event.getPlugin();
        
        if(p.getClass().getName().equals("com.iCo6.iConomy")) {
            LoginReward.setBalanceHandler(new iConomy6Balance(db, (com.iCo6.iConomy)p));
            LoginReward.slog("Hooked iConomy 6.");
        } 

        else if(p.getClass().getName().equals("com.iConomy.iConomy")) {
            LoginReward.setBalanceHandler(new iConomy5Balance(db, (com.iConomy.iConomy)p));
            LoginReward.slog("Hooked iConomy 5.");
        }
    }

    @EventHandler(priority= EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin p = event.getPlugin();
        if(p.getClass().getName().equals("com.iCo6.iConomy")) {
            LoginReward.setBalanceHandler(new DummyBalance(db));
            LoginReward.slog("Unhooked iConomy 6. Using Dummy balance handler.");
        }
        
        else if(p.getClass().getName().equals("com.iConomy.iConomy")) {
            LoginReward.setBalanceHandler(new DummyBalance(db));
            LoginReward.slog("Unhooked iConomy 5. Using Dummy balance handler.");
        }
    }
}
