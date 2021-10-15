package com.zybooks.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class InventoryListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private ItemDatabase mItemDb;

    ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_inventory_list);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mDrawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        mItemDb = new ItemDatabase(this);

        // Lookup the recyclerview in activity layout
        RecyclerView rvItems = findViewById(R.id.rvItems);

        // Initialize items
        items = mItemDb.getItems(ItemDatabase.ItemSortOrder.ALPHABETIC);

        // Create adapter passing in the item data
        ItemsAdapter adapter = new ItemsAdapter(this,items);

        // Attach the adapter to the recyclerview to populate items
        rvItems.setAdapter(adapter);

        // Set layout manager to position the items
        rvItems.setLayoutManager(new LinearLayoutManager(this));

    }

    public void onFABClicked(View view) {
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item3) {
            Toast.makeText(getApplicationContext(), "Logging out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.item2) {
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}