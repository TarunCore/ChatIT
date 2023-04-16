package com.example.chatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText edtUserName,edtEmail,edtPassword;
    private TextView txtAlreadyHave;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUserName=findViewById(R.id.edtUsername);
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassword);
        txtAlreadyHave=findViewById(R.id.txtHave);
        btnRegister=findViewById(R.id.btnRegister);
        mAuth=FirebaseAuth.getInstance();
        ProgressDialog pg= new ProgressDialog(MainActivity.this);
        pg.setTitle("Authentication");
        pg.setMessage("Dude plz wait till u are verified");
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");

        txtAlreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUserName.getText().toString().equals("")||edtEmail.getText().toString().equals("")||
                edtPassword.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this, "All fields are needed da venna", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pg.show();
                    mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        userRef.child(mAuth.getCurrentUser().getUid()).child("Name")
                                                .setValue(edtUserName.getText().toString());
                                        Toast.makeText(MainActivity.this, edtUserName.getText().toString()+"Successfully logged in", Toast.LENGTH_SHORT).show();
                                        pg.dismiss();
                                        Intent intent = new Intent(MainActivity.this, Working.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this,"Error"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        pg.dismiss();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(MainActivity.this, Working.class);
            startActivity(intent);
            finish();
        }
    }
}