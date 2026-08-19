package com.zybooks.cs360inventoryapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.zybooks.cs360inventoryapp.R;
import com.zybooks.cs360inventoryapp.models.SMS;
import android.telephony.PhoneNumberUtils;

public class Settings extends AppCompatActivity {

    Switch smsToggle;
    EditText phoneNumber;
    Button saveSettings;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phoneNumberString = phoneNumber.getText().toString();

            saveSettings.setEnabled((phoneNumberString.length() == 10 && smsToggle.isChecked()) || !smsToggle.isChecked());
            PhoneNumberUtils.formatNumber(phoneNumberString, "US");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SMS settings = getSettings();

        Toolbar toolbar = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);

        saveSettings = findViewById(R.id.settingsButton);
        smsToggle = findViewById(R.id.smsSwitch);
        phoneNumber.addTextChangedListener(textWatcher);
        phoneNumber = findViewById(R.id.editTextPhone);

        phoneNumber.setText(String.valueOf(settings.getPhoneNumber()));
        smsToggle.setChecked(settings.isToggle());

        saveSettings.setOnClickListener(v -> {
            String phoneNumberString = phoneNumber.getText().toString();
            boolean smsToggleState = smsToggle.isChecked();
            saveSettings(phoneNumberString, smsToggleState);
            finish();
        });
        //delete if builds dont work
        saveSettings.setEnabled(false);
    }

    private void saveSettings(String phoneNum, boolean smsTog) {
        SharedPreferences sharedPrefs = getSharedPreferences("CS360InventoryApp", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("PHONE_NUMBER", phoneNum);
        editor.putBoolean("SMS_TOGGLE", smsTog);
        editor.apply();
    }

    private SMS getSettings() {
        SMS savedSettings = new SMS();

        SharedPreferences sharedPrefs = getSharedPreferences("CS360InventoryApp", Context.MODE_PRIVATE);

        savedSettings.setPhoneNumber(sharedPrefs.getString("PHONE_NUMBER", ""));
        savedSettings.setToggle(sharedPrefs.getBoolean("SMS_TOGGLE", false));

        return savedSettings;
    }
}