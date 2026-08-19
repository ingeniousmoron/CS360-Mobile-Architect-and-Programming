package com.zybooks.cs360inventoryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zybooks.cs360inventoryapp.R;
import com.zybooks.cs360inventoryapp.db.UserDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView register = findViewById(R.id.registerLink);
        Button login = findViewById(R.id.loginButton);

        EditText username = findViewById(R.id.input_Username);
        EditText password = findViewById(R.id.input_Password);

        register.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });
        login.setOnClickListener(v -> {
            String userString = username.getText().toString();
            String passString = password.getText().toString();

            if (userString.isEmpty() || passString.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_LONG).show();
            }

            boolean success = UserDatabaseHelper.getInstance(this).login(userString, passString);
            if (success) {
                Intent intent = new Intent(MainActivity.this, Inventory.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Username or password was incorrect", Toast.LENGTH_LONG).show();
            }

        });
    }
}