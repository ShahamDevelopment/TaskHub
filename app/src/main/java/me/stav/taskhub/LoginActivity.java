package me.stav.taskhub;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.stav.taskhub.utilities.FirebaseHandler;

public class LoginActivity extends AppCompatActivity {

    private FirebaseHandler fbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fbHandler = new FirebaseHandler(LoginActivity.this);
    }
}