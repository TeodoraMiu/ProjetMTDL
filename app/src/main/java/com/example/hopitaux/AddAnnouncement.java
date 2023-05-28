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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddAnnouncement extends AppCompatActivity {

    private Button publishButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private String hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        String email = mAuth.getCurrentUser().getEmail();

        database.collection("donators").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        hospital = task.getResult().getDocuments().get(0).getString("name");
                        Log.e("ceva", hospital);
                    } else {
                        Log.e("Error", "no such donor in db");
                    }
                } else {
                    Log.e("Error", "Failed");
                }
            }
        });

        publishButton = findViewById(R.id.buttonPublish);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText titleInput = findViewById(R.id.addTitle);
                String title = titleInput.getText().toString();

                EditText bloodTypeInput = findViewById(R.id.addBloodType);
                String bloodType = bloodTypeInput.getText().toString();

                EditText reasonInput = findViewById(R.id.addReason);
                String reason = reasonInput.getText().toString();

                EditText descriptionInput = findViewById(R.id.addDescription);
                String description = descriptionInput.getText().toString();

                EditText cityInput = findViewById(R.id.addCity);
                String city = cityInput.getText().toString();

                Map<String, String> map = new HashMap<>();
                map.put("title", title);
                map.put("bloodType", bloodType);
                map.put("reason", reason);
                map.put("description", description);
                map.put("city", city);
                map.put("hospital", hospital);
                map.put("hospitalEmail", email);
                database.collection("announcements").add(map)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                  @Override
                                                  public void onSuccess(DocumentReference documentReference) {
                                                      Toast.makeText(AddAnnouncement.this, "Announcement Published!", Toast.LENGTH_SHORT).show();
                                                      Intent intent = new Intent(AddAnnouncement.this, MainMenuHospital.class);
                                                      startActivity(intent);
                                                  }
                                              }
                        ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddAnnouncement.this, "Failed to add to database!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}