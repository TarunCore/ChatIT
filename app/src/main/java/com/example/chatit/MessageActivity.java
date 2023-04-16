package com.example.chatit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private TextView txtrecieverName, txtLastSeen;
    private String recieverName, recieverId;
    private DatabaseReference userRef, chatRef;
    private FirebaseAuth mAuth;
    private EditText edtMessage;
    private ImageButton btnSend;
    private RecyclerView mRecyleView;
    private MessageAdapter mMessageAdpater;
    public static ArrayList<String> messages,messagePosition,messageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mToolBar=findViewById(R.id.toolBarMessage);
        setSupportActionBar(mToolBar);

        getWindow().setBackgroundDrawableResource(R.drawable.bgmessage);

        txtrecieverName=findViewById(R.id.txtRecieverName);
        Intent intent=getIntent();
        recieverId = intent.getStringExtra("RecieverId");
        recieverName = intent.getStringExtra("RecieverName");
        txtrecieverName.setText(recieverName);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        edtMessage=findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        messages = new ArrayList<>();
        messagePosition = new ArrayList<>();
        messageId = new ArrayList<>();
        mRecyleView = findViewById(R.id.recyclerView);
        mRecyleView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        mMessageAdpater = new MessageAdapter(MessageActivity.this,messages);
        mRecyleView.setAdapter(mMessageAdpater);
        chatRef = FirebaseDatabase.getInstance().getReference().child("Chats");

        getMessages();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMessage.getText().toString().equals(""))
                {
                    Toast.makeText(MessageActivity.this,"Enter something da venna", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("SenderId",mAuth.getCurrentUser().getUid());
                    map.put("Message",edtMessage.getText().toString());
                    map.put("RecieverId",recieverId);
                    chatRef.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            userRef.child(mAuth.getCurrentUser().getUid()).child("ChatLists").child(recieverId).setValue(ServerValue.TIMESTAMP);
                            userRef.child(recieverId).child("ChatLists").child(mAuth.getCurrentUser().getUid()).setValue(ServerValue.TIMESTAMP);
                            edtMessage.setText("");
                        }
                    });
                }
            }
        });


    }
    void getMessages()  //private void
    {
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    if(snapshot.child("RecieverId").getValue().toString().equals(mAuth.getCurrentUser().getUid())
                        && snapshot.child("SenderId").getValue().toString().equals(recieverId))
                    {
                        messages.add(snapshot.child("Message").getValue().toString());
                        messagePosition.add("0");
                        messageId.add(snapshot.getKey());
                        mMessageAdpater.notifyItemInserted(messages.size()-1);
                        mRecyleView.smoothScrollToPosition(messages.size()-1);
                    }
                    else if(snapshot.child("RecieverId").getValue().toString().equals(recieverId)
                            && snapshot.child("SenderId").getValue().toString().equals(mAuth.getCurrentUser().getUid()))
                    {
                        messages.add(snapshot.child("Message").getValue().toString());
                        messagePosition.add("1");
                        messageId.add(snapshot.getKey());
                        mMessageAdpater.notifyItemInserted(messages.size()-1);
                        mRecyleView.smoothScrollToPosition(messages.size()-1);
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)  //delete
            {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}