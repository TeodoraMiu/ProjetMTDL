package com.example.hopitaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hopitaux.ui.login.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private Button signUpButton;
    private Button signInButton;

    private String signInEmail;

    FirebaseAuth mAuth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        signUpButton = findViewById(R.id.signUpButton);
        signInButton = findViewById(R.id.signInButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpActivity();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameInput = findViewById(R.id.editTextTextEmailAddress);
                String username = usernameInput.getText().toString();

                EditText passwordInput = findViewById(R.id.editTextTextPassword);
                String password = passwordInput.getText().toString();
                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    signInEmail = username;
                                    Toast.makeText(MainActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                                    openMainMenu();
                                } else {
                                    //parola gresita/email gresit/nu ai cont deloc
                                    Toast.makeText(MainActivity.this, "Authentication failed: " + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void openSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void openMainMenu() {
        database.collection("donators").whereEqualTo("email", signInEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String role = "";
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        role = task.getResult().getDocuments().get(0).getString("role");
                        // check what role the user has (donor/hospital/admin)
                        if (role.equals("user")) {
                            // go to donor main activity
                            Intent intent = new Intent(MainActivity.this, MainMenuDonor.class);
                            startActivity(intent);
                        } else if (role.equals("hospital")) {
                            // go to hospital main activity
                            Intent intent = new Intent(MainActivity.this, MainMenuHospital.class);
                            startActivity(intent);
                        } else if (role.equals(("admin"))) {
//                            Intent intent = new Intent(MainActivity.this, MainMenuAdmin.class);
//                            startActivity(intent);
                        }
                    } else {
                        Log.e("Error", "no such donor in db");
                    }
                } else {
                    Log.e("Error", "Failed");
                }
            }
        });
    }
}