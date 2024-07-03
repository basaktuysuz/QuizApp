package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quizapp.SelectCategory.SelectCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputLayout loginPassword, loginEmail;
    Button loginBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.email);
        loginPassword = findViewById(R.id.password);
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        loginBtn=findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUser();
            }
        });

    }


    //checking is user exists
    public void isUser() {

        String userEnteredEmail = loginEmail.getEditText().getText().toString().trim();
        System.out.println(userEnteredEmail+"AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        String userEnteredPassword = loginPassword.getEditText().getText().toString().trim();
        System.out.println(userEnteredPassword);

        mAuth.signInWithEmailAndPassword(userEnteredEmail, userEnteredPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User authentication successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // User is logged in
                                String userId = user.getUid();

                                // Retrieve  user details
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference userReference = db.collection("users").document(userId);
                                userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String nameFromDB = document.getString("name");
                                                String usernameFromDB = document.getString("username");
                                                String phoneNoFromDB = document.getString("phoneNo");
                                                String emailFromDB = document.getString("email");
                                                String passwordFromDB = document.getString("password");

                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                // Passing values to another activity
                                                intent.putExtra("name", nameFromDB);
                                                intent.putExtra("username", usernameFromDB);
                                                intent.putExtra("email", emailFromDB);
                                                intent.putExtra("phoneNo", phoneNoFromDB);
                                                intent.putExtra("password", passwordFromDB);

                                                startActivity(intent);
                                                if (!validateEmail() | !validatePassword()) {
                                                    return;
                                                }
                                            } else {
                                                // Document does not exist
                                                Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {

                                            Toast.makeText(getApplicationContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else { // User authentication failed
                            loginPassword.setError("Wrong Password or User does not exist");
                            loginPassword.requestFocus();
                        }
                    }
                });
    }



    public void loginUser(View view) {
        if(!validateEmail()|!validatePassword()){
            return;
        }
        else {
            isUser();
        }
    }
    //validating username
    public Boolean validateEmail() {
        String val = loginEmail.getEditText().getText().toString();
        //   String noWhiteSpace="\"\\\\A\\\\w{4,20}\\\\z\"";
        if (val.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
            loginEmail.setError("Field cannot be empty");
            return false;
        } else {
            loginEmail.setError(null);
            loginEmail.setErrorEnabled(false);
            return true;
        }
    }

    //validating password
    public Boolean validatePassword() {
        String val = loginPassword.getEditText().getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Field cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            loginPassword.setErrorEnabled(false);
            return true;
        }
    }

}