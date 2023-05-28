package com.example.hopitaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;

public class Recommendations extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recommendationsView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Recommendations.this, 1);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Announcement> announcements = new ArrayList<>();
        PublicationsAdapter adapter = new PublicationsAdapter(announcements, "user");
        recyclerView.setAdapter(adapter);

        // here i filter through all of the announcements and find
        // the ones that correspond to the donor's blood type and city
        String email = mAuth.getCurrentUser().getEmail();

        database.collection("donators").whereEqualTo("email", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   if (!task.getResult().getDocuments().isEmpty()) {
                                                       String bloodType = task.getResult().getDocuments().get(0).getString("bloodType");
                                                       String city = task.getResult().getDocuments().get(0).getString("city");
                                                       database.collection("announcements").whereEqualTo("city", city).whereEqualTo("bloodType", bloodType).get()
                                                               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                       if (task.isSuccessful()) {
                                                                           if (!task.getResult().getDocuments().isEmpty()) {
                                                                               for (int index = 0; index < task.getResult().getDocuments().size(); index++) {
                                                                                   String title = task.getResult().getDocuments().get(index).getString("title");
                                                                                   String hospital = task.getResult().getDocuments().get(index).getString("hospital");
                                                                                   String bloodType = task.getResult().getDocuments().get(index).getString("bloodType");
                                                                                   String reason = task.getResult().getDocuments().get(index).getString("reason");
                                                                                   String description = task.getResult().getDocuments().get(index).getString("description");
                                                                                   Announcement announcement = new Announcement(title, bloodType, reason, description, hospital);
                                                                                   adapter.announcements.add(announcement);
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
                                           }
                                       }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Recommendations.this, "Failed to modify account information!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}