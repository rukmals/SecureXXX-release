package com.example.securex.passwordupdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.securex.BottomNavActivity;
import com.example.securex.R;
import com.example.securex.about.AboutActivity;
import com.example.securex.applock.AppLockActivity;
import com.example.securex.applock2.RecActivity;
import com.example.securex.filesecurity.EncrptorHome;
import com.example.securex.scanner.ListAppActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PasswordUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_update);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.applock:
                        startActivity(new Intent(PasswordUpdateActivity.this, RecActivity.class));
                        finish();
                        break;
                    case R.id.home:
                        startActivity(new Intent(PasswordUpdateActivity.this, AboutActivity.class));
                        finish();
                        break;
                    case R.id.filescurity:
                        startActivity(new Intent(PasswordUpdateActivity.this, EncrptorHome.class));
                        finish();
                        break;
                    case R.id.appsecurity:
                        startActivity(new Intent(PasswordUpdateActivity.this, ListAppActivity.class));
                        finish();
                        break;


                }
                return false;
            }
        });


    }
}
