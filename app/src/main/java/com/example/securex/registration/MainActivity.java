package com.example.securex.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.securex.R;
import com.example.securex.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userExist();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void userExist() {
        SharedPreferences pref = getSharedPreferences("com.android.app.users",MODE_PRIVATE);
        String status = pref.getString("UserStatus","NotRegistered");
        if(status.equals("Registered")){
            this.finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
