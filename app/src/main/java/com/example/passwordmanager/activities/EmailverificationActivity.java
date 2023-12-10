package com.example.passwordmanager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.passwordmanager.features.VerificationCodeUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class EmailverificationActivity extends AppCompatActivity {


    private EditText etVerificationCode;
    private Button btnVerify;
    private String generatedCode;
    private EditText emailAdress;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverification);

        btnVerify = findViewById(R.id.btnVerify);
        etVerificationCode = findViewById(R.id.etVerificationCode);
        emailAdress = findViewById(R.id.etEmailAddress);


        // Initialize Firebase Auth and Firestore
        /*verifAuth = FirebaseAuth.getInstance();
        verifUser = verifAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance(); */

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = emailAdress.getText().toString().trim();

                if (!TextUtils.isEmpty(enteredEmail)) {
                     //enteredEmai = emailAdressl;
                    sendRandomCodeToEmail(enteredEmail);
                } else {
                    Toast.makeText(EmailverificationActivity.this, "Enter email address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }





    // Function to generate a random code and store it in Firestore
    private void sendRandomCodeToEmail(String email) {
        // Generate a random code
        generatedCode = VerificationCodeUtil.generateRandomCode(6);

        // Save the code to Firestore for later verification
        Map<String, Object> codeData = new HashMap<>();
        codeData.put("code", generatedCode);

        db.collection("verificationCodes")
                .document(email)
                .set(codeData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Send the email to the user
                        sendVerificationEmail(email, generatedCode);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(EmailverificationActivity.this, "Failed to send code", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendVerificationEmail(String userEmail, String code) {
        // Customize the email subject and body
        String emailSubject = "Verify Your Email";
        String emailBody = "Your verification code is: " + code;

        // Use your preferred method to send emails, such as sending through an SMTP server or using a third-party service.
        // This example assumes you have a method sendEmail that sends an email.
        //sendEmail(userEmail, emailSubject, emailBody);
    }



    private void verifyCode(String enteredCode, String userEmail) {
        // Retrieve the stored code from Firestore
        db.collection("verificationCodes")
                .document(userEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String storedCode = documentSnapshot.getString("code");

                            if (enteredCode.equals(storedCode)) {
                                // The entered code is correct, proceed with registration or other actions
                                Toast.makeText(EmailverificationActivity.this, "Verification successful!",
                                        Toast.LENGTH_SHORT).show();

                                // Navigate to the registration page or perform other actions
                                navigateToRegistrationActivity();
                            } else {
                                // The entered code is incorrect
                                Toast.makeText(EmailverificationActivity.this, "Incorrect verification code",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle error
                        Toast.makeText(EmailverificationActivity.this, "Failed to verify code",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToRegistrationActivity() {
        // Replace "RegistrationActivity.class" with your actual registration activity class
        // For example: new Intent(EmailverificationActivity.this, YourRegistrationActivity.class);
        Intent intent = new Intent(EmailverificationActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish(); // Finish this activity to prevent going back to it
    }




}

