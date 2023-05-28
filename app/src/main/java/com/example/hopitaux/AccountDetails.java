package com.example.hopitaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountDetails extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        signUpButton = findViewById(R.id.buttonSave);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser email = mAuth.getCurrentUser();

                EditText nameInput = findViewById(R.id.modifyName);
                String name = nameInput.getText().toString();

                EditText addressInput = findViewById(R.id.modifyAddress);
                String address = addressInput.getText().toString();

                EditText cityInput = findViewById(R.id.modifyCity);
                String city = cityInput.getText().toString();

                EditText ageInput = findViewById(R.id.modifyAge);
                String age = ageInput.getText().toString();

                EditText bloodTypeInput = findViewById(R.id.modifyBloodType);
                String bloodType = bloodTypeInput.getText().toString();

                Donor donor = Donor.getInstance();
                donor.setName(name);
                donor.setEmail(email.getEmail());
                donor.setAddress(address);
                donor.setCity(city);
                donor.setAge(age);
                donor.setBloodType(bloodType);

                Map<String, String> map = new HashMap<>();
                map.put("email", email.getEmail());
                map.put("name", name);
                map.put("address", address);
                map.put("city", city);
                map.put("age", age);
                map.put("bloodType", bloodType);
                database.collection("donators").add(map)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                  @Override
                                                  public void onSuccess(DocumentReference documentReference) {
                                                      Toast.makeText(AccountDetails.this, "Account Details Saved!", Toast.LENGTH_SHORT).show();
                                                      openMainMenu();
                                                  }
                                              }
                        ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AccountDetails.this, "Failed to add to database!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    public void openMainMenu(){
        // go to account details
        Intent intent = new Intent(this, MainMenuDonor.class);
        startActivity(intent);
    }
}