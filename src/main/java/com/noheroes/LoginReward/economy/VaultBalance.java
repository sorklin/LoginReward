/*
 * Copyright (C) 2012 Sorklin <sorklin at gmail.com>
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
package com.noheroes.LoginReward.economy;

import com.noheroes.LoginReward.LoginReward;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

/**
 *
 * @author Sorklin <sorklin at gmail.com>
 */
public class VaultBalance implements Balance {
    private Economy eco = null;
    private LoginReward lr;
    
    public VaultBalance(LoginReward instance, Economy economy){
        this.eco = economy;
        this.lr = instance;
    }
    
    public String getClassName() {
        return this.eco.getClass().getCanonicalName();
    }

    public boolean isEnabled() {
        return this.eco != null;
    }

    public void add(Player p, double amount) {
        eco.bankDeposit(p.getName(), amount);
    }

    public void add(String p, double amount) {
        eco.bankDeposit(p, amount);
    }

    public String format(double amount) {
        return eco.format(amount);
    }
}
