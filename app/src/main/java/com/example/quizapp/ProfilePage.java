package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePage extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView email , phoneNo,username,fullname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        email=findViewById(R.id.email_profile);
        phoneNo=findViewById(R.id.phoneNo_profile);
        username=findViewById(R.id.username_profile);
        fullname=findViewById(R.id.fullname_profile);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        fetchUserData();

        ImageView profileIcon = findViewById(R.id.profileicon_profilepage);

        // Receive avatar URL from MainActivity
        String avatarUrl = getIntent().getStringExtra("avatarUrl");


        // Load avatar using Glide
        Glide.with(this)
                .load(avatarUrl)
                .placeholder(R.drawable.loading)
                .into(profileIcon);
    }


    public void fetchUserData() {

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
                            String nameFromDB = document.getString("name");
                            String usernameFromDB = document.getString("username");
                            String phoneNoFromDB = document.getString("phoneNo");
                            String emailFromDB = document.getString("email");

email.setText(emailFromDB);
phoneNo.setText(phoneNoFromDB);
username.setText(usernameFromDB);
fullname.setText(nameFromDB);

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
    }}


