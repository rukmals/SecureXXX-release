package com.example.securex.applock;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securex.R;

public class PermissionActivity extends AppCompatActivity {

    private TextView tvPermission;
    private Button btUsagePermission;
    private Button btStartService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(hasUsageStatsPermission(this)){
            startActivity(new Intent(this,AppLockActivity.class));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissionctivity);

        tvPermission = (TextView) findViewById(R.id.permtext);
        btUsagePermission = (Button) findViewById(R.id.usageperm);
        btStartService = (Button) findViewById(R.id.statperm);

        if(!needsUsageStatsPermission()) {
            btUsagePermission.setVisibility(View.GONE);
            tvPermission.setText("PERMISSION GRANTED");
        } else {
            btUsagePermission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestUsageStatsPermission();
                }
            });
        }
        btStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForegroundToastService.start(getBaseContext());
                Toast.makeText(getBaseContext(), "START", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    private boolean needsUsageStatsPermission() {
        return postLollipop() && !hasUsageStatsPermission(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void requestUsageStatsPermission() {
        if(!hasUsageStatsPermission(this)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    private boolean postLollipop() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean hasUsageStatsPermission(PermissionActivity context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }
}
