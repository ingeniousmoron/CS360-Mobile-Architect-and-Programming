package com.zybooks.cs360inventoryapp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zybooks.cs360inventoryapp.R;
import com.zybooks.cs360inventoryapp.db.UserDatabaseHelper;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = findViewById(R.id.registerButton);
        EditText email = findViewById(R.id.input_Email);
        EditText username = findViewById(R.id.input_username);
        EditText password = findViewById(R.id.input_password);
        EditText confirmPass = findViewById(R.id.confirm_password);

        register.setOnClickListener(v -> {
            String emailString = email.getText().toString();
            String userString = username.getText().toString();
            String passString = password.getText().toString();
            String confirmPassString = confirmPass.getText().toString();

            if(emailString.isEmpty() || userString.isEmpty() || passString.isEmpty() || confirmPassString.isEmpty()) {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!passString.equals(confirmPassString)) {
                Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            Boolean success = UserDatabaseHelper.getInstance(this).add(userString, emailString, passString);
            if(success) {
                Toast.makeText(this, "User added successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error adding user", Toast.LENGTH_LONG).show();
            }



        });
    }

}