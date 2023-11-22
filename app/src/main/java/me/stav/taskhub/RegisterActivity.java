package me.stav.taskhub;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.stav.taskhub.utilities.FirebaseHandler;
import me.stav.taskhub.utilities.InternetConnection;
import me.stav.taskhub.utilities.InternetConnectionReceiver;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseHandler firebaseHandler;
    private EditText editTextEmail, editTextPassword;
    private TextView registerBtn, haveAnAccount;
    private InternetConnectionReceiver internetConnectionReceiver;
    private InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseHandler = new FirebaseHandler(RegisterActivity.this);

        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);

        registerBtn = findViewById(R.id.registerBtn);
        haveAnAccount = findViewById(R.id.haveAnAccount);

        internetConnectionReceiver = new InternetConnectionReceiver();
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetConnectionReceiver, filter);

        registerBtn.setOnClickListener(v -> {
            internetConnection = internetConnectionReceiver.getInternetConnection();
            if (internetConnection.isConnected()) {
                firebaseHandler.registerWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            } else {
                Toast.makeText(RegisterActivity.this, "You need to be connected to the internet to register!", Toast.LENGTH_SHORT).show();
            }
        });

        haveAnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(internetConnectionReceiver);
        super.onDestroy();
    }
}