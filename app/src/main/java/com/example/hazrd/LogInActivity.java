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

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class LogInActivity extends AppCompatActivity {

    //Firebase variables
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    //UI variables
    private EditText emailInput;
    private EditText passwordInput;
    private TextView signUpScreenText;

    //User details
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        //UI elements
        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        signUpScreenText = (TextView) findViewById(R.id.createUserButton);


        signInUser();
        navigateToRegistration();
    }

    private void signInUser() {
        Button signInButton;

        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = emailInput.getText().toString().trim();
                password = passwordInput.getText().toString().trim();

                try {
                    signInMethod(email, password);
                } catch (Exception e) {
                    Toast.makeText(LogInActivity.this, "ERROR: Empty fields.",
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
                        String TAG = "Login";

                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();

                            Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser(); //TODO: Will get back to
                            System.out.println("Successful");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            System.out.println("Unsuccessful");
                        }

                        Intent intent = new Intent(LogInActivity.this, ChatClient.class);
                        startActivity(intent);
                    }
                });
    }

    private void navigateToRegistration() {
        signUpScreenText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegisterUser.class);
                intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}