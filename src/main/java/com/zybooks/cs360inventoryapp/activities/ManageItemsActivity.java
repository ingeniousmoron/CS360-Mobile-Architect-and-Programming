package com.zybooks.cs360inventoryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.zybooks.cs360inventoryapp.R;
import com.zybooks.cs360inventoryapp.models.Constants;
import com.zybooks.cs360inventoryapp.models.Item;

public class ManageItemsActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextUnit;
    EditText editTextQuant;

    Button buttonSave;

    Item currentItem;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name = editTextName.getText().toString();
            String unit = editTextUnit.getText().toString();
            String quantString = editTextQuant.getText().toString();

            buttonSave.setEnabled(!name.isEmpty() && !unit.isEmpty() && !quantString.isEmpty());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_items);

        Toolbar toolbar = findViewById(R.id.toolbarManage);
        setSupportActionBar(toolbar);

        editTextName = findViewById(R.id.editTextName);
        editTextUnit = findViewById(R.id.editTextUnit);
        editTextQuant = findViewById(R.id.editTextQuant);

        buttonSave = findViewById(R.id.saveButton);

        buttonSave.setOnClickListener(v -> {

            currentItem.setItemName(editTextName.getText().toString());
            currentItem.setItemUnit(editTextUnit.getText().toString());
            currentItem.setQuantity(Integer.parseInt(editTextQuant.getText().toString()));

            Intent result = getResult(false);

            setResult(RESULT_OK, result);
            finish();
        });

        currentItem = new Item();
        Bundle extras = getIntent().getExtras();
        if(null != extras) {
            currentItem.setItemName(extras.getString(Constants.ITEM_NAME));
            currentItem.setItemUnit(extras.getString(Constants.ITEM_UNIT));
            currentItem.setQuantity(extras.getInt(Constants.ITEM_QUANT));
            currentItem.setId(extras.getLong(Constants.ITEM_ID));
        }

        editTextName.setText(currentItem.getItemName());
        editTextUnit.setText(currentItem.getItemUnit());
        editTextQuant.setText(Integer.toString(currentItem.getQuantity()));

        editTextName.addTextChangedListener(textWatcher);
        editTextUnit.addTextChangedListener(textWatcher);
        editTextQuant.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_items_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.delete_item) {
            setResult(RESULT_OK, getResult(true));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent getResult(boolean deleteItem) {
        Intent result = new Intent(getApplicationContext(), Inventory.class);

        result.putExtra(Constants.ITEM_NAME, currentItem.getItemName());
        result.putExtra(Constants.ITEM_UNIT, currentItem.getItemUnit());
        result.putExtra(Constants.ITEM_QUANT,currentItem.getQuantity());
        result.putExtra(Constants.ITEM_ID, currentItem.getId());
        result.putExtra(Constants.DELETE_ITEM, deleteItem);

        return result;
    }
}