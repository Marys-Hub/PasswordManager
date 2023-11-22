package com.example.passwordmanager.features;

import java.util.Random;

public class LowerCase extends PasswordGenerator {
    @Override
    public String Char() {
        Random r = new Random();
        int nr = r.nextInt(26) + 'a';
        return Character.toString((char) nr);
    }
}
