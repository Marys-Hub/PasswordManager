package com.example.passwordmanager;

import java.util.Random;

public class VerificationCodeUtil {

    public static String generateRandomCode(int length) {
        // Define the characters allowed in the verification code
        String allowedChars = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM!@#$%^&*()";

        // Create a StringBuilder to store the code
        StringBuilder codeBuilder = new StringBuilder(length);

        // Use a random number generator to pick characters from the allowed set
        Random random = new Random();
        for (int i = 0; i < 11; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            codeBuilder.append(randomChar);
        }

        // Return the generated code as a string
        return codeBuilder.toString();
    }
}
