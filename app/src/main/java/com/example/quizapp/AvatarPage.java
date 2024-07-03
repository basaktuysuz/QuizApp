package com.example.quizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AvatarPage extends AppCompatActivity {


    String[] avatarUrls = {
            "https://www.pngarts.com/files/3/Avatar-PNG-High-Quality-Image.png",
            "https://www.pngarts.com/files/11/Avatar-PNG-Photo.png",
            "https://www.pngarts.com/files/11/Avatar-PNG-High-Quality-Image.png",
            "https://www.pngarts.com/files/11/Avatar-PNG-Free-Download.png",
            "https://www.pngarts.com/files/11/Avatar-Free-PNG-Image.png",
            "https://www.pngarts.com/files/11/Avatar-PNG-Image-Background.png",
            "https://w7.pngwing.com/pngs/555/703/png-transparent-computer-icons-avatar-woman-user-avatar-face-heroes-service-thumbnail.png",
            "https://w7.pngwing.com/pngs/122/453/png-transparent-computer-icons-user-profile-avatar-female-profile-heroes-head-woman-thumbnail.png",
            "https://w7.pngwing.com/pngs/129/292/png-transparent-female-avatar-girl-face-woman-user-flat-classy-users-icon-thumbnail.png",
            "https://w7.pngwing.com/pngs/78/788/png-transparent-computer-icons-avatar-business-computer-software-user-avatar-child-face-hand-thumbnail.png",
            "https://w7.pngwing.com/pngs/710/723/png-transparent-digital-marketing-computer-icons-technical-support-user-avatar-avatar-child-face-heroes-thumbnail.png",
            "https://w7.pngwing.com/pngs/238/446/png-transparent-computer-icons-user-profile-avatar-old-man-face-heroes-head-thumbnail.png",
            "https://w7.pngwing.com/pngs/228/419/png-transparent-the-matrix-agent-smith-trinity-the-oracle-neo-others-miscellaneous-face-orange.png",
            "https://w7.pngwing.com/pngs/382/121/png-transparent-morpheus-computer-icons-the-matrix-miscellaneous-purple-angle.png",
            "https://play-lh.googleusercontent.com/7XUn13_eyh3Xg92JKh_nYsQ0LwmBvFdX1lfl15L2ioUK2Ds_MC5nEwBhSiM-GHEYfro",
    };

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_page);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();


        setupAvatarButtons();
    }

    private void setupAvatarButtons() {
        ImageButton avatarButton1 = findViewById(R.id.avatarButton1);
        ImageButton avatarButton2 = findViewById(R.id.avatarButton2);
        ImageButton avatarButton3 = findViewById(R.id.avatarButton3);
        ImageButton avatarButton4 = findViewById(R.id.avatarButton4);
        ImageButton avatarButton5 = findViewById(R.id.avatarButton5);

        loadAvatarIntoButton(avatarButton1, avatarUrls[0]);
        loadAvatarIntoButton(avatarButton2, avatarUrls[1]);
        loadAvatarIntoButton(avatarButton3, avatarUrls[2]);
        loadAvatarIntoButton(avatarButton4, avatarUrls[3]);
        loadAvatarIntoButton(avatarButton5, avatarUrls[4]);


        avatarButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAvatar("https://www.pngarts.com/files/3/Avatar-PNG-High-Quality-Image.png");
            }
        });

        avatarButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAvatar("https://www.pngarts.com/files/11/Avatar-PNG-Photo.png");
            }
        });

        avatarButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAvatar("https://www.pngarts.com/files/11/Avatar-PNG-High-Quality-Image.png");
            }
        });

    }
    // Belirli bir butona avatar yüklemek için method
    private void loadAvatarIntoButton(ImageButton button, String avatarUrl) {
        Glide.with(this)
                .load(avatarUrl) // Avatar URL'sini yükle
                .placeholder(R.drawable.loading) // Yüklenirken gösterilecek placeholder resmi
                .into(button); // Belirtilen butona yükle
    }
    private void updateAvatar(String url) {
        if (firebaseUser != null) {
            DocumentReference userRef = firestore.collection("users").document(firebaseUser.getUid());
            userRef.update("URL", url).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        showAvatarChangedSnackbar();
                    } else {
                        Toast.makeText(AvatarPage.this, "Failed to update avatar", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showAvatarChangedSnackbar() {
        Snackbar.make(findViewById(android.R.id.content), "Your avatar has been changed", Snackbar.LENGTH_LONG).show();
    }
}
