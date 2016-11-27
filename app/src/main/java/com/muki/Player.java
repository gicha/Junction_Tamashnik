package com.muki;


import java.util.Date;

class Player {
    public String name = null;
    int health = -1;
    int satiety = -1;

    Player() {
        name = Preferences.getString("name");
        if (name == null)
            name = "";
        health = Preferences.getInt("health");
        if (health == -1)
            health = 100;

        satiety = Preferences.getInt("satiety");
        if (satiety == -1)
            satiety = 100;

    }

    public void setName(String name) {
        Preferences.setString("name", name);
        this.name = name;
    }


    void takeFood() {
        setSatiety(100);
        Preferences.setInt("timeFood", (int) new Date().getTime() / 1000);
    }

    void putHealth() {
        setHealth(100);
        Preferences.setInt("timeHealth", (int) new Date().getTime() / 1000);
    }

    public int getHealth() {
        return health;
    }

    private void setHealth(int health) {
        Preferences.setInt("health", health);
        this.health = health;
    }

    public int getSatiety() {
        return satiety;
    }

    private void setSatiety(int satiety) {
        Preferences.setInt("satiety", satiety);
        this.satiety = satiety;
    }

    public int getTimeFood() {
        return Preferences.getInt("timeFood");
    }

    public int getTimeHealth() {
        return Preferences.getInt("timeHealth");
    }
}
