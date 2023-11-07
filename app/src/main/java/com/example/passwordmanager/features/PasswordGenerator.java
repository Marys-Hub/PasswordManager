package com.example.passwordmanager.features;

import java.util.ArrayList;

public abstract class PasswordGenerator {
    private static ArrayList<PasswordGenerator> generators;

    public static void clear(){
        if(generators!=null) generators.clear();
        else generators = new ArrayList<>();
    }

    public static void add(PasswordGenerator pwdg){
        generators.add(pwdg);
    }

    public static boolean isEmpty(){
        return generators.isEmpty();
    }

    private static PasswordGenerator getPassGen(){ // isEmpty()
        if(generators.size()==1) return generators.get(0);
        int randIdx = (int) (Math.random() * generators.size());
        return generators.get(randIdx);
    }

    public static String genPass(int sizePass){ // size == 0
        StringBuilder password = new StringBuilder();
        while(sizePass!=0){
            password.append(getPassGen().Char());
            sizePass--;
        }
        return password.toString();
    }
    public abstract String Char();
}

