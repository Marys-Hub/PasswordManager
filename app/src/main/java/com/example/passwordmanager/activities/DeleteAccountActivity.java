package com.example.passwordmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteAccountActivity extends AppCompatActivity {

    Button btnBack;
    Button btnConfirm;
    ProgressBar bar;
    TextView userEmail;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        btnBack = findViewById(R.id.btnBack);
        btnConfirm = findViewById(R.id.btnConfirm);
        userEmail = findViewById(R.id.edtEmail);

        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(DeleteAccountActivity.this, SettingsActivity.class);
                startActivity(goBack);
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserAndData();

            }
        });
    }

    private void deleteUserAndData() {
        // Ensure the user has been authenticated
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // Delete the user from Firebase Authentication
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showToast("Delete successful");
                            // User account deleted successfully
                            // Redirect to login or registration screen
                            Intent intent = new Intent(DeleteAccountActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Handle deletion failure from Firebase Authentication
                            showToast("Authentication deletion failed");
                        }
                    });
        }
    }
    private void showToast(String message) {
        Toast.makeText(DeleteAccountActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}