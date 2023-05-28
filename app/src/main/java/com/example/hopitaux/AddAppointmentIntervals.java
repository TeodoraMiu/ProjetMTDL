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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAppointmentIntervals extends AppCompatActivity {

    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment_intervals);

        database = FirebaseFirestore.getInstance();

        Button addButton = findViewById(R.id.addButtonAppointment);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText dayInput = findViewById(R.id.addDayAppointment);
                String day = dayInput.getText().toString();

                EditText hourInput = findViewById(R.id.addHourAppointment);
                String hour = hourInput.getText().toString();

                EditText placesInput = findViewById(R.id.addPlacesAppointment);
                Double places = Double.parseDouble(placesInput.getText().toString());

                Map<String, Object> map = new HashMap<>();
                map.put("day", day);
                map.put("hour", hour);
                map.put("places", places);
                List<String> candidates = Arrays.asList();
                map.put("candidates", candidates);
                Hospital hospital = Hospital.getInstance();
                map.put("title", hospital.getSelectedAnnouncement());

                database.collection("appointment").add(map)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                  @Override
                                                  public void onSuccess(DocumentReference documentReference) {
                                                      Toast.makeText(AddAppointmentIntervals.this, "Appointment Interval Successfully Added!", Toast.LENGTH_SHORT).show();
                                                      Intent intent = new Intent(AddAppointmentIntervals.this, MainMenuHospital.class);
                                                      startActivity(intent);
                                                  }
                                              }
                        ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddAppointmentIntervals.this, "Failed to add to database!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}