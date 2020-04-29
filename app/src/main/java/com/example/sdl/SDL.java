package com.example.sdl;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.google.firebase.database.FirebaseDatabase;

public class SDL extends Application {
    public static final String CHANNEL_1_ID = "MONEY_MATTERS";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

//        createNotificationChannels();

    }
    private void createNotificationChannels()
    {
        NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1_ID,
                "CHANNEL1",
                NotificationManager.IMPORTANCE_DEFAULT
        );
//
//        channel1.setDescription("This is channel");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }
}
