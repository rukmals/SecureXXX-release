package com.example.securex.applock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.securex.BottomNavActivity;
import com.example.securex.R;
import com.example.securex.passwordupdate.PasswordUpdateActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppLockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.applock);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(AppLockActivity.this, PasswordUpdateActivity.class));
                        finish();
                        break;
                    case R.id.home:
                        startActivity(new Intent(AppLockActivity.this, BottomNavActivity.class));
                        finish();
                        break;

                }

                return false;
            }
        });

    }
}
