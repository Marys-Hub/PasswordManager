package com.example.passwordmanager;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import com.example.passwordmanager.R;

import com.example.passwordmanager.activities.LoginActivity;
import com.example.passwordmanager.activities.RegisterActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class RegistrationActivityTest {



        RegisterActivity registrationActivity;

        @Mock
        private FirebaseAuth mockFirebaseAuth;

        @Mock
        FirebaseUser mockFirebaseUser;

        @Before
        public void setUp() {
           /* MockitoAnnotations.initMocks(this);
            when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
            registrationActivity = Robolectric.buildActivity(RegisterActivity.class).create().get();
            registrationActivity.firebaseAuth = mockFirebaseAuth;*/
        }

        // BVA Tests for weak pass, minimum length pass, max len pass, strong pass

        @Test
        public void testWeakPassword() {
            // Password with less than 8 characters
            String name = "John";
            String email = "john@example.com";
            String weakPassword = "weak"; // Less than 8 characters
            //assertFalse(registrationActivity.isValidPassword(weakPassword));
        }

        @Test
        public void testMinimumLengthPassword() {
            // Password with minimum length (8 characters)
            String name = "John";
            String email = "john@example.com";
            String minPassword = "min12345"; // Exactly 8 characters
            //assertTrue(registrationActivity.isValidPassword(minPassword));
        }

        @Test
        public void testMaximumLengthPassword() {
            // Password with maximum length (50 characters)
            String name = "John";
            String email = "anamaria@icloud.com";
            String maxPassword = "maximumPassword1234567890maximumPassword1234567890"; // 50 characters
            //assertTrue(registrationActivity.registerForActivityResult(maxPassword));
        }

        @Test
        public void testStrongPassword() {
            // Password with all types of characters, meeting the requirements
            String name = "John";
            String email = "popescu@gmail.com";
            String strongPassword = "Strong@Password123"; // Contains uppercase, lowercase, digit, and special character
            //assertTrue(registrationActivity.isValidPassword(strongPassword));
        }

        // ECP tests for valid registration, invalid registration, invalid email format

        @Test
        public void testValidRegistration() {
            // Arrange
            String email = "test@example.com";
            String password = "password123";
            EditText emailEditText = registrationActivity.findViewById(R.id.etEmail);
            EditText passwordEditText = registrationActivity.findViewById(R.id.etPassword);
            emailEditText.setText(email);
            passwordEditText.setText(password);
            Button registerButton = registrationActivity.findViewById(R.id.buttonRegister);
            when(mockFirebaseAuth.createUserWithEmailAndPassword(email, password))
                    .thenReturn(Tasks.forResult(mock(AuthResult.class)));

            // Act
            registerButton.performClick();

            // Assert
            verify(mockFirebaseAuth).createUserWithEmailAndPassword(email, password);
            Intent expectedIntent = new Intent(registrationActivity, LoginActivity.class);
            Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
            assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        }

        @Test
        public void testInvalidEmailFormat() {
            // Arrange
            String invalidEmail = "invalidemail";
            String password = "password123";
            EditText emailEditText = registrationActivity.findViewById(R.id.etEmail);
            EditText passwordEditText = registrationActivity.findViewById(R.id.etPassword);
            emailEditText.setText(invalidEmail);
            passwordEditText.setText(password);
            Button registerButton = registrationActivity.findViewById(R.id.buttonRegister);

            // Act
            registerButton.performClick();

            // Assert
            verify(mockFirebaseAuth).createUserWithEmailAndPassword(invalidEmail, password);
            assertEquals("Invalid email format", emailEditText.getError());
        }
}

