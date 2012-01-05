package com.noheroes.LoginReward;

import org.bukkit.entity.Player;


public class DummyBalance implements Balance {
    
    public DummyBalance (LoginReward db) {
    }

    @Override
    public void add (Player p, double amount) {}
    @Override
    public void add (String p, double amount) {}

    @Override
    public String getClassName () {
            return this.getClass().getName();
    }

    @Override
    public boolean isEnabled () {
            return true;
    }

    @Override
    public String format(double amount) {
        return String.valueOf(amount);
    }
}
