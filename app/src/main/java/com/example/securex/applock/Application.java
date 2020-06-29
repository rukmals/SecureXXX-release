package com.example.securex.applock;

import android.graphics.drawable.Drawable;

public class Application {

    private String Name;
    private Drawable Icon;
    private boolean isLock;
    private String Package;

    public Application(String name, Drawable icon,String apackage) {
        Name = name;
        Icon = icon;
        Package =apackage;

    }

    public String getName() {
        return Name;
    }

    public Drawable getIcon() {
        return Icon;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public String getPackage() {
        return Package;
    }
}
