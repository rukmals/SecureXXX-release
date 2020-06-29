package com.example.securex.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.securex.R;
import com.example.securex.applock.ForegroundToastService;
import com.example.securex.applock2.BackgroundManager;

import java.util.Iterator;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    LocalBroadcastManager localBroadcastManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



//        BackgroundManager.getInstance().init(this).startService();
        if (hasUsageStatsPermission(getApplicationContext())) {
            if (!isServiceRunning()) {
                ForegroundToastService.start(getBaseContext());
            }
        }

    }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        private boolean hasUsageStatsPermission (Context context){
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                    android.os.Process.myUid(), context.getPackageName());
            boolean granted = mode == AppOpsManager.MODE_ALLOWED;
            return granted;
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


