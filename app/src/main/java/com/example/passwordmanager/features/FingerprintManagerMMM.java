package com.example.passwordmanager.features;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

public class FingerprintManagerMMM {
    public interface FingerprintAuthenticationCallback {
        void onAuthenticationSucceeded();
        void onAuthenticationFailed();
    }
    private Context context;

    public FingerprintManagerMMM(Context context) {
        this.context = context;
    }

    public void authenticateFingerprint(FingerprintAuthenticationCallback callback) {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Authentication")
                .setSubtitle("Place your finger on the sensor")
                .setNegativeButtonText("Cancel")
                .build();

        BiometricPrompt.AuthenticationCallback authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                callback.onAuthenticationSucceeded();
            }

            @Override
            public void onAuthenticationFailed() {
                callback.onAuthenticationFailed();
            }
        };

        BiometricPrompt biometricPrompt = new BiometricPrompt((AppCompatActivity) context,
                ContextCompat.getMainExecutor(context),
                authenticationCallback);

        biometricPrompt.authenticate(promptInfo);
    }
}
