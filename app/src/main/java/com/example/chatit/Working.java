package com.example.chatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class Working extends AppCompatActivity {

    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabAdapter mTabAdapter;
    private TextView profileName;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working);
        mToolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(mToolbar);
        mAuth = FirebaseAuth.getInstance();
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        profileName = findViewById(R.id.profileText);
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");

        userRef.child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        profileName.setText(snapshot.child("Name").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout){
            mAuth.signOut();
            Intent intent = new Intent(Working.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (item.getItemId()==R.id.settings)
        {
            Intent intent = new Intent(Working.this, Settings.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.anything)
        {
            Intent intent = new Intent(Working.this, Anything.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}