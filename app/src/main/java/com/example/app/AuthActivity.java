package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String mailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_auth);
        EditText username = (EditText) findViewById(R.id.editText1);
        EditText password = (EditText) findViewById(R.id.editText2);
        MaterialButton action = (MaterialButton) findViewById(R.id.action);
        MaterialButton alternate = (MaterialButton) findViewById(R.id.alternate);
        action.setOnClickListener(v -> {
            loginUser(username.getText().toString(), password.getText().toString());
        });
        alternate.setOnClickListener(v -> {
            createUser(username.getText().toString(), password.getText().toString());
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, SystemDashboard.class);
        intent.putExtra("mail", mailID);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("mail", mailID);
        myEdit.commit();
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.e("AuthActivity", "User already signed in");
            mailID = currentUser.getEmail();
            gotoMainActivity();
        }
    }

    private void createUser(String email, String password) {
        if (email == null || Objects.equals(email, "")) {
            Toast.makeText(AuthActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password == null || Objects.equals(password, "")) {
            Toast.makeText(AuthActivity.this, "Enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("AuthActivity", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        mailID = user.getEmail();
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_SHORT).show());
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("AuthActivity", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(AuthActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                });
    }

    private void loginUser(String email, String password) {
        if (email == null || Objects.equals(email, "")) {
            Toast.makeText(AuthActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password == null || Objects.equals(password, "")) {
            Toast.makeText(AuthActivity.this, "Enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("AuthActivity", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        mailID = user.getEmail();
                        //updateUI(user);
                        runOnUiThread(this::gotoMainActivity);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("AuthActivity", "signInWithEmail:failure", task.getException());
                        Toast.makeText(AuthActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                });
    }

    private FirebaseUser getUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        return user;
    }


}