package com.example.spm2.Utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.spm2.UI.NavigationDrawer;


public class MyBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(intent.getExtras().getInt("id"));
        Toast.makeText(context, "Opening SPM...", Toast.LENGTH_SHORT).show();
        Intent startActivityIntent=new Intent(context, NavigationDrawer.class);
        context.startActivity(startActivityIntent);

    }
}
