package com.example.passwordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {


    Button buttonSignInRegister;
    TextInputEditText editTextEmail, editTextPassword;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.emailTextInputLayout);
        editTextPassword = findViewById(R.id.passwordTextInputLayout);

        buttonSignInRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(email)){// lightweight pop-up message or notification that briefly appears on the user's screen
                    Toast.makeText(RegisterActivity.this, "Enter email: ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Enter password: ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            private void loginUser(String email, String password){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "SignUp successful", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(RegisterActivity.this, HomePage.class);
                            startActivity(homeIntent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "SignUp failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });

    }




}