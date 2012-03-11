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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
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

    @EventHandler(priority= EventPriority.MONITOR, ignoreCancelled=true)
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin p = event.getPlugin();
        if(p.getName().equalsIgnoreCase("vault")){
            LoginReward.setBalanceHandler(new DummyBalance(db));
            LoginReward.slog("Unhooked Vault. Using Dummy balance handler.");
        }
    }
}
