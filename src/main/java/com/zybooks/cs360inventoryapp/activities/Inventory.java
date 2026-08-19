package com.zybooks.cs360inventoryapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zybooks.cs360inventoryapp.R;
import com.zybooks.cs360inventoryapp.adaptors.ItemListAdapter;
import com.zybooks.cs360inventoryapp.models.Constants;
import com.zybooks.cs360inventoryapp.models.Item;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zybooks.cs360inventoryapp.db.InventoryDatabaseHelper;

public class Inventory extends AppCompatActivity {

    ArrayList<Item> items;
    GridView itemGridView;
    FloatingActionButton addFab;
    private ItemListAdapter adapter;
    ActivityResultLauncher genericActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        EdgeToEdge.enable(this);

        Toolbar toolbar = findViewById(R.id.toolbarInventory);
        setSupportActionBar(toolbar);

        items = new ArrayList<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            InventoryDatabaseHelper.getInstance(Inventory.this).getAllItems(items);

            handler.post(()-> Toast.makeText(Inventory.this, "Retrieved all items", Toast.LENGTH_LONG).show());
        });

        adapter = new ItemListAdapter(this, R.layout.inventory_item, items);

        itemGridView = findViewById(R.id.inventoryGridView);
        itemGridView.setAdapter(adapter);
        itemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item i = adapter.getItem(position);
                if (i != null) {
                    launchManageItemActivity(i);
                }
            }
        });

        addFab = findViewById(R.id.fabAdd);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchManageItemActivity(new Item());
            }
        });
        initLauncher();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inventory_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.sms_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchManageItemActivity(Item item) {
        Intent intent = new Intent(getApplicationContext(), ManageItemsActivity.class);

        intent.putExtra(Constants.ITEM_ID, item.getId());
        intent.putExtra(Constants.ITEM_NAME, item.getItemName());
        intent.putExtra(Constants.ITEM_UNIT, item.getItemUnit());
        intent.putExtra(Constants.ITEM_QUANT, item.getQuantity());

        genericActivityLauncher.launch(intent);
    }

    private void initLauncher() {
        genericActivityLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result ->
                        {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent intent = result.getData();
                                if (intent != null) {
                                    handleResults(intent);
                                }
                            }
                        });
    }

    private void handleResults(Intent intent) {

        boolean shouldDeleteItem =
                intent.getBooleanExtra(Constants.DELETE_ITEM, false);

        Item item = new Item();
        item.setItemName(intent.getStringExtra(Constants.ITEM_NAME));
        item.setItemUnit(intent.getStringExtra(Constants.ITEM_UNIT));
        item.setQuantity(intent.getIntExtra(Constants.ITEM_QUANT, 0));
        item.setId(intent.getLongExtra(Constants.ITEM_ID, 0));

        if(!shouldDeleteItem) {
            upsertItem(item);
        } else {
            deleteItem(item);
        }
    }

    private void upsertItem(Item item) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(()-> {

            String message =
                    String.format(item.getId() == 0 ? "%s was added." : "%s was updated.", item.getItemName());

            if (item.getId() == 0) {
                long id = InventoryDatabaseHelper.getInstance(Inventory.this).add(item);
                item.setId(id);
                items.add(item);
            } else {
                InventoryDatabaseHelper.getInstance(Inventory.this).update(item);

                items.forEach(i -> {
                    if (i.getId() == item.getId()) {
                        i.setItemName(item.getItemName());
                        i.setItemUnit(item.getItemUnit());
                        i.setQuantity(item.getQuantity());
                    }
                });
            }
            handler.post(()-> {
                adapter.notifyDataSetChanged();
                Toast.makeText(Inventory.this, message, Toast.LENGTH_LONG).show();
            });
        });

    }

    private void deleteItem(Item item) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(()-> {

            String message;

            boolean wasDeleted = InventoryDatabaseHelper.getInstance(Inventory.this).delete(item);

            if (wasDeleted) {
                message = "Item was removed.";

                int itemIndex = -1;
                for (int i = 0; i < items.size(); i++) {
                    if (item.getId() == items.get(i).getId()) {
                        itemIndex = i;
                        break;
                    }
                }

                if (-1 != itemIndex) {
                    items.remove(itemIndex);
                }

            } else {
                message = "Problem removing item";
            }
            handler.post(()-> {
                adapter.notifyDataSetChanged();
                Toast.makeText(Inventory.this, message, Toast.LENGTH_LONG).show();
            });
        });
    }
}