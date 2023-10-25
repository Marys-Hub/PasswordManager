package com.example.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;


import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.passwordmanager.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //new

        buttonLogin = (Button) findViewById(R.id.buttonLogin); //new
        buttonRegister =(Button) findViewById(R.id.buttonRegister);//new


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }

        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openRegisterActivity();}
        });

    }

    public void openLoginActivity(){
        // Start the LoginActivity when the "LOG IN" button is clicked
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);

    }

    public void openRegisterActivity(){
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        try{startActivity(registerIntent);}catch(Exception e){System.out.println("Something went wrong.");} }

    public void funk(){}

}