package com.example.securex.data;

import android.content.Context;
import android.content.SharedPreferences;

public class UserRepository {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    Context context;

    public UserRepository(Context context) {
        pref = context.getSharedPreferences("com.android.app.users",context.MODE_PRIVATE);
        editor=pref.edit();

    }


    public User getUser() {
        String Username =pref.getString("Username",null);
        String Email =pref.getString("Email",null);
        String Pin =pref.getString("Pin",null);
        String Password =pref.getString("Password",null);
        String Color =pref.getString("Color",null);
        int Size =pref.getInt("Size",0);


        return new User(Username, Email, Color, Pin, Size,Password);



    }


    public void saveUser(User user) {
        editor.clear();
        editor.putString("Username",user.getUsername());
        editor.putString("Email",user.getEmail());
        editor.putString("Color",user.getColor());
        editor.putString("Password",user.getPassword());
        editor.putString("Pin",user.getPin());
        editor.putInt("Size",user.getSize());
        editor.putString("UserStatus","Registered");

        editor.apply();

    }

    public void changeEmail(String email){

        editor.putString("Email",email);
        editor.apply();
    }

    public void changeColor(String color){

        editor.putString("Color",color);
        editor.apply();
    }
    public void updatePin(String pin){

        editor.putString("Pin",pin);
        editor.apply();
    }

    public void changeGraphical(String Color,int Size,String Password){
        editor.putString("Color",Color);
        editor.putString("Password",Password);
        editor.putInt("Size",Size);

        editor.apply();
    }

    public String getEmail(){
        return pref.getString("Email",null);
    }



}
