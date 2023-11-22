package com.example.passwordmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.passwordmanager.R;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

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
    }
}