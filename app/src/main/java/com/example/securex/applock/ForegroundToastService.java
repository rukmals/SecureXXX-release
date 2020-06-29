package com.example.securex.applock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.securex.BuildConfig;
import com.example.securex.R;
import com.example.securex.applock2.ReceiverApplock;
import com.rvalerio.fgchecker.AppChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForegroundToastService extends Service {

    private final static int NOTIFICATION_ID = 1234;
    private final static String STOP_SERVICE = ForegroundToastService.class.getPackage()+".stop";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private BroadcastReceiver stopServiceReceiver;
    private AppChecker appChecker;
    private boolean unlocked;
    Intent dialogIntent;
    String current_app;
    String prev_app;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    List<String> locked_apps;
    LocalBroadcastManager localBroadcastManager;
    Context context;



    public static void start(Context context) {
        context.startService(new Intent(context, ForegroundToastService.class));
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, ForegroundToastService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dialogIntent = new Intent(getApplicationContext(),UnlockActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        registerReceivers();
        startChecker();
        createNotificationChannel();
        createStickyNotification();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        context =this;

        current_app="";
        prev_app="1";
        unlocked=false;
        locked_apps = new ArrayList<>();
        pref = getApplicationContext().getSharedPreferences("com.android.app.users",getApplicationContext().MODE_PRIVATE);
        editor=pref.edit();
        Map<String, ?> prefsMap = pref.getAll();
        for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
            Log.v("SharedPreferences", entry.getKey() + ":" +
                    entry.getValue().toString());
            if(entry.getValue().toString().equals("locked")){
                locked_apps.add(entry.getKey());

            }
        }
        for (String packgae:locked_apps){
            Log.d("names",packgae);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopChecker();
        removeNotification();
        unregisterReceivers();
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this,PermissionActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = createStickyNotification();
        startForeground(1,notification);

        return START_STICKY;
    }

    private void startChecker() {
        appChecker = new AppChecker();
        appChecker
                .when(getPackageName(), new AppChecker.Listener() {
                    @Override
                    public void onForeground(String packageName) {

//                        Toast.makeText(getBaseContext(), "Our app is in the foreground.", Toast.LENGTH_SHORT).show();
                    }
                })
                .whenOther(new AppChecker.Listener() {
                    @Override
                    public void onForeground(final String packageName) {

                        if(!current_app.equals(packageName)){
                            current_app=packageName;
                        String app= pref.getString("current_app","");
                        Intent intent1 = new Intent(context, ReceiverApplock.class);
                        sendBroadcast(intent1);}


//                        if(locked_apps.contains(packageName) && !app.equals(packageName)){
//                            Log.d("APP","CURRENT");
//                            editor.putString("current_app",packageName);
//                            editor.commit();
//                            current_app=packageName;
//                            Intent intent = new Intent(context,UnlockActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtra("current_app",packageName);
//                            context.startActivity(intent);
//                            Log.d("SHASHIMAL","SHASHIMAL");





//
//                     }
                        if(!packageName.equals(current_app)){
                            current_app="";
                        }
                    }
                })
                .timeout(210)
                .start(this);
    }

    private void stopChecker() {
        appChecker.stop();
    }

    private void registerReceivers() {
        stopServiceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                stopSelf();
            }
        };
        registerReceiver(stopServiceReceiver, new IntentFilter(STOP_SERVICE));
    }

    private void unregisterReceivers() {
        unregisterReceiver(stopServiceReceiver);
    }

    private Notification createStickyNotification() {
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("SecureX")
                .build();
        return notification;
    }

    private void removeNotification() {
        NotificationManager manager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        manager.cancel(NOTIFICATION_ID);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }



}
