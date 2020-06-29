package com.example.securex.scanner;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.securex.R;
import com.example.securex.about.AboutActivity;
import com.example.securex.applock2.RecActivity;
import com.example.securex.filesecurity.EncrptorHome;
import com.example.securex.passwordupdate.PasswordUpdateActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListAppActivity extends AppCompatActivity implements View.OnClickListener {

    public String[] can_see_location_info = { "android.permission.INTERNET",
            "android.permission.READ_PHONE_STATE","android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECEIVE_SMS","android.permission.READ_CONTACTS",
            "android.permission.WRITE_SMS","android.permission.SEND_SMS",
            "android.permission.READ_SMS","android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"};

    public String[] can_see_personal_info = { };
    public String[] can_impact_battery = { "android.permission.BLUETOOTH","android.permission.NFC"};
    public String[] can_change_system = {"android.permission.WRITE_SETTINGS","android.permission.READ_CALENDAR",
            "android.permission.BLUETOOTH","android.permission.NFC"};

    public String[] can_cost_money= {"android.permission.READ_CALL_LOG",
            "android.permission.INTERNET","android.permission.READ_PHONE_STATE",
            "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.RECEIVE_SMS",
            "android.permission.READ_CONTACTS",
            "android.permission.WRITE_SMS","android.permission.SEND_SMS",
            "android.permission.READ_SMS","android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"};





    public boolean results = false;
    public int counter;
    public int c2;
    public ArrayList<PInfo> apps;

    public Handler mHandler = new Handler();





    public ArrayList<PInfo> can_cost_money_obj = new ArrayList<PInfo>();
    public ArrayList<PInfo> can_see_personal_info_obj = new ArrayList<PInfo>();
    public ArrayList<PInfo> can_impact_battery_obj = new ArrayList<PInfo>();
    public ArrayList<PInfo> can_change_system_obj = new ArrayList<PInfo>();
    public ArrayList<PInfo> can_see_location_info_obj = new ArrayList<PInfo>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final ListAppActivity something = this;
        setBotNav();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                apps = getInstalledApps(false);

                Button b = (Button) findViewById(R.id.button1);
                b.setOnClickListener(something);
            }
        }, 500);









    }



    //this class get Application information from the device
    public static class PInfo implements Parcelable {

        public String appname = "";
        public String pname = "";
        public int icon = 0;
        public ArrayList<String> permissions = new ArrayList<String>();
        public ArrayList<String> critical = new ArrayList<String>();


        public PInfo() {
            this.appname = appname;
            this.pname = pname;
            this.icon = icon;
            this.permissions = permissions;
            this.critical = critical;
            ;
        }

        private PInfo(Parcel in) {
            appname = in.readString();
            pname = in.readString();
            icon = in.readInt();
            try{
                in.readStringList(permissions);
                in.readStringList(critical);
            }catch(Exception e){

            };

        }

        @Override
        public int describeContents() {
            // TODO Auto-generated method stub
            return 0;
        }
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            // TODO Auto-generated method stub
            dest.writeString(appname);
            dest.writeString(pname);
            dest.writeInt(icon);
            try{
                dest.writeStringList(permissions);
                dest.writeStringList(critical);
            }catch(Exception e){

            };


        }



        public static final Parcelable.Creator<PInfo> CREATOR = new Parcelable.Creator<PInfo>() {

            public PInfo createFromParcel(Parcel in) {
                return new PInfo(in);
            }

            public PInfo[] newArray(int size) {
                return new PInfo[size];
            }
        };




    }

    public String[] readfile(String filename,int length){
        BufferedReader reader;
        String[] permission_list= new String[length];
        try{

            reader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
            String line;
            int i=0;
            while ((line = reader.readLine()) != null) {
                permission_list[i]=line;
                i++;

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return permission_list;
    }
    //scan the device with progress bar

    private ArrayList<PInfo> getPackages() {
        final int max = apps.size();
        final TextView percent = (TextView) findViewById(R.id.percent);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar1);

        mProgress.setVisibility(View.VISIBLE);
        mProgress.setMax(max);
        counter=0;

        mHandler.postDelayed(new Runnable(){

            public void run(){

                counter++;
                //
                if(counter<apps.size()){

                    mProgress.setProgress(counter);
                    int percent_num = (counter*100 / max);
                    percent.setText(percent_num + " %");
                    mHandler.postDelayed(this, 20);

                }else{
                    Button b1 = (Button) findViewById(R.id.button1);
                    b1.setEnabled(true);
                    b1.setText("SEE RESULTS");
                    results = true;
                    percent.setText("100 %");

                }
            }
        }, 20);

        return apps;

    }
    //get installed application
    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {

        ArrayList<PInfo> res = new ArrayList<PInfo>();


        List<PackageInfo> packs = getPackageManager().getInstalledPackages(getPackageManager().GET_PERMISSIONS);
        c2 = 0;


        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            //get non system applications
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }

            PInfo newInfo = new PInfo();
            newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
            newInfo.pname = p.packageName;;
            newInfo.icon = p.applicationInfo.icon;

            try{
                    if(p.requestedPermissions!=null)
                        for (String s : p.requestedPermissions) {
                            newInfo.permissions.add(s);
                        }
            }catch(Exception e){

            };




            boolean toAddcan_cost_money_obj = false;
            boolean toAddcan_see_personal_info_obj = false;
            boolean toAddcan_impact_battery_obj = false;
            boolean toAddcan_change_system_obj = false;
            boolean toAddcan_see_location_info_obj = false;


            if(p.requestedPermissions!=null){


                //check permission from each application with the arrays above
                for(c2=0; c2<p.requestedPermissions.length; c2++){

                    if(p.requestedPermissions[c2]!=null && Arrays.asList(can_cost_money).contains(p.requestedPermissions[c2])){
                        toAddcan_cost_money_obj = true;
                        try{
                            if(p.requestedPermissions[c2]!=null){
                                int value = ((Integer.parseInt(p.requestedPermissions[c2])*100)/10);

                                newInfo.critical.add(p.requestedPermissions[c2]);
                            }
                        }catch(Exception e){

                        };
                    }


                    if(p.requestedPermissions[c2]!=null && Arrays.asList(can_see_personal_info).contains(p.requestedPermissions[c2])){
                        toAddcan_see_personal_info_obj = true;
                        try{
                            if(p.requestedPermissions[c2]!=null){
                                newInfo.critical.add(p.requestedPermissions[c2]);
                            }
                        }catch(Exception e){

                        };
                    }


                    if(p.requestedPermissions[c2]!=null && Arrays.asList(can_see_personal_info).contains(p.requestedPermissions[c2])){
                        toAddcan_impact_battery_obj = true;
                        try{
                            if(p.requestedPermissions[c2]!=null){
                                newInfo.critical.add(p.requestedPermissions[c2]);
                            }
                        }catch(Exception e){

                        };
                    }


                    if(p.requestedPermissions[c2]!=null && Arrays.asList(can_change_system).contains(p.requestedPermissions[c2])){
                        toAddcan_change_system_obj = true;
                        try{
                            if(p.requestedPermissions[c2]!=null){
                                newInfo.critical.add(p.requestedPermissions[c2]);
                            }
                        }catch(Exception e){

                        };
                    }


                    if(p.requestedPermissions[c2]!=null && Arrays.asList(can_see_location_info).contains(p.requestedPermissions[c2])){
                        toAddcan_see_location_info_obj = true;
                        try{
                            if(p.requestedPermissions[c2]!=null){
                                newInfo.critical.add(p.requestedPermissions[c2]);
                            }
                        }catch(Exception e){

                        };
                    }


                }


                newInfo.permissions = new ArrayList<String>();


                if(toAddcan_cost_money_obj){
                    can_cost_money_obj.add(newInfo);
                }


                if(toAddcan_see_personal_info_obj){
                    can_see_personal_info_obj.add(newInfo);
                }


                if(toAddcan_impact_battery_obj){
                    can_impact_battery_obj.add(newInfo);
                }


                if(toAddcan_change_system_obj){
                    can_change_system_obj.add(newInfo);
                }


                if(toAddcan_see_location_info_obj){
                    can_see_location_info_obj.add(newInfo);
                }


                res.add(newInfo);

            }


        }

        return res;
    }


    @Override
    public void onClick(View arg0) {
        //do scan
        if(!results){
            arg0.setEnabled(false);
            getPackages();
        }
        // go to results
        else{



            Intent myIntent = new Intent(ListAppActivity.this, Result.class);

            try{


                //pass application information to the result class
                myIntent.putParcelableArrayListExtra("can_cost_money_obj", can_cost_money_obj);
                myIntent.putParcelableArrayListExtra("can_see_personal_info_obj", can_see_personal_info_obj);
                myIntent.putParcelableArrayListExtra("can_impact_battery_obj", can_impact_battery_obj);
                myIntent.putParcelableArrayListExtra("can_change_system_obj", can_change_system_obj);
                myIntent.putParcelableArrayListExtra("can_see_location_info_obj", can_see_location_info_obj);

                startActivity(myIntent);
                finish();
            }catch(Exception e){

            };

        }
    }

    public void setBotNav(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.appsecurity);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.applock:
                        startActivity(new Intent(ListAppActivity.this, RecActivity.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(ListAppActivity.this, PasswordUpdateActivity.class));
                        finish();
                        break;
                    case R.id.filescurity:
                        startActivity(new Intent(ListAppActivity.this, EncrptorHome.class));
                        finish();
                        break;
                    case R.id.home:
                        startActivity(new Intent(ListAppActivity.this, AboutActivity.class));
                        break;

                }

                return false;
            }
        });
    }



}
