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

public class ShowPastDonations extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_past_donations);

        recyclerView = findViewById(R.id.pastDonations);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ShowPastDonations.this, 1);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<ConfirmationRequest> requests = new ArrayList<>();
        ConfirmationRequestAdapter adapter = new ConfirmationRequestAdapter(requests);
        recyclerView.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        database = FirebaseFirestore.getInstance();

        database.collection("confirmationRequests").whereEqualTo("candidate", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        for (int index = 0; index < task.getResult().getDocuments().size(); index++) {
                            String candidate = task.getResult().getDocuments().get(index).getString("candidate");
                            String confirmed = task.getResult().getDocuments().get(index).getString("confirmed");
                            String day = task.getResult().getDocuments().get(index).getString("day");
                            String hour = task.getResult().getDocuments().get(index).getString("hour");
                            String title = task.getResult().getDocuments().get(index).getString("title");
                            String hospital = task.getResult().getDocuments().get(index).getString("hospital");
                            ConfirmationRequest confirmationRequest = new ConfirmationRequest(candidate, confirmed, day, hour, hospital, title);
                            adapter.confirmationRequests.add(confirmationRequest);
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

    }
}