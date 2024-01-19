package com.example.passwordmanager.CRUD;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.example.passwordmanager.activities.HomePage;
import com.example.passwordmanager.features.Site;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddPasswordActivity extends AppCompatActivity {
    private EditText siteName, siteUsername, sitePassword;
    private Button addButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        btnBack = findViewById(R.id.btnBack);
        addButton =findViewById(R.id.addButton);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        sitePassword = findViewById(R.id.passwordEditText);
        siteUsername = findViewById(R.id.usernameEditText);
        siteName = findViewById(R.id.siteNameEditText);

        btnBack.setOnClickListener(v -> {
            Intent goBackMain = new Intent(AddPasswordActivity.this, HomePage.class);
            startActivity(goBackMain);
            finish();
        });

        addButton.setOnClickListener(v ->{
            addSiteToFirestore();
        });
    }

    private void addSiteToFirestore(){
        String userId = auth.getCurrentUser().getUid();

        String siteNameText = siteName.getText().toString();
        String username = siteUsername.getText().toString();
        String password = sitePassword.getText().toString();

        DocumentReference userSitesRef = db.collection("users").document(userId);

        userSitesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    addDocumentToSitesCollection(userSitesRef, siteNameText, username, password);
                } else {
                    createSitesCollectionAndAddDocument(userId, userSitesRef, siteNameText, username, password);
                }
            } else {
                Toast.makeText(this, "Password couldn't be added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDocumentToSitesCollection(DocumentReference userSitesRef, String siteName, String username, String password) {
        DocumentReference siteRef = userSitesRef.collection("sites").document();

        Site siteData = new Site(siteName, password, username);

        siteRef.set(siteData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Password couldn't be added", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createSitesCollectionAndAddDocument(String userId, DocumentReference userSitesRef, String siteName, String username, String password) {
        userSitesRef.collection("sites").document().set(new Site())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // "sites" collection created successfully, now add the document
                        addDocumentToSitesCollection(userSitesRef, siteName, username, password);
                    } else {
                        Toast.makeText(this, "Password couldn't be added", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}