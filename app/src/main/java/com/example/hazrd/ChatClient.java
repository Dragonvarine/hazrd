package com.example.hazrd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class ChatClient extends AppCompatActivity {
    //Firebase
    FirebaseUser user;

    //UI elements
    TextView messageInput;
    Button messageButton;
    Button mapButton;
    Button issuesButton;

    //Recycler fields
    ChatAdapter adapter;
    RecyclerView recyclerView;

    //Database fields
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, String> messageMap = new HashMap<>();

    //Stored messages
    ArrayList<String> messages = new ArrayList<>();

    //Date and time
    Date currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);

        //Initialise time
        currentTime = new Date();

        //Get user
        user = FirebaseAuth.getInstance().getCurrentUser();

        //UI elements
        messageInput = (EditText) findViewById(R.id.message_input);
        messageButton = (Button) findViewById(R.id.message_button);
        mapButton = (Button) findViewById(R.id.mapButton);
        issuesButton = (Button) findViewById(R.id.issueButton);

        //Initiate the display.
        initRecyclerView();

        //Displays messages when database is updated.
        refreshEvent();

        //Initialise button listeners.
        goToMap();
        goToIssues();

        //Display previous messages
        displayOlderMessages();

        //Enable sending messages.
        sendMessage();


    }

    private void goToIssues() {
        issuesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatClient.this, ReportIssue.class);
                intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void goToMap() {
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatClient.this, MapsActivity.class);
                intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    void refreshEvent() {
        db.collection("Messsages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                db.collection("Messages").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("db", document.getId() + "->" + document.getData());
                                        Map<String, Object> documentMap = new HashMap<>();
                                        documentMap.putAll(document.getData());
                                        System.out.println(documentMap.toString());
                                        String text = (String) documentMap.get("Message");
                                        System.out.println(text);

                                        messages.add(text);

                                        adapter.notifyDataSetChanged();
                                        recyclerView.smoothScrollToPosition(messages.size() - 1);
                                    }
                                } else {
                                    Log.d("db", "Error getting documents");
                                }
                            }
                        });
            }
        });
    }

    private void sendMessage() {
        messageButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageInput.getText().toString().equals("")) {
                    currentTime = Calendar.getInstance().getTime();

                    messageMap.put("Username", user.getUid());
                    messageMap.put("Message", messageInput.getText().toString());

                    db.collection("Messages").document(currentTime.toString()).set(messageMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ChatClient.this, "Saved", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatClient.this, "Failed to save", Toast.LENGTH_SHORT).show();
                                }
                            });

                    messageInput.setText(null);

                } else {
                    Toast.makeText(ChatClient.this, "ERROR: Empty fields.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    private void displayOlderMessages() {
        db.collection("Messages").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("db", document.getId() + "->" + document.getData());
                                Map<String, Object> documentMap = new HashMap<>();
                                documentMap.putAll(document.getData());
                                System.out.println(documentMap.toString());
                                String text = (String) documentMap.get("Message");
                                System.out.println(text);

                                messages.add(text);

                                adapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(messages.size() - 1);
                            }
                        } else {
                            Log.d("db", "Error getting documents");
                        }
                    }
                });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_viewer);
        adapter = new ChatAdapter(messages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}