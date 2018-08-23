package com.iiita.messmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public static final String[] VALUES = new String[]{"AB", "BC", "CD", "AE"};
    private TextInputEditText tvUser;
    private TextInputEditText tvPassword;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        Button bnSubmit = findViewById(R.id.bnSubmit);
        bnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        SharedPreferences.Editor editor = pref.edit();
        tvUser = findViewById(R.id.input_roll);
        tvPassword = findViewById(R.id.input_password);
        if (tvUser.getText().toString().equalsIgnoreCase("lit2017018")) {
            if (tvPassword.getText().toString().equals("hello")) {
                editor.putBoolean("isSignedIn", true);
                editor.apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else
                Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(LoginActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
        }
    }

}
