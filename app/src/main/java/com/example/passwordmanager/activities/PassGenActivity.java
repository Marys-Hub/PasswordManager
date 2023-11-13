package com.example.passwordmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.example.passwordmanager.features.PasswordGenerator;
import com.example.passwordmanager.features.LowerCase;
import com.example.passwordmanager.features.Numeric;
import com.example.passwordmanager.features.LowerCase;
import com.example.passwordmanager.features.SpecialChar;
import com.example.passwordmanager.features.UpperCase;

public class PassGenActivity extends AppCompatActivity {
    private EditText editPasswordSize;
    private TextView textPasswordGenerated,textErrorMessage;
    private CheckBox checkLower, checkUpper,checkSpecialChar, checkNumeric;
    private Button btnGenerate, btnCopy;

    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_gen);

        initViews();
        clickListeners();
    }

    private void clickListeners() {
        btnGenerate.setOnClickListener(view -> {
            int passwordSize = Integer.parseInt(editPasswordSize.getText().toString());

            textErrorMessage.setText("");

            if(passwordSize<8){
                textErrorMessage.setText("Password Size must be greater than 8");
                return;
            }

            PasswordGenerator.clear();
            if(checkLower.isChecked()) PasswordGenerator.add(new LowerCase());
            if(checkNumeric.isChecked()) PasswordGenerator.add(new Numeric());
            if(checkUpper.isChecked()) PasswordGenerator.add(new UpperCase());
            if(checkSpecialChar.isChecked()) PasswordGenerator.add(new SpecialChar());


            if(PasswordGenerator.isEmpty()){
                textErrorMessage.setText("Please select at least one password content type");
                return;
            }

            String passwrd = PasswordGenerator.genPass(passwordSize);
            textPasswordGenerated.setText(passwrd);

        });

        btnCopy.setOnClickListener(view ->{
            ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            manager.setPrimaryClip(ClipData.newPlainText("password",textPasswordGenerated.getText().toString()));
            Toast.makeText(this, "Password Copied", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        editPasswordSize = findViewById(R.id.edit_pwd_size);
        textPasswordGenerated = findViewById(R.id.text_password_result);
        textErrorMessage = findViewById(R.id.text_error);
        checkLower = findViewById(R.id.check_lower);
        checkUpper = findViewById(R.id.check_upper);
        checkSpecialChar = findViewById(R.id.check_special_char);
        checkNumeric = findViewById(R.id.check_numeric);
        btnGenerate = findViewById(R.id.btn_generate);
        btnCopy = findViewById(R.id.btn_copy);
        btnBack = findViewById(R.id.btnGeneratePassBack);
    }
}