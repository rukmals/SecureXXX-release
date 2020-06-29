package com.example.securex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.securex.applock2.RecActivity;
import com.example.securex.filesecurity.EncrptorHome;
import com.example.securex.passwordupdate.PasswordUpdateActivity;
import com.example.securex.scanner.ListAppActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_otton_nav);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.home);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.applock:
                        startActivity(new Intent(BottomNavActivity.this, RecActivity.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(BottomNavActivity.this, PasswordUpdateActivity.class));
                        finish();
                        break;
                    case R.id.filescurity:
                        startActivity(new Intent(BottomNavActivity.this, EncrptorHome.class));
                        finish();
                    case R.id.appsecurity:
                        startActivity(new Intent(BottomNavActivity.this, ListAppActivity.class));
                        finish();
                }

                return false;
            }
        });
    }
}
