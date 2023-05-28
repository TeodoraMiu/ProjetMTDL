package com.example.hopitaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProgramMeetup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private RecyclerView recyclerView;

    private String hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_meetup);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        Donor donor = Donor.getInstance();

        recyclerView = findViewById(R.id.appointmentList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ProgramMeetup.this, 1);
        recyclerView.setLayoutManager(layoutManager);

        Log.e("teo", donor.getSelectedAnnouncementTitle());

        database.collection("announcements").whereEqualTo("title", donor.getSelectedAnnouncementTitle()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        hospital = task.getResult().getDocuments().get(0).getString("hospital");
                        ArrayList<Appointment> appointments = new ArrayList<>();
                        AppointmentAdapter adapter = new AppointmentAdapter(appointments, hospital);
                        recyclerView.setAdapter(adapter);

                        database.collection("appointment").whereEqualTo("title", donor.getSelectedAnnouncementTitle()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().getDocuments().isEmpty()) {
                                        for (int index = 0; index < task.getResult().getDocuments().size(); index++) {
                                            String day = task.getResult().getDocuments().get(index).getString("day");
                                            Log.e("teo", day);
                                            String hour = task.getResult().getDocuments().get(index).getString("hour");
                                            Double places = task.getResult().getDocuments().get(index).getDouble("places");
                                            Appointment appointment = new Appointment(donor.getSelectedAnnouncementTitle(), day, hour, places);
                                            adapter.appointments.add(appointment);
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Log.e("Error", "no such donor in db");
                                    }
                                } else {
                                    Log.e("Error", "Failed");
                                }
                            }
                        });
                    } else {
                        Log.e("teo", "title not found when searching for hospital");
                    }
                } else {
                    Log.e("Error", "Failed");
                }
            }
        });
    }
}