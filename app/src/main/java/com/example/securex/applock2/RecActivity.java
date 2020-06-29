package com.example.securex.applock2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.securex.R;
import com.example.securex.about.AboutActivity;
import com.example.securex.applock.ForegroundToastService;
import com.example.securex.filesecurity.EncrptorHome;
import com.example.securex.passwordupdate.PasswordUpdateActivity;
import com.example.securex.scanner.ListAppActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecActivity extends AppCompatActivity {

    Button button;
    CardView cardView;
    Button displayover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec);

        initView();
        setNav();

    }

    private void initView() {

        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppAdapter appAdpater = new AppAdapter(this,getAppApps());
        recyclerView.setAdapter(appAdpater);

        button = (Button) findViewById(R.id.permisiion);
        cardView = (CardView) findViewById(R.id.permission_card);
        displayover=(Button) findViewById(R.id.overthedisplay);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            displayover.setVisibility(View.GONE);
        }
        displayover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPermission(v);
            }
        });
    }

    private List<AppItem> getAppApps() {
        List<AppItem> appItems = new ArrayList<>();

        PackageManager pk = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pk.queryIntentActivities(intent,0);

        for (ResolveInfo resolveInfo : resolveInfos){
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            appItems.add(new AppItem(activityInfo.loadIcon(pk),activityInfo.loadLabel(pk).toString(),activityInfo.packageName));
        }



        return appItems;
    }

    public  void  setPermission(View view){
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            if(Utils.checkPermission(this) && Settings.canDrawOverlays(this)){
                cardView.setVisibility(View.GONE);
                if (!isServiceRunning()) {
                    ForegroundToastService.start(getBaseContext());
                }
            }
        }
        super.onResume();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP && Settings.canDrawOverlays(this)){
            if(Utils.checkPermission(this)){
                cardView.setVisibility(View.GONE);
                if (!isServiceRunning()) {
                    ForegroundToastService.start(getBaseContext());
                }
            }
        }
        super.onStart();
    }

    public void setNav(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.applock);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(RecActivity.this, PasswordUpdateActivity.class));
                        finish();
                        break;
                    case R.id.filescurity:
                        startActivity(new Intent(RecActivity.this, EncrptorHome.class));
                        finish();
                        break;
                    case R.id.appsecurity:
                        startActivity(new Intent(RecActivity.this, ListAppActivity.class));
                        finish();
                        break;
                    case R.id.home:
                        startActivity(new Intent(RecActivity.this, AboutActivity.class));
                        break;
                }

                return false;
            }
        });
    }

    private boolean isServiceRunning () {
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(50);
        Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = i
                    .next();

            if (runningServiceInfo.service.getClassName().equals("com.example.securex.applock.ForegroundToastService")) {
                serviceRunning = true;

                if (runningServiceInfo.foreground) {
                    //service run in foreground
                }
            }
        }
        return serviceRunning;
    }


}
