package com.example.passwordmanager.features;

import java.util.Random;

public class Numeric extends PasswordGenerator {
    @Override
    public String Char() {
        Random r = new Random();
        return String.valueOf(r.nextInt(9));
    }
}
