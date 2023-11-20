package com.example.passwordmanager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailverificationActivity extends AppCompatActivity {


    private Button btnVerify;
    private EditText emailAdress;
    private String generatedVerificationCode;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser verifUser = mAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverification);
        btnVerify = findViewById(R.id.btnVerify);
        emailAdress = findViewById(R.id.etEmailAddress);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailAdress.getText().toString().trim();

                if (!email.isEmpty()) {
                    sendEmailVerification(email);
                } else {
                    Toast.makeText(EmailverificationActivity.this,
                            "Please enter your email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendEmailVerification(String email) {
        mAuth.createUserWithEmailAndPassword(email, "temporaryPassword")
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // User is created, now send email verification
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendVerificationEmail(user);
                        } else {
                            // If account creation fails, display a message to the user.
                            Toast.makeText(EmailverificationActivity.this,
                                    "Failed to create account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailverificationActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            // Navigate to the registration page or perform other actions
                        } else {
                            Toast.makeText(EmailverificationActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

