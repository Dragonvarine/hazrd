package com.example.hazrd;

import java.util.Scanner;

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

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private EditText emailInput;
    private EditText passwordInput;
    private TextView signUpScreenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);


        signUpScreenText = (TextView) findViewById(R.id.createUserButton);


        Intent intent = new Intent(MainActivity.this, ReportIssue.class);
        startActivity(intent);

        //signInUser(); //This will change, purely for testing
        //navigateToRegistration();
      
        //Whole activity is subject to change a lot to fit with the changes.
    }

    private void signInUser() {
        Button signInButton;

        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = emailInput.getText().toString();
                password = passwordInput.getText().toString();

                try {
                    signInMethod(email, password);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "ERROR: Empty fields.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInMethod(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser(); //TODO: Will get back to
                            System.out.println("Successful");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            System.out.println("Unsuccessful");
                        }

                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent);
                    }
                });
    }

    private void navigateToRegistration() {
        signUpScreenText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterUser.class);
                startActivity(intent);
            }
        });
    }

}