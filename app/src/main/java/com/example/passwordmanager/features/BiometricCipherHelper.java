package com.example.passwordmanager.features;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BiometricCipherHelper {

    private static final String KEY_ALIAS = "key_alias";

    public static SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(true)
                    .setInvalidatedByBiometricEnrollment(true);

            keyGenerator.init(builder.build());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException |
                 InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SecretKey getSecretKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
            return secretKeyEntry.getSecretKey();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | IOException e) {
            e.printStackTrace();
            return null;
        } catch (UnrecoverableEntryException e) {
            throw new RuntimeException(e);
        }
    }

    public static Cipher createCipher(int mode, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(mode, secretKey);
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }
}
