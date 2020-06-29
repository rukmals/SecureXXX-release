package com.example.securex.applock2;

import android.graphics.drawable.Drawable;

public class AppItem {

    private Drawable icon;
    private String name;
    private String packageName;

    public AppItem(Drawable icon, String name, String packageName) {
        this.icon = icon;
        this.name = name;
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }
}