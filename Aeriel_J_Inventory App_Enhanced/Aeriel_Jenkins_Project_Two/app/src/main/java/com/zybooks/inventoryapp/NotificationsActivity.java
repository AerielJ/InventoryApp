package com.zybooks.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.navigation.NavigationView;

public class NotificationsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final int SMS_REQUEST_CODE = 0;
    private Toolbar mNotificationsToolbar;
    private ToggleButton mToggleButton;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_notifications);

        // Check sms permission when notification activity starts
        checkSMSPermission();

        mNotificationsToolbar = findViewById(R.id.notifications_toolbar);
        setSupportActionBar(mNotificationsToolbar);

        // Check sms permission when toggle button is clicked on
        mToggleButton = findViewById(R.id.toggleButton);
        mToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> checkSMSPermission());

        mDrawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mNotificationsToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(view -> mDrawer.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item3) {
            Toast.makeText(getApplicationContext(), "Logging out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.item1) {
            Intent intent = new Intent(this, InventoryListActivity.class);
            startActivity(intent);
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkSMSPermission() {
        String smsPermission = Manifest.permission.SEND_SMS;

        ActivityCompat.requestPermissions(this,
                new String[]{smsPermission}, SMS_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == SMS_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                // set toggle button to on and disable
                mToggleButton.setChecked(true);
                mToggleButton.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // permission denied
                // set toggle button to off
                mToggleButton.setChecked(false);
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }
}