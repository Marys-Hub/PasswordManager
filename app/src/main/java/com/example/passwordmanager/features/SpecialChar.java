package com.example.passwordmanager.features;

import java.util.Random;

public class SpecialChar extends PasswordGenerator {
    private final char[] spCharArray = "?./!%*$^+-)]@(['{}#<>".toCharArray();

    @Override
    public String Char() {
        Random r = new Random();
        return String.valueOf(r.nextInt(spCharArray.length));
    }
}

