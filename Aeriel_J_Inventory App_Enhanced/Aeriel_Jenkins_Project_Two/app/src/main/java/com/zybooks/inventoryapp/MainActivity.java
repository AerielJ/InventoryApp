package com.zybooks.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mUserNameEditText, mPasswordEditText;
    private Button mLoginButton, mCreateButton;
    private User mUser;
    private UserDatabase mUserDb;


    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 

        mUserNameEditText = findViewById(R.id.editTextTextUserName);
        mPasswordEditText = findViewById(R.id.editTextTextPassword);
        mLoginButton = findViewById(R.id.loginButton);
        mCreateButton = findViewById(R.id.createButton);

        mUserNameEditText.addTextChangedListener(loginTextWatcher);
        mPasswordEditText.addTextChangedListener(loginTextWatcher);

        mUserDb = new UserDatabase(this);

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {  // TextWatcher must implement three methods
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Nothing to call before text change
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = mUserNameEditText.getText().toString().trim();
            String passwordInput = mPasswordEditText.getText().toString().trim();

            // Enable login and create login buttons when username and password edit texts are not empty
            mLoginButton.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());
            mCreateButton.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Nothing to call after text change
        }
    };

    // Check username and password against database when login button is clicked
    public void onLoginClicked(View view) {
        String name = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        mUser = new User(name, password);
        boolean userExists = mUserDb.checkUser(mUser);

        if (userExists) {
            // Start Inventory List activity
            Intent intent = new Intent(this, InventoryListActivity.class);
            startActivity(intent);
        }
        else {
            // Prompt user to create login
            Toast.makeText(this,"Login Unsuccessful. Please create login", Toast.LENGTH_LONG).show();
        }
    }

    // Add username and password to the database when create login button is clicked
    public void onCreateClicked(View view) {
        String name = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        mUser = new User(name, password);
        boolean userAdded = mUserDb.addUser(mUser);

        if (userAdded) {
            // Start Inventory List activity
            Intent intent = new Intent(this, InventoryListActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Unable to create login", Toast.LENGTH_LONG).show();
        }
    }
}