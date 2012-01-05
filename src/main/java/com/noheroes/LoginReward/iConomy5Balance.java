package com.noheroes.LoginReward;

import org.bukkit.entity.Player;
import com.iConomy.iConomy;

public class iConomy5Balance implements Balance {
	private iConomy iconomy;
	private LoginReward db;
	
	public iConomy5Balance (LoginReward instance, iConomy iconomy) {
		//super (scs);
		this.db = instance;
	}
	
        @Override
	public boolean isEnabled () {
            return iconomy.isEnabled();
	}
	
        @Override
	public void add (Player p, double amount) {
            try {
                this.add(p.getName(), amount);
            } catch (NullPointerException npe) {
                LoginReward.slog("NullPointerException: " + npe.getMessage());
            }
	}
	
        @Override
	public void add (String p, double amount) {
            try {
                iConomy.getAccount(p).getHoldings().add(amount);
            } catch (NullPointerException npe) {
                LoginReward.slog("NullPointerException: " + npe.getMessage());
            }
	}

        @Override
	public String getClassName () {
            return iConomy.class.getName();
	}

        @Override
        public String format(double amount) {
            return iConomy.format(amount);
        }
        
}
