package me.stav.taskhub.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import me.stav.taskhub.HomeActivity;

public class FirebaseHandler {
    private final Context context;
    private final FirebaseAuth fbAuth;
    public FirebaseHandler(Context context) {
        this.context = context;
        this.fbAuth = FirebaseAuth.getInstance();
    }

    // Function gets two strings, and uses Firebase to login.
    public void loginWithEmailAndPassword(String email, String password) {
        if (checkIsEmailValid(email) && checkIsPasswordValid(password)) {
            fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) { // Login Succeed
                    Toast.makeText(context, "Login succeeded", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) { // Failed to login
                    Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Function gets a string and checks if it is a valid email and if it is not empty
    private boolean checkIsEmailValid(String email) {
        // Checking string is not empty
        if (email.isEmpty()) {
            Toast.makeText(this.context, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Checking string is a valid email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Email is not valid", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Function gets a string and checks if the string is not empty, contains capital letter, lower case and a number
    private boolean checkIsPasswordValid(String password) {
        // Sets the flags for checking
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;

        // Checking every character in the password string
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if( Character.isDigit(ch)) {    // Checks if is number
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {   // Checks if is capital letter
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) { // Checks if is lower case letter
                lowerCaseFlag = true;
            }
        }

        if (password.length() < 8) {    // Checks string length is 8 characters or more
            Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!capitalFlag) { // Checks the capital flag
            Toast.makeText(context, "Password must contain at least 1 capital letter", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!lowerCaseFlag) {   // Checks the lower case flag
            Toast.makeText(context, "Password must contain at least 1 lower case letter", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!numberFlag) {  // Check the number flag
            Toast.makeText(context, "Password must contain at least 1 number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
