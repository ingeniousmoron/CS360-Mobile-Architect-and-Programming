package com.zybooks.cs360inventoryapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zybooks.cs360inventoryapp.R;
import com.zybooks.cs360inventoryapp.models.Item;
import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends ArrayAdapter<Item> {
    public ItemListAdapter(@NonNull Context context, int resource, @NonNull List<Item> itemList) {
        super(context, resource, itemList);
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        View gridView = convertView;

        if (gridView == null) {
            gridView = LayoutInflater.from(getContext()).inflate(R.layout.inventory_item, parent, false);
        }

        TextView name = gridView.findViewById(R.id.textViewItemName);
        TextView quantity = gridView.findViewById(R.id.textViewItemQty);
        TextView unit = gridView.findViewById(R.id.textViewItemUnit);

        Item currentItem = getItem(position);

        name.setText(currentItem.getItemName());
        quantity.setText(Integer.toString(currentItem.getQuantity()));
        unit.setText(currentItem.getItemUnit());

        return gridView;
    }
}
