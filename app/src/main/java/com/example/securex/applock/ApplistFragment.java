package com.example.securex.applock;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.securex.R;
import com.example.securex.databinding.FragmentApplistBinding;
import com.gun0912.tedpermission.TedPermission;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplistFragment extends Fragment {

    FragmentApplistBinding binding;
    RecyclerView recyclerView;
    PackageManager packageManager;
    List<PackageInfo> applicationInfos;

    public ApplistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentApplistBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String s[] = getResources().getStringArray(R.array.array);
        recyclerView = binding.recycleview;
        applicationInfos=getActivity().getPackageManager().getInstalledPackages(0);
        List<Application> apps = new ArrayList<>();



        if(hasUsageStatsPermission(getContext())){
            binding.permission.setVisibility(View.GONE);
        }

        if(!hasUsageStatsPermission(getContext())){
            blur();
        }



        for (int i=0;i<applicationInfos.size();i++){
            PackageInfo packageInfo = applicationInfos.get(i);
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0){
                Application application = new Application(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(),packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()),packageInfo.applicationInfo.packageName);
                apps.add(application);
                String appName = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                String packageName = packageInfo.applicationInfo.packageName;
            }
        }

        binding.permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PermissionActivity.class));
            }
        });



        AppListAdapter appListAdapter = new AppListAdapter(getContext(),apps);
        recyclerView.setAdapter(appListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }

    public void blur(){
        float radius = 5f;

        View decorView = getActivity().getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        binding.blurview.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(getContext()))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);
    }


}
