package com.example.passwordmanager.CRUD;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passwordmanager.R;
import com.example.passwordmanager.activities.HomePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DeletePassword extends AppCompatActivity {
    private Button delButton;
    private EditText siteName;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_password);

        delButton = findViewById(R.id.deleteButton);
        siteName = findViewById(R.id.siteNameText);
        auth = FirebaseAuth.getInstance();
        btnBack = findViewById(R.id.btnBack);
        db = FirebaseFirestore.getInstance();


        delButton.setOnClickListener(v->{
            deleteSiteFirestore();
        });

        btnBack.setOnClickListener(v -> {
            Intent goBackMain = new Intent(DeletePassword.this, HomePage.class);
            startActivity(goBackMain);
            finish();
        });
    }

    private void deleteSiteFirestore() {
        String userId = auth.getCurrentUser().getUid();

        String siteNameText = siteName.getText().toString();
        db.collection("users").document(userId).collection("sites").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String site = doc.getString("name");
                            if (site.equals(siteNameText)) {
                                doc.getReference().delete();
                                Toast.makeText(this, "Site deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.e("Firestore", "Error gettind the data: ", task.getException());
                    }
                });
    }

}