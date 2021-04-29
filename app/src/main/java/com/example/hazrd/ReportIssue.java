package com.example.hazrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ReportIssue extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, String> theReport = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        user = FirebaseAuth.getInstance().getCurrentUser();

        EditText issueTextBox = (EditText) findViewById(R.id.issueTextBox);


        Button reportButton = (Button) findViewById(R.id.reportButton);

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!issueTextBox.getText().toString().equals("")) {
                    theReport.put("UserID", "Test");
                    theReport.put("Text", issueTextBox.getText().toString());

                    db.collection("Issues").document().set(theReport)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ReportIssue.this, "Issue sent.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ReportIssue.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    issueTextBox.setText(null);
                }
            }
        });
    }
}