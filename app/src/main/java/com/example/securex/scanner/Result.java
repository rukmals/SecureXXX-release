package com.example.securex.scanner;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.securex.R;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.example.securex.about.AboutActivity;
import com.example.securex.applock2.RecActivity;
import com.example.securex.filesecurity.EncrptorHome;
import com.example.securex.passwordupdate.PasswordUpdateActivity;
import com.example.securex.scanner.ListAppActivity.PInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Result extends AppCompatActivity implements ActionBar.TabListener{

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private String title;

    public ArrayList<PInfo> lista;


    //get application information from listappactivity class
    public ArrayList<PInfo> can_cost_money_obj;
    public ArrayList<PInfo> can_see_personal_info_obj;
    public ArrayList<PInfo> can_impact_battery_obj;
    public ArrayList<PInfo> can_change_system_obj;
    public ArrayList<PInfo> can_see_location_info_obj;

    //MainContructor.ResultPresenter resultPresenter;

    ListView listView;

    public ArrayList<PInfo> appovi;

    public Handler handler = new Handler();


    int number;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_screen_slide_page);

        listView = (ListView) findViewById(R.id.Lista);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.appsecurity);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.applock:
                        startActivity(new Intent(Result.this, RecActivity.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(Result.this, PasswordUpdateActivity.class));
                        finish();
                        break;
                    case R.id.filescurity:
                        startActivity(new Intent(Result.this, EncrptorHome.class));
                        finish();
                    case R.id.home:
                        startActivity(new Intent(Result.this, AboutActivity.class));
                        break;

                }

                return false;
            }
        });

        //resultPresenter = new MainResultPresenter(this);

        can_cost_money_obj = getIntent().getParcelableArrayListExtra("can_cost_money_obj");
        can_see_personal_info_obj = getIntent().getParcelableArrayListExtra("can_see_personal_info_obj");
        can_impact_battery_obj = getIntent().getParcelableArrayListExtra("can_impact_battery_obj");
        can_change_system_obj = getIntent().getParcelableArrayListExtra("can_change_system_obj");
        can_see_location_info_obj = getIntent().getParcelableArrayListExtra("can_see_location_info_obj");


        lista = can_cost_money_obj;
        Sort(lista);
        final CustomAdapter adapter = new CustomAdapter(Result.this, appovi);

        listView.setAdapter(adapter);


        number = adapter.getCount();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int count = adapter.getCount();
                for(int i=0;i<count+1;i++){
                    if(position==i){
                        Intent intent = new Intent(getApplicationContext(),ListPermission.class);

                        Bundle bundle = new Bundle();

                        intent.putExtras(bundle);

                        intent.putExtra("size1",lista.get(position).critical);
                        intent.putExtra("packageName",lista.get(position).pname);
                        startActivity(intent);
                    }
                }


            }
        });





    }




    //sort application with malware persentage
    public void Sort(ArrayList<PInfo> lista){
        appovi = lista;

        Collections.sort(appovi, new Comparator<PInfo>() {
            public int compare(PInfo one, PInfo other) {

                Integer i = new Integer(other.critical.size());

                return i.compareTo(one.critical.size());
            }
        });
    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    public String getText(String text) {
        return "SEE VIRUS PERMISSION INFORMATION ";
    }


    //change color in progress bar
    public String getCol(Integer val){
        String col;
        if(val > 50){
            col = "#FFED0505";
        }else{
            col="#FF4BE30B";
        }
        return col;
    }



    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */


    /**
     * CUSTOM ADAPTER
     */

    //custom adapter class for pass the result to the front end
    public class CustomAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<PInfo> data;

        public CustomAdapter(Activity activity, ArrayList<PInfo> items) {
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
                convertView = inflater.inflate(R.layout.list_item, null);

            try {

                ImageView slika = (ImageView) convertView.findViewById(R.id.slika);
                TextView naslov = (TextView) convertView.findViewById(R.id.naslov);
                TextView critical = (TextView) convertView.findViewById(R.id.critical);

                ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar2);

                String pname = data.get(position).pname;
                Drawable icon;

                try {



                    icon = getPackageManager().getApplicationIcon(pname);

                    slika.setImageDrawable(icon);
                    naslov.setText(data.get(position).appname);


;
                    critical.setText(""+(data.get(position).critical.size()*100/14)+"%");

                    progressBar.setProgress(data.get(position).critical.size()*100/14);

                    if(data.get(position).critical.size()*100/14 > 50){
                        progressBar.getProgressDrawable().setColorFilter(
                                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                    }
                    else{
                        progressBar.getProgressDrawable().setColorFilter(
                                Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
                    }



                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ParseException e) {

                e.printStackTrace();

            }

            return convertView;

        }
    }



}
