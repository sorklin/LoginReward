package com.noheroes.LoginReward.economy;

import org.bukkit.entity.Player;

import com.iCo6.iConomy;
import com.iCo6.system.Accounts;
import com.noheroes.LoginReward.LoginReward;

public class iConomy6Balance implements Balance {
    
    private iConomy iconomy;
    private Accounts accounts;
    private LoginReward db;

    public iConomy6Balance (LoginReward db, iConomy iconomy) {
            this.iconomy = iconomy;
            this.db	= db;
            accounts = new Accounts ();
    }

    @Override
    public String getClassName() {
            return iconomy.getClass().getName();
    }

    @Override
    public boolean isEnabled() {
            return iconomy.isEnabled();
    }

    @Override
    public void add(Player p, double amount) {
            this.add(p.getName(), amount);		
    }

    @Override
    public void add(String p, double amount) {
        if (accounts.exists(p))
            accounts.get(p).getHoldings().add(amount);
        else
            LoginReward.slog("WARNING: add failed with player " + p);
    }

    @Override
    public String format(double amount) {
        return iConomy.format(amount);
    }	
}
