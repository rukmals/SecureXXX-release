package com.example.securex.scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListPermission extends AppCompatActivity {






    TextView naslov,critical;
    int position;
    ListView listView;
    Button malware;
    Classifier classifier;
    ProgressBar progressBar;
    TextView percent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list_app);
        setContentView(R.layout.fragment_screen_slide_page2);
        percent = (TextView) findViewById(R.id.percent);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setScaleY(4);
        progressBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        listView = (ListView) findViewById(R.id.Lista);
        malware = (Button) findViewById(R.id.malware);
        classifier = Classifier.getInstance(this);
        malware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String packageName = getIntent().getStringExtra("packageName");
                List<String> per = getGrantedPermissions(packageName);
                Log.d("permissions",Integer.toString(per.size()));
                int cls=classifier.predict(per);
                Log.d("CLASS",Integer.toString(cls));

                MalwareBottomSheet bottomSheet = new MalwareBottomSheet();
                Bundle bundle = new Bundle();
                bundle.putInt("pred",cls);
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(),"bottomSheet");



            }
        });




        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.appsecurity);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.applock:
                        startActivity(new Intent(ListPermission.this, RecActivity.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(ListPermission.this, PasswordUpdateActivity.class));
                        finish();
                        break;
                    case R.id.filescurity:
                        startActivity(new Intent(ListPermission.this, EncrptorHome.class));
                        finish();
                    case R.id.home:
                        startActivity(new Intent(ListPermission.this, AboutActivity.class));
                        break;
                }

                return false;
            }
        });




        for(int i=0;i<300;i++) {
            if (position == i) {
                Intent intent = getIntent();
                Bundle bundle = this.getIntent().getExtras();
                ArrayList<String> count = intent.getStringArrayListExtra("size1");
                ArrayList<String> newList = new ArrayList<String>();
                for(int j=0;j<count.size();j++){
                    String s = count.get(j);
                    //List<String> myList = new ArrayList<String>(Arrays.asList(s.split("\\s*.\\s*")));
                    String mystring = s.substring(19,s.length());
                    newList.add(mystring);

                }
                //ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,newList);
                //critical.setText(count.get(0));
                final CustomAdapter adapter = new CustomAdapter(ListPermission.this, newList);

                listView.setAdapter(adapter);
                //listView.setAdapter(arrayAdapter);

            }
        }
        Intent intent = getIntent();
        Bundle bundle = this.getIntent().getExtras();
        ArrayList<String> count = intent.getStringArrayListExtra("size1");
        progressBar.setProgress(count.size()*100/14);
        if(count.size()*100/14> 50){
            progressBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else{
            progressBar.getProgressDrawable().setColorFilter(
                    Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        }
        int percentange = (int) count.size()*100/14;
        percent.setText(Integer.toString(percentange)+"%");


    }
    public class CustomAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<String> data;

        public CustomAdapter(Activity activity, ArrayList<String> items) {
            this.activity = activity;
            this.data = items;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null)
                convertView = inflater.inflate(R.layout.list_activity, null);

            try {


                TextView paket = (TextView) convertView.findViewById(R.id.paket);


                paket.setText(data.get(position));





            } catch (ParseException e) {

                e.printStackTrace();

            }

            return convertView;

        }
    }

    List<String> getGrantedPermissions(final String appPackage) {
        List<String> granted = new ArrayList<String>();
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    granted.add(pi.requestedPermissions[i]);
                }
            }
        } catch (Exception e) {
        }
        return granted;
    }

}
