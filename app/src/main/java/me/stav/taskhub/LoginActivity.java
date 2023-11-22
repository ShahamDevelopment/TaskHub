package me.stav.taskhub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.stav.taskhub.utilities.FirebaseHandler;
import me.stav.taskhub.utilities.InternetConnection;
import me.stav.taskhub.utilities.InternetConnectionReceiver;

public class LoginActivity extends AppCompatActivity {

    private FirebaseHandler firebaseHandler;
    private EditText editTextEmail, editTextPassword;
    private TextView loginBtn, forgotPassword, createNewAccount;
    private InternetConnectionReceiver internetConnectionReceiver;
    private InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializing values

        firebaseHandler = new FirebaseHandler(LoginActivity.this);

        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);

        loginBtn = findViewById(R.id.loginBtn);
        forgotPassword = findViewById(R.id.forgotPassword);
        createNewAccount = findViewById(R.id.createNewAccount);

        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        internetConnectionReceiver = new InternetConnectionReceiver();
        registerReceiver(internetConnectionReceiver, filter);

        loginBtn.setOnClickListener(v -> {
            internetConnection = internetConnectionReceiver.getInternetConnection();
            if (internetConnection.isConnected()) {
                firebaseHandler.loginWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            } else {
                Toast.makeText(LoginActivity.this, "You need to be connected to the internet to login!", Toast.LENGTH_SHORT).show();
            }
        });

        createNewAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        forgotPassword.setOnClickListener(v -> {
            forgotPassword();
        });
    }

    private void forgotPassword() {
        EditText editTextAlertDialog = new EditText(this);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Forgot your password?")
                .setMessage("Enter your email address")
                .setView(editTextAlertDialog)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String email = editTextAlertDialog.getText().toString();
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        firebaseHandler.forgotPassword(email);
                        dialog.dismiss();
                    } else {
                        editTextAlertDialog.setError("Email is not valid");
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(internetConnectionReceiver);
        super.onDestroy();
    }
}