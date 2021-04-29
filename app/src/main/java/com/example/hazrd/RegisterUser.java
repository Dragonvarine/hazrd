package com.example.hazrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class RegisterUser extends AppCompatActivity {

    //Firebase variables
    private Map<String, Object> userData = new HashMap<>();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore database;

    //User details
    String userID;
    private String username;
    private String email;
    private String password;

    //UI variables
    private EditText emailInput;
    private EditText passwordInput1;
    private EditText passwordInput2;
    private TextView signInScreenText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        //UI elements
        emailInput = (EditText) findViewById(R.id.signUpEmailInput1);
        passwordInput1 = (EditText) findViewById(R.id.signUpPasswordInput1);
        passwordInput2 = (EditText) findViewById(R.id.signUpPasswordInput2);
        signInScreenText = (TextView) findViewById(R.id.alreadyUserButton);

        signUpUser();
        navigateToLogin();
    }

    private void signUpUser() {
        Button signUpButton;

        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = emailInput.getText().toString();
                password = passwordInput1.getText().toString();

                try {
                    signUpMethod(email, password);
                } catch (Exception e) {
                    Toast.makeText(RegisterUser.this, "ERROR: Empty fields.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUpMethod(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    private static final String TAG = "Register";

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            user = mAuth.getCurrentUser();
                            userID = user.getUid();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            user.updateProfile(profileUpdate);
                            inputUserData();

                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent(RegisterUser.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    //TODO: Revisit this function
    //Input data to Firestore database.
    public void inputUserData() {
        String TAG = "database";

        userData.put("username", username);
        userData.put("email", email);

        Map<String, Object> update = new HashMap<>();
        update.put(userID, userData);

        database.collection("Users").document(userID).set(update); //Update details to Firestore

//        database.collection("Users").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "Error adding document", e);
//            }
//        });
    }


    private void navigateToLogin() {
        signInScreenText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterUser.this, LogInActivity.class);
                intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}