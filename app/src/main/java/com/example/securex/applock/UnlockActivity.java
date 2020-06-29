package com.example.securex.applock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.securex.R;
import com.example.securex.applock2.Utils;
import com.goodiebag.pinview.Pinview;

public class UnlockActivity extends AppCompatActivity {


    String current_app;
    SharedPreferences pref;
    Pinview pinview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        pref = getApplicationContext().getSharedPreferences("com.android.app.users",getApplicationContext().MODE_PRIVATE);
        pinview =(Pinview) findViewById(R.id.PinUnlockApp);
        Log.d("APP","UNLOCK");
        final Utils utils = new Utils(this);

        String last =  utils.getLastApp();
        Drawable icon = null;
        try {
            icon = getApplicationContext().getPackageManager().getApplicationIcon(last);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ImageView iconView = (ImageView) findViewById(R.id.app_icon);
        iconView.setImageDrawable(icon);

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
        @Override
        public void onDataEntered(Pinview pinview, boolean fromUser) {
            String pin = pinview.getValue().toString();
            if(pin.equals(pref.getString("Pin","no"))){
                Log.d("LATT@",utils.getLastApp());
                utils.clearLastApp();
                finish();
            }
            else{
                pinview.clearValue();

            }
        }
    });




    }

    @Override
    public void onBackPressed() {
        String pin = pinview.getValue().toString();
        if(!pin.equals(pref.getString("Pin","no"))){
            startCurrentHomePackage();
        }
        else{
            finish();
            super.onBackPressed();}
        }




    private void startCurrentHomePackage() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY);

        ActivityInfo activityInfo = resolveInfo.activityInfo;
        ComponentName componentName = new ComponentName(activityInfo.applicationInfo.packageName,activityInfo.name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
        new Utils(getApplicationContext()).clearLastApp();
        finish();

    }


}
