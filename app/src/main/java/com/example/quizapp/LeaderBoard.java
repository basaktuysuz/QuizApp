package com.example.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class LeaderBoard extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        db = FirebaseFirestore.getInstance();

        // İlk üç kullanıcıyı çekin ve gösterin
        showTopThreeUsers();
        //kalan kullanıcıları göster
        showRemainingUsers();
    }

    private void showTopThreeUsers() {
        CollectionReference usersRef = db.collection("highestScores");
        usersRef.orderBy("score", Query.Direction.DESCENDING).limit(3).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                    if (documents.size() > 0) {
                        setTopUser(documents.get(0), R.id.firstUserAvatar, R.id.firstUserName, R.id.firstUserScore);
                    }
                    if (documents.size() > 1) {
                        setTopUser(documents.get(1), R.id.secondUserAvatar, R.id.secondUserName, R.id.secondUserScore);
                    }
                    if (documents.size() > 2) {
                        setTopUser(documents.get(2), R.id.thirdUserAvatar, R.id.thirdUserName, R.id.thirdUserScore);
                    }
                }
            } else {
                // Handle error
                Exception e = task.getException();
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTopUser(DocumentSnapshot document, int avatarId, int nameId, int scoreId) {
        ImageView avatar = findViewById(avatarId);
        TextView name = findViewById(nameId);
        TextView score = findViewById(scoreId);

        //firestoredan verileri alma
        Long userScore = document.getLong("score");
        String userName = document.getString("name");
        String url = document.getString("URL");

        if (url != null) {
            // Load the avatar image from URL using a library like Picasso or Glide
            Glide.with(this).load(url).into(avatar);
        }
        //avatar kısmı
        name.setText(userName);
        score.setText(String.valueOf(userScore) + " QP");
    }

    private void showRemainingUsers() {
        CollectionReference usersRef = db.collection("highestScores");
        usersRef.orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> userList = new ArrayList<>(task.getResult().getDocuments());
                // İlk üç kullanıcıyı ekleme
                if (userList.size() > 3) {
                    userList.subList(0, 3).clear(); // İlk üçü kaldır
                } else {
                    userList.clear(); // Eğer toplam kullanıcı sayısı üç veya daha azsa, geri kalan kullanıcı kalmaz
                }

                RecyclerView recyclerView = findViewById(R.id.leadershipRecycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                UserAdapter adapter = new UserAdapter(userList);
                recyclerView.setAdapter(adapter);
            } else {
                // Handle error
                Exception e = task.getException();
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Firestore instance


}