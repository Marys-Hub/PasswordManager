package com.example.passwordmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.passwordmanager.features.DarkModePrefManager;
import com.example.passwordmanager.R;

public class SettingsActivity extends AppCompatActivity {

    Button btnBack;
    Button btnLogout;
    private Switch darkModeSwitch;

    Button btnDeteleAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(new DarkModePrefManager(this).isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        setContentView(R.layout.activity_settings);
        setDarkModeSwitch();


        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackMain = new Intent(SettingsActivity.this, HomePage.class);
                startActivity(goBackMain);
                finish();
            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

       Button  btnChangePassword = findViewById(R.id.btnChangePass);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToChangePass = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(goToChangePass);
                finish();
            }

        });

        btnDeteleAccount = findViewById(R.id.btnDelete);

        btnDeteleAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToConfirm = new Intent(SettingsActivity.this, DeleteAccountActivity.class);
                startActivity(goToConfirm);
                finish();
            }
        });
    }


    private void setDarkModeSwitch(){
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setChecked(new DarkModePrefManager(this).isNightMode());
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DarkModePrefManager darkModePrefManager = new DarkModePrefManager(SettingsActivity.this);
                darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
            }
        });
    }

    private void logout() {
        // Perform any necessary logout actions (e.g., clear session, user data, etc.)
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
        Intent goBackMain = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(goBackMain);

        // Finish the current activity to prevent going back to it using the back button
        finish();
    }

}