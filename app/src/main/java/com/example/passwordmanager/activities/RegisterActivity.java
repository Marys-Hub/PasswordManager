package com.example.passwordmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private TextView tvNameError, tvEmailError, tvPasswordError, tvColor;
    private CardView frameOne, frameTwo, frameThree, frameFour;
    private CardView btnRegister;
    private boolean isAtLeast8 = false, hasUppercase = false, hasNumber = false, hasSymbol = false, isRegistrationClickable = false;
    FirebaseAuth firebaseAuth;

    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        tvColor = findViewById(R.id.tvColor);
        tvNameError = findViewById(R.id.tvNameError);
        tvEmailError = findViewById(R.id.tvEmailError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        frameOne = findViewById(R.id.frameOne);
        frameTwo = findViewById(R.id.frameTwo);
        frameThree = findViewById(R.id.frameThree);
        frameFour = findViewById(R.id.frameFour);
        btnRegister = findViewById(R.id.btnRegister);
        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString(), email = etEmail.getText().toString(), password = etPassword.getText().toString();

                if (name.length() > 0 && email.length() > 0 && password.length() > 0) {
                    if (isRegistrationClickable) {
                        //Toast.makeText(RegisterActivity.this, "Registration successfull", Toast.LENGTH_LONG).show();
                        //e un delay de la firebase si imi arata succesfull inainte sa verifice daca exista asa ca l-am eliminat
                        signUpUser(name, email, password);
                    }
                } else {
                    if (name.length() == 0) {
                        tvNameError.setVisibility(View.VISIBLE);
                    }
                    if (email.length() == 0) {
                        tvEmailError.setVisibility(View.VISIBLE);
                    }
                    if (password.length() == 0) {
                        tvPasswordError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        inputChange();


       btnBack = findViewById(R.id.btnRegisterBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackMain = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(goBackMain);
                finish();
            }
        });




    }

    private void checkEmpty(String name, String email, String password) {
        if (name.length() > 0 && tvNameError.getVisibility() == View.VISIBLE) {
            tvNameError.setVisibility(View.GONE);
        }
        if (password.length() > 0 && tvPasswordError.getVisibility() == View.VISIBLE) {
            tvPasswordError.setVisibility(View.GONE);
        }
        if (email.length() > 0 && tvEmailError.getVisibility() == View.VISIBLE) {
            tvEmailError.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ResourceType")
    private void checkAllData(String email) {
        if (isAtLeast8 && hasUppercase && hasNumber && hasSymbol && email.length() > 0) {
            isRegistrationClickable = true;
            tvColor.setTextColor(Color.WHITE);
            btnRegister.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            isRegistrationClickable = false;
            btnRegister.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
    }

    @SuppressLint("ResourceType")
    private void registrationDataCheck() {
        String password = etPassword.getText().toString(), email = etEmail.getText().toString(), name = etName.getText().toString();

        // Email validation regular expression
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        checkEmpty(name, email, password);

        if (password.length() >= 8) {
            isAtLeast8 = true;
            frameOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            isAtLeast8 = false;
            frameOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
        if (password.matches("(.*[A-Z].*)")) {
            hasUppercase = true;
            frameTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasUppercase = false;
            frameTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
        if (password.matches("(.*[0-9].*)")) {
            hasNumber = true;
            frameThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasNumber = false;
            frameThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
        if (password.matches("^(?=.*[_.%!@#$^&*()]).*$")) {
            hasSymbol = true;
            frameFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasSymbol = false;
            frameFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        // Email validation check
        if (email.matches(emailPattern)) {
            tvEmailError.setVisibility(View.GONE);
        } else {
            tvEmailError.setVisibility(View.VISIBLE);
        }

        checkAllData(email);
    }

    private void inputChange() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void signUpUser(String name, String email, String password) {
        // Show a progress dialog to indicate the registration process
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        // Use Firebase Authentication to create a user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Dismiss the progress dialog regardless of the result
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        // Registration successful, navigate to the login page
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        if (currentUser != null) {
                            // Set the user's display name
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            currentUser.updateProfile(profileUpdates);
                        }

                        // You can navigate to the login page using an Intent
                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(loginIntent);

                        // Finish the current activity so the user can't go back to it
                        finish();
                    } else {
                        // Registration failed, handle the error
                        if (task.getException() != null) {
                            String errorMessage = task.getException().getMessage();

                            // Check if the email is already in use
                            if (errorMessage != null && errorMessage.contains("email address is already in use")) {
                                Toast.makeText(RegisterActivity.this, "Email already in use. Please use a different email.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }



}