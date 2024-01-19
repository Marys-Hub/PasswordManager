package com.example.passwordmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.passwordmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RiskFactors extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView risk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_factors);
        risk = findViewById(R.id.risksTextView);

        db = FirebaseFirestore.getInstance();
        analyzePasswords();
    }

    private void analyzePasswords(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        risk.setText("");
        if (user != null) {
            String userId = user.getUid();
            db.collection("users").document(userId).collection("sites").get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String siteName = doc.getString("name");
                                String password = doc.getString("password");
                                String username = doc.getString("username");

                                String email = user.getEmail();

                                if(!checkPasswordStrengthCriteria1(password)){
                                    risk.append("\n• Password for the site " + siteName + " should be at least 8 characters " +
                                            "long and contain at least: one uppercase letter, one " +
                                            "lowercase letter, one digit, and one special character\n");
                                }
                                if(!checkPasswordStrengthCriteria2(password)) {
                                    risk.append("\n• Password for the site " + siteName + " should not have consecutive numbers or letters\n");
                                }
                                if(!checkPasswordStrengthCriteria3(password, username, email)) {
                                    risk.append("\n• Password for the site " + siteName + " should not contain personal information\n");
                                }
                                if(!checkPasswordStrengthCriteria4(password)) {
                                    risk.append("\n• Password for the site " + siteName + " should not contain common words\n");
                                }
                                if(!checkPasswordStrengthCriteria5(password)) {
                                    risk.append("\n• Password for the site " + siteName + " should not contain keyboard patterns\n");
                                }
                            }
                        } else {
                            Log.e("Firestore", "Error gettind the data: ", task.getException());
                        }
                    });
        }
    }

    private Boolean checkPasswordStrengthCriteria1(String password){
        // Check if the password contains: minimum 8 characters, at least one uppercase letter, one lowercase letter,
        // one digit, and one special character
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private Boolean checkPasswordStrengthCriteria2(String password) {
        // Check for consecutive numbers or consecutive letters
        return !password.matches(".*\\d{2,}.*") && !password.matches(".*[a-zA-Z]{2,}.*");
    }

    private Boolean checkPasswordStrengthCriteria3(String password, String username, String email){
        // Check for personal information e.g. username, email
        password = password.toLowerCase();
        username = username.toLowerCase();
        email = email.toLowerCase();
        return !password.contains(username) && !password.contains(email);
    }

    private Boolean checkPasswordStrengthCriteria4(String password) {
        // Check if the password contains common words e.g. 'password'
       String[] commonWords = {"password", "access", "letmein", "parola", "password123"};
        password = password.toLowerCase();
        for(String pattern: commonWords){
            if(password.contains(pattern)){
                return false;
            }
        }
        return true;
    }

    private Boolean checkPasswordStrengthCriteria5(String password) {
        // Check for keyboard patterns
        String[] keyboardPatterns = {"qwerty", "asdfgh", "zxcvbn", "poiuytrewq", "lkjhgfdsa", "mnbvcxz"};
        password = password.toLowerCase();
        for(String pattern: keyboardPatterns){
            if(password.contains(pattern)){
                return false;
            }
        }
        return true;
    }
}