package com.example.securex.applock2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.securex.applock.UnlockActivity;

public class ReceiverApplock extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils utils = new Utils(context);
        String appRunning = utils.getLauncherTopApp();

        if(utils.isLock(appRunning)){

            if(!appRunning.equals(utils.getLastApp())){

                utils.clearLastApp();
                utils.setLastApp(appRunning);
                Log.d("LAST_APP3",utils.getLastApp());


                Intent intent1 = new Intent(context, UnlockActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent1.putExtra("broadcast_receiver","broadcast_receiver");

                Log.d("APP","BEFORE START");
                context.startActivity(intent1);
            }
        }
    }
}