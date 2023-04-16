package com.example.chatit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Settings extends AppCompatActivity {

    private Context mContext;
    private DatabaseReference userRef;
    private EditText edtValue;
    private Button btnUpdate;
    private TextView txtValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        userRef = FirebaseDatabase.getInstance().getReference().child("Items");
        edtValue=findViewById(R.id.edtValueTest);
        btnUpdate=findViewById(R.id.btnPrice);
        txtValue=findViewById(R.id.txtDisplay);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("FireBase Notification","FireBase Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtValue.getText().toString().equals(""))
                {
                    Toast.makeText(Settings.this, "Enter Something",
                            Toast.LENGTH_SHORT).show();
                }
                else {
//                    userRef.setValue(edtValue.getText().toString());
                    userRef.child("Rice").setValue(edtValue.getText().toString());
                }
            }

        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
//                    txtValue.setText(snapshot.getValue().toString());
//                    pushNotification("Data Update","Updated data: "+snapshot.getValue().toString());
                    txtValue.setText(snapshot.child("Rice").getValue().toString());
                    pushNotification("Data Update","Updated data: "+snapshot.child("Rice").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pushNotification(String title, String body){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Settings.this,"FireBase Notification");
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(body);
        mBuilder.setSmallIcon(R.drawable.applogo);
        mBuilder.setAutoCancel(true);

        NotificationManagerCompat managerCompact = NotificationManagerCompat.from(Settings.this);
        managerCompact.notify(0,mBuilder.build());
    }
}