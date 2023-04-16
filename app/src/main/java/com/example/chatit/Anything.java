package com.example.chatit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Anything extends AppCompatActivity {

    private Button notifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anything);
        notifyButton=findViewById(R.id.btnNotifying);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Anything.this,"My Notification");
                    mBuilder.setContentTitle("Data");
                    mBuilder.setContentText("This is the data");
                    mBuilder.setSmallIcon(R.drawable.logopurple);


                NotificationManagerCompat managerCompact = NotificationManagerCompat.from(Anything.this);
                managerCompact.notify(0,mBuilder.build());
            }
        });

    }
}