package com.example.passwordmanager.features;

import java.util.Random;

public class UpperCase extends PasswordGenerator {
    @Override
    public String Char() {
        Random r = new Random();
        int nr = r.nextInt(26) + 'A';
        return Character.toString((char) nr);
    }
}
