package com.example.passwordmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.passwordmanager.R;
import com.example.passwordmanager.features.Site;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchPassActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private static final String TAG = "Ex";
    private TextView parole;
    private Button button;
    private ListView listView;
    private SearchView searchView;
    private ArrayAdapter<Site> adapter;
    private List<Site> sites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pass);

        initView();
        button.setOnClickListener(v -> readData());
        listView.setAdapter(adapter);
        setupSearchView();
    }

    public void readData() {
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                StringBuilder data = new StringBuilder();
                QuerySnapshot querySnapshot = task.getResult();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (querySnapshot != null) {
                    List<DocumentSnapshot> docs = querySnapshot.getDocuments();
                    for (DocumentSnapshot doc : docs) {
                        assert user != null;
                        if(user.getUid().equals(doc.getId())) {
//                        String n = doc.getString("github");
                            Map<String, Object> mapWithSitesAndPass = doc.getData();
                            //    Object[] pass = mapWithSitesAndPass.values().toArray();
                            //    Object[] sitesObj = mapWithSitesAndPass.keySet().toArray();
                            //    sites.add(new Site((String) sitesObj[0], (String) pass[0]));
//                            data.append(doc.getData()); //{github="mmmmm"}
                        }
                    }
//                    parole.setText(data.toString());
                } else {
                    Log.w(TAG, "Eroare", task.getException());
                }
            }
        });
    }

    private void filter(String query){
        ArrayAdapter<String> adapter1 = (ArrayAdapter<String>) listView.getAdapter();
        adapter1.getFilter().filter(query);
    }

    private void setupSearchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void initView(){
        db = FirebaseFirestore.getInstance();
        parole = findViewById(R.id.dataTextView);
        button = findViewById(R.id.button);
        listView = findViewById(R.id.list_view);
        searchView = findViewById(R.id.search_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sites);

    }
}