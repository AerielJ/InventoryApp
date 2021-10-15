package com.zybooks.inventoryapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ItemDetailsActivity extends AppCompatActivity {
    private Item mItem;
    private EditText mNameEditText, mQuantityEditText;
    private ItemDatabase mItemDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        mNameEditText = findViewById(R.id.editTextName);
        mQuantityEditText = findViewById(R.id.editTextQuantity);

        Toolbar detailsToolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(detailsToolbar);

        mItemDb = new ItemDatabase(this);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void onDoneClicked(View view){
        String mName = mNameEditText.getText().toString();
        int mQuantity = Integer.parseInt(mQuantityEditText.getText().toString());
        mItem = new Item(mName, mQuantity);
        boolean itemAdded = mItemDb.addItem(mItem);

        if (itemAdded) {
            Intent intent = new Intent(this, InventoryListActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Unable to add item", Toast.LENGTH_LONG).show();
        }
    }
}