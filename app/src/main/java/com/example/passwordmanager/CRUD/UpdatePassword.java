package com.example.passwordmanager.CRUD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.example.passwordmanager.activities.HomePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class UpdatePassword extends AppCompatActivity {
    private Button updateButton;
    private EditText siteName, username, password;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        updateButton = findViewById(R.id.updateButton);
        auth = FirebaseAuth.getInstance();
        btnBack = findViewById(R.id.btnBack);
        db = FirebaseFirestore.getInstance();
        siteName = findViewById(R.id.siteNameEditText1);
        username = findViewById(R.id.usernameEditText1);
        password = findViewById(R.id.passwordEditText1);

        updateButton.setOnClickListener(v->{
            updateSiteFirestore();
        });

        btnBack.setOnClickListener(v -> {
            Intent goBackMain = new Intent(UpdatePassword.this, HomePage.class);
            startActivity(goBackMain);
            finish();
        });
    }

    private void updateSiteFirestore() {
        String userId = auth.getCurrentUser().getUid();

        String siteNameText = siteName.getText().toString();
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();

        db.collection("users").document(userId).collection("sites").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String site = doc.getString("name");
                            if (site.equals(siteNameText)) {
                                if(!usernameText.isEmpty()){
                                    Map<String, Object> updateUsername = new HashMap<>();
                                    updateUsername.put("username", usernameText);
                                    doc.getReference().set(updateUsername, SetOptions.merge());
                                }
                                if(!passwordText.isEmpty()){
                                    Map<String, Object> updatePassword = new HashMap<>();
                                    updatePassword.put("password", passwordText);
                                    doc.getReference().set(updatePassword, SetOptions.merge());
                                }
                                Toast.makeText(this, "Site updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.e("Firestore", "Error gettind the data: ", task.getException());
                    }
                });
    }


}