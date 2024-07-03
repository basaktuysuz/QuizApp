package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quizapp.Avatar.AvatarPage;
import com.example.quizapp.SelectCategory.SelectCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        String name= (String) bundle.get("username");
        TextView nametext = findViewById(R.id.nameWelcome);
        nametext.setText(name+ " !!!");


         updateAvatarIconOnMainpage();
        Button startQ = findViewById(R.id.startQuiz);
        startQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectCategory.class);
                startActivity(intent);
            }
        });
        Button leader = findViewById(R.id.seeLeaderboard);
        leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LeaderBoard.class);
                startActivity(intent);
            }
        });
        Button avatae = findViewById(R.id.avatarChange);
        avatae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AvatarPage.class);

                startActivity(intent);
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        updateAvatarIconOnMainpage();
    }
    public void updateAvatarIconOnMainpage() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // if user exists
            String userId = user.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userReference = db.collection("users").document(userId);
            userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String urlFromDB = document.getString("URL");

                            // Load image
                            ImageView imageView = findViewById(R.id.profileicon);
                            Glide.with(getApplicationContext())
                                    .load(urlFromDB)
                                    .placeholder(R.drawable.loading) //  image while loading
                                    .into(imageView);



                            // Set click listener for profile icon
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Open ProfilePage and pass avatarUrl
                                    Intent intent = new Intent(MainActivity.this, ProfilePage.class);
                                    intent.putExtra("avatarUrl", urlFromDB);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            // user not exists
                            Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    }

