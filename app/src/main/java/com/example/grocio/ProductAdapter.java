package com.example.grocio;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ProductAdapter extends CursorAdapter {

    public ProductAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Get data from the cursor and bind it to the views
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_PRICE));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_CATEGORY));

        // Bind data to views
        TextView textViewName = view.findViewById(R.id.product_name);
        TextView textViewPrice = view.findViewById(R.id.product_price);
        TextView textViewCategory = view.findViewById(R.id.product_category);

        textViewName.setText(name);
        textViewPrice.setText(String.format("$%.2f", price));
        textViewCategory.setText(category);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate your list item layout and return the view
        return LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
    }
}