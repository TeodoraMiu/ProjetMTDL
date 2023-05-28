package com.example.hopitaux;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmationRequestAdapter extends RecyclerView.Adapter<ConfirmationRequestAdapter.ConfirmationRequestViewHolder> {
    public ArrayList<ConfirmationRequest> confirmationRequests;
    private FirebaseFirestore database;
    private FirebaseAuth mAuth;

    public ConfirmationRequestAdapter(ArrayList<ConfirmationRequest> confirmationRequests) {
        this.confirmationRequests = confirmationRequests;
    }

    @NonNull
    @Override
    public ConfirmationRequestAdapter.ConfirmationRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmation_request_item, parent, false);
        return new ConfirmationRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmationRequestAdapter.ConfirmationRequestViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        String candidateEmail = confirmationRequests.get(position).getCandidate();
        String day = confirmationRequests.get(position).getDay();
        String hour = confirmationRequests.get(position).getHour();
        String confirmed = confirmationRequests.get(position).getConfirmed();
        // go through all of the confirmationRequests and filter out the ones that are already confirmed
        for (ConfirmationRequest request: confirmationRequests) {
            if (request.getConfirmed().equals("true")) {
                confirmationRequests.remove(request);
            }
        }
        // search database for this candidate to show all different info about them
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        if (candidateEmail.equals(mAuth.getCurrentUser().getEmail())) {
            // this means we are logged in as a donor, hide the confirm button
            holder.confirmButton.setVisibility(View.INVISIBLE);
        }

        database.collection("donators").whereEqualTo("email", candidateEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        String candidateName = task.getResult().getDocuments().get(0).getString("name");
                        String candidateBloodType = task.getResult().getDocuments().get(0).getString("bloodType");
                        String candidateAge = task.getResult().getDocuments().get(0).getString("age");
                        holder.candidateName.setText(candidateName);
                        holder.candidateAge.setText(candidateAge);
                        holder.candidateBloodType.setText(candidateBloodType);
                        holder.day.setText(day);
                        holder.hour.setText(hour);
                        if (confirmed == "true") {
                            holder.confirmed.setText("Confirmed!");
                        } else {
                            holder.confirmed.setText("Not confirmed");
                        }
                    } else {
                        Log.e("Error", "no such donor in db");
                    }
                } else {
                    Log.e("Error", "Failed");
                }
            }
        });



        holder.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // confirm this request
                confirmationRequests.get(position).setConfirmed("true");
                // update in database
                database.collection("confirmationRequests").whereEqualTo("title", confirmationRequests.get(position).getTitle()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().getDocuments().isEmpty()) {
                                DocumentReference confirmationRequest = task.getResult().getDocuments().get(0).getReference();
                                Map<String, Object> map = new HashMap<>();
                                map.put("confirmed", "true");
                                confirmationRequest.update(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                confirmationRequest.delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(view.getContext(), "Confirmed appointment!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("teo", "didn't delete appointment");
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(view.getContext(), "Not confirmed!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Log.e("Error", "no such donor in db");
                            }
                        } else {
                            Log.e("Error", "Failed");
                        }
                    }
                });
                holder.confirmButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return confirmationRequests.size();
    }

    public class ConfirmationRequestViewHolder extends RecyclerView.ViewHolder {

        private TextView candidateName;
        private TextView candidateBloodType;
        private TextView candidateAge;
        private TextView day;
        private TextView hour;

        private TextView confirmed;

        private FloatingActionButton confirmButton;

        public ConfirmationRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.confirmationRequestCandidateName);
            candidateBloodType = itemView.findViewById(R.id.confirmationRequestCandidateBloodType);
            candidateAge = itemView.findViewById(R.id.confirmationRequestCandidateAge);
            day = itemView.findViewById(R.id.confirmationRequestDay);
            hour = itemView.findViewById(R.id.confirmationRequestHour);
            confirmed = itemView.findViewById(R.id.confirmed);
            confirmButton = itemView.findViewById(R.id.confirmButton);
        }
    }
}
