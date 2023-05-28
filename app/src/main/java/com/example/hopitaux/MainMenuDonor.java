package com.example.hopitaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainMenuDonor extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private FloatingActionButton accountButton;
    private FloatingActionButton recommendationsButton;
    private FloatingActionButton showPastDonationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_donor);

        mAuth = FirebaseAuth.getInstance();

        TextView nameView = findViewById(R.id.userWelcome);
        database = FirebaseFirestore.getInstance();

        String email = mAuth.getCurrentUser().getEmail();

        database.collection("donators").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        String name = task.getResult().getDocuments().get(0).getString("name");
                        nameView.setText(name);
                    } else {
                        Log.e("Error", "no such donor in db");
                    }
                } else {
                    Log.e("Error", "Failed");
                }
            }
        });

        accountButton = findViewById(R.id.showAccountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountActivity();
            }
        });

        recommendationsButton = findViewById(R.id.showRecommendationsButton);
        recommendationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecommendationsActivity();
            }
        });

        showPastDonationsButton = findViewById(R.id.showPastDonationsButton);
        showPastDonationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShowPastDonationsActivity();
            }
        });
    }

    private void openAccountActivity() {
        Intent intent = new Intent(this, AccountInformation.class);
        startActivity(intent);
    }

    private void openRecommendationsActivity() {
        Intent intent = new Intent(this, Recommendations.class);
        startActivity(intent);
    }

    private void openShowPastDonationsActivity() {
        Intent intent = new Intent(this, ShowPastDonations.class);
        startActivity(intent);
    }
}