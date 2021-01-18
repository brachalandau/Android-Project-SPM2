package com.example.spm2.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.spm2.R;
import com.example.spm2.UI.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParcelService extends Service {
    String TAG="mySer";
    static DatabaseReference parcelsRef;
    FirebaseDatabase database=FirebaseDatabase.getInstance();//gets access

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();


        context = this;
        notificationManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this, "M_CH_ID");


        remoteViews = new RemoteViews(getPackageName(), R.layout.activity_send_notification);
        remoteViews.setImageViewResource(R.id.notif_icon,R.drawable.spmlogo);


        parcelsRef= database.getReference("parcels");
        parcelsRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                notification_id = (int) System.currentTimeMillis();

                Intent button_intent = new Intent("button_click");
                button_intent.putExtra("id",notification_id);
                PendingIntent button_pending_event = PendingIntent.getBroadcast(context,notification_id,
                        button_intent,0);

                remoteViews.setOnClickPendingIntent(R.id.button,button_pending_event);

                Intent notification_intent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notification_intent,0);

                builder.setSmallIcon(R.drawable.spmlogo)
                        .setAutoCancel(true)
                        .setContentTitle("A New Parcel Has Arrived")
                        .setCustomBigContentView(remoteViews)
                        .setVibrate(new long[]{ 0, 100, 200, 300 })
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);
                builder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis());
                notificationManager.notify(notification_id,builder.build());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    //@Override
    //public int onStartCommand(Intent intent, int flags, int startId) {
    //    return Service.START_REDELIVER_INTENT;
    //}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }





}



