package com.example.passwordmanager.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.passwordmanager.R;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    Button btnSettings;
    Button btnAddPass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        LinearLayout linearLayout1 = findViewById(R.id.passGen);
        linearLayout1.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, PassGenActivity.class);
            startActivity(intent);
        });

        LinearLayout linearLayout2 = findViewById(R.id.searchPass);
        linearLayout2.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, SearchPassActivity.class);
            startActivity(intent);
        });


        btnAddPass=findViewById(R.id.buttonAddPass);
        btnSettings=findViewById(R.id.buttonSettings);


        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettings = new Intent(HomePage.this, SettingsActivity.class);
                startActivity(goToSettings);
                finish();
            }
        });

        btnAddPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAdd = new Intent(HomePage.this, AddPasswordActivity.class);
                startActivity(goToAdd);
                finish();
            }
        });
    }

}