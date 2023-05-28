package com.example.hopitaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AccountInformation extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private Button modifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        String email = mAuth.getCurrentUser().getEmail();

        EditText nameView = findViewById(R.id.accountInfoName);
        EditText addressView = findViewById(R.id.accountInfoAddress);
        EditText cityView = findViewById(R.id.accountInfoCity);
        EditText bloodTypeView = findViewById(R.id.accountInfoBloodType);

        database.collection("donators").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        String name = task.getResult().getDocuments().get(0).getString("name");
                        String address = task.getResult().getDocuments().get(0).getString("address");
                        String city = task.getResult().getDocuments().get(0).getString("city");
                        String bloodType = task.getResult().getDocuments().get(0).getString("bloodType");
                        nameView.setText(name);
                        addressView.setText(address);
                        cityView.setText(city);
                        bloodTypeView.setText(bloodType);
                    } else {
                        Log.e("Error", "no such donor in db");
                    }
                } else {
                    Log.e("Error", "Failed");
                }
            }
        });

        modifyButton = findViewById(R.id.modifyAccountDetailsButton);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameInput = findViewById(R.id.accountInfoName);
                String name = nameInput.getText().toString();

                EditText addressInput = findViewById(R.id.accountInfoAddress);
                String address = addressInput.getText().toString();

                EditText cityInput = findViewById(R.id.accountInfoCity);
                String city = cityInput.getText().toString();

                EditText bloodTypeInput = findViewById(R.id.accountInfoBloodType);
                String bloodType = bloodTypeInput.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("address", address);
                map.put("city", city);
                map.put("bloodType", bloodType);
                database.collection("donators").whereEqualTo("email", email).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                      if (task.isSuccessful()) {
                                                          if (!task.getResult().getDocuments().isEmpty()) {
                                                              DocumentReference donator = task.getResult().getDocuments().get(0).getReference();
                                                              donator.update(map)
                                                                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                          @Override
                                                                          public void onSuccess(Void aVoid) {
                                                                              Intent intent = new Intent(AccountInformation.this, MainMenuDonor.class);
                                                                              startActivity(intent);
                                                                          }
                                                                      })
                                                                      .addOnFailureListener(new OnFailureListener() {
                                                                          @Override
                                                                          public void onFailure(@NonNull Exception e) {
                                                                              Toast.makeText(AccountInformation.this, "Failed to modify account information!", Toast.LENGTH_SHORT).show();
                                                                          }
                                                                      });

                                                          }
                                                      }
                                                  }
                                              }
                        ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AccountInformation.this, "Failed to modify account information!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}