package com.example.hopitaux;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    public ArrayList<Appointment> appointments;
    private FirebaseFirestore database;
    private FirebaseAuth mAuth;

    private String hospital;

    public AppointmentAdapter(ArrayList<Appointment> appointments, String hospital) {
        this.appointments = appointments;
        this.hospital = hospital;
    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);
        return new AppointmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        String appointmentDay = appointments.get(position).getDay();
        holder.appointmentDay.setText(appointmentDay);

        String appointmentHour = appointments.get(position).getHour();
        holder.appointmentHour.setText(appointmentHour);

        Double appointmentPlaces = appointments.get(position).getPlaces();
        holder.appointmentPlaces.setText("" + appointmentPlaces);

        String appointmentTitle = appointments.get(position).getTitle();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // donor chose a date and time for the appointment
                // first, need to check if the appointment places number is not 0
                if (appointmentPlaces != 0) {
                    // this interval is available
                    // now, i need to check if this donor is already a candidate for this appointment
                    Log.e("teo", appointments.get(position).getTitle());
                    database.collection("appointment").whereEqualTo("title", appointments.get(position).getTitle()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!task.getResult().getDocuments().isEmpty()) {
                                    Map<String, Object> DBappointments = task.getResult().getDocuments().get(0).getData();
                                    List<String> candidates = (List<String>)DBappointments.get("candidates"); // i hope this works!
                                    boolean ok = true;
                                    for (int i=0; i < candidates.size() - 1; i++) {
                                        if (candidates.get(i).equals(mAuth.getCurrentUser().getEmail())) {
                                            ok = false;
                                            break;
                                        }
                                    }
                                    if (ok) {
                                        // the donor isn't already programmed
                                        // send a confirmation request to the hospital
                                        Map<String, String> map = new HashMap<>();
                                        map.put("candidate", mAuth.getCurrentUser().getEmail());
                                        map.put("day", appointmentDay);
                                        map.put("hour", appointmentHour);
                                        map.put("hospital", hospital);
                                        map.put("title", appointmentTitle);
                                        map.put("confirmed", "false");
                                        Log.e("teo", hospital);
                                        database.collection("confirmationRequests").add(map)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                          @Override
                                                                          public void onSuccess(DocumentReference documentReference) {
                                                                              Toast.makeText(view.getContext(), "Confirmation Request Sent!", Toast.LENGTH_SHORT).show();
                                                                              Intent intent = new Intent(view.getContext(), MainMenuDonor.class);
                                                                              view.getContext().startActivity(intent);
                                                                          }
                                                                      }
                                                ).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(view.getContext(), "Failed to add to database!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(view.getContext(), "You are already programmed for this appointment!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.e("teo", "no such title in appointment db");
                                }
                            } else {
                                Log.e("Error", "Failed");
                            }
                        }
                    });

                } else {
                    // there are no more places for this interval, announce user through a toast
                    Toast.makeText(view.getContext(), "There are no more free places for this date and time interval!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        private TextView appointmentDay;
        private TextView appointmentHour;
        private TextView appointmentPlaces;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentDay = itemView.findViewById(R.id.appointmentDay);
            appointmentHour = itemView.findViewById(R.id.appointmentHour);
            appointmentPlaces = itemView.findViewById(R.id.appointmentPlaces);
        }
    }
}
