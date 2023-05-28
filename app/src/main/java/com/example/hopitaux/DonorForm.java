package com.example.hopitaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hopitaux.ui.login.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DonorForm extends AppCompatActivity {

    private Button signUpButton;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_form);
        mAuth = FirebaseAuth.getInstance();
//        EditText nameInput = findViewById(R.id.editNameForm);
//        String name = nameInput.getText().toString();
//
//        EditText addressInput = findViewById(R.id.editAddressForm);
//        String address = addressInput.getText().toString();
//
//        EditText cityInput = findViewById(R.id.editCityForm);
//        String city = cityInput.getText().toString();
//
//        EditText ageInput = findViewById(R.id.editAgeForm);
//        String age = ageInput.getText().toString();

        signUpButton = findViewById(R.id.buttonSignUpForm);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameInput = findViewById(R.id.editUsername);
                String username = usernameInput.getText().toString();

                EditText passwordInput = findViewById(R.id.editPasswordForm);
                String password = passwordInput.getText().toString();
                mAuth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(DonorForm.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                                    openAccountDetails();
                                } else {
                                    //parola gresita/email gresit/nu ai cont deloc
                                    Toast.makeText(DonorForm.this, "Authentication failed: " + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    public void openAccountDetails(){
        // go to account details
        Intent intent = new Intent(this, AccountDetails.class);
        startActivity(intent);
    }
}