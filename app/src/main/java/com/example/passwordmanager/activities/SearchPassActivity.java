package com.example.passwordmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.example.passwordmanager.features.BiometricCipherHelper;
import com.example.passwordmanager.features.Site;
import com.example.passwordmanager.features.SiteAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class SearchPassActivity extends AppCompatActivity {
    private List<Site> sites;
    private RecyclerView recyclerView;
    private SiteAdapter siteAdapter;
    private SearchView searchView;
    private FirebaseFirestore db;
    private String pass;
    private FingerprintManagerCompat fingerprintManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pass);

        initView();
    }

    private void filterList(String text){
        List<Site> filteredList = new ArrayList<>();
        for(Site site : sites){
            if(site.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(site);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "No such site found", Toast.LENGTH_SHORT).show();
        } else {
            siteAdapter.setFilteredList(filteredList);
        }
    }

    private void addDataFromFirestone() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("users").document(userId).collection("sites").get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String siteName = doc.getString("name");
                                String password = doc.getString("password");
                                String username = doc.getString("username");

                                sites.add(new Site(siteName, password, username));
                            }
                        } else {
                            Log.e("Firestore", "Error gettind the data: ", task.getException());
                        }
                    });
        }
    }
    private void initView(){
        db = FirebaseFirestore.getInstance();
        sites = new ArrayList<>();
        addDataFromFirestone();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        fingerprintManager = FingerprintManagerCompat.from(this);
        siteAdapter = new SiteAdapter(sites, fingerprintManager, site ->{
            Toast.makeText(this, "Use Fingerprint to copy the password", Toast.LENGTH_LONG).show();
            pass = site.getPassword();
            createBiometricPrompt();
        });
        recyclerView.setAdapter(siteAdapter);
        }


    private void createBiometricPrompt() throws InvalidAlgorithmParameterException {
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.USE_FINGERPRINT}, 0);
            return;
        }

        if (fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
            SecretKey secretKey = BiometricCipherHelper.generateSecretKey();
            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(Objects.requireNonNull(BiometricCipherHelper.createCipher(Cipher.ENCRYPT_MODE, secretKey)));
            CancellationSignal cancellationSignal = new CancellationSignal();

            FingerprintManager.AuthenticationCallback authenticationCallback = new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                    showPassword(pass);
                }

                @Override
                public void onAuthenticationFailed() {
                    Toast.makeText(SearchPassActivity.this, "Fingerprint authentication failed", Toast.LENGTH_SHORT).show();
                }
            };
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, authenticationCallback, null);
        } else {
            Toast.makeText(this, "Fingerprint not available or no fingerprints enrolled", Toast.LENGTH_SHORT).show();
        }
    }
        private void showPassword(String pass){
            ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            manager.setPrimaryClip(ClipData.newPlainText("password",pass));
            Toast.makeText(this, "Password Copied", Toast.LENGTH_SHORT).show();
        }
}