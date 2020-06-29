package com.example.securex.filesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.securex.R;
import com.example.securex.about.AboutActivity;
import com.example.securex.applock2.RecActivity;
import com.example.securex.passwordupdate.PasswordUpdateActivity;
import com.example.securex.scanner.ListAppActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EncrptorHome extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Button img=(Button)findViewById(R.id.img);
        final Button video=(Button)findViewById(R.id.video);
        final Button audio=(Button)findViewById(R.id.audio);
        final Button folder=(Button)findViewById(R.id.folder);
        final Button txt=(Button)findViewById(R.id.txt);
        final Button any=(Button)findViewById(R.id.any);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(EncrptorHome.this,ImageEncrypt.class);
                startActivity(n);

            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(EncrptorHome.this,VideoEncrypt.class);
                startActivity(n);

            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(EncrptorHome.this,AudioEncrypt.class);
                startActivity(n);

            }
        });
        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(EncrptorHome.this,FolderEncrypt.class);
                startActivity(n);

            }
        });
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(EncrptorHome.this,TextEncyrpt.class);
                startActivity(n);

            }
        });
        any.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(EncrptorHome.this,FormatEncryption.class);
                startActivity(n);

            }
        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.filescurity);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.applock:
                        startActivity(new Intent(EncrptorHome.this, RecActivity.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(EncrptorHome.this, PasswordUpdateActivity.class));
                        finish();
                        break;
                    case R.id.appsecurity:
                        startActivity(new Intent(EncrptorHome.this, ListAppActivity.class));
                        finish();
                        break;
                    case R.id.home:
                        startActivity(new Intent(EncrptorHome.this, AboutActivity.class));
                        break;
//                        finish();

                }

                return false;
            }
        });
    }

}
