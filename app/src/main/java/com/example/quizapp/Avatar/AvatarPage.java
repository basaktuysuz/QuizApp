package com.example.quizapp.Avatar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.example.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AvatarPage extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private RecyclerView recyclerView;
    private List<Avatar> avatarList;
    private AvatarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_page);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        // Initialize Glide
        Glide.init(this, new GlideBuilder()
                .setMemoryCache(new LruResourceCache(64 * 1024 * 1024)) // 24 MB memory cache
                .setDiskCache(new InternalCacheDiskCacheFactory(this, 640 * 1024 * 1024)) // 250 MB disk cache
                .setLogLevel(Log.DEBUG));

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        // 3 columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        avatarList = new ArrayList<>();
        adapter = new AvatarAdapter(this, avatarList);
        recyclerView.setAdapter(adapter);

        loadAvatars();
        setupAvatarButtons();
    }

    private void loadAvatars() {
        StorageReference storageRef = storage.getReference().child("Avatar Icons");
        storageRef.listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (StorageReference fileRef : task.getResult().getItems()) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                avatarList.add(new Avatar(uri.toString()));
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                } else {
                    Toast.makeText(AvatarPage.this, "Failed to load avatars", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupAvatarButtons() {
        adapter.setOnItemClickListener(new AvatarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String imageUrl) {
                updateAvatar(imageUrl);
            }
        });
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
