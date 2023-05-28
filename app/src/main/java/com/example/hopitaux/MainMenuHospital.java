package com.example.hopitaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainMenuHospital extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_hospital);

        recyclerView = findViewById(R.id.publicationsHospital);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainMenuHospital.this, 1);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Announcement> announcements = new ArrayList<>();
        PublicationsAdapter adapter = new PublicationsAdapter(announcements, "hospital");
        recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();

        database = FirebaseFirestore.getInstance();

        database.collection("announcements").whereEqualTo("hospitalEmail", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

        addButton = findViewById(R.id.addAnnouncementButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuHospital.this, AddAnnouncement.class);
                startActivity(intent);
            }
        });
    }
}