package com.example.hazrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatClient extends AppCompatActivity {

    //UI elements
    TextView messageInput;
    Button messageButton;

    //Recycler fields
    ChatAdapter adapter;
    RecyclerView recyclerView;

    //Database fields
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, String> messageMap = new HashMap<>();

    //Stored messages
    ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);

        //UI elements
        messageInput = (EditText) findViewById(R.id.message_input);
        messageButton = (Button) findViewById(R.id.message_button);

        //Initiate the display.
        initRecyclerView();

        //Enable sending messages.
        sendMessage();

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
                                String text = (String) documentMap.get("message");
                                System.out.println(text);

                                messages.add(text);

                                adapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(messages.size() - 1);
                            }
                        }
                        else {
                            Log.d("db", "Error getting documents");
                        }
                    }
                });
    }

    private void sendMessage() {
        messageButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageInput.getText().toString().equals("")) {

                    messageMap.put("username", "Test_user");
                    messageMap.put("message", messageInput.getText().toString());


                    db.collection("Messages").document().set(messageMap)
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



                    //Update the view.
                    adapter.notifyDataSetChanged();
                    messageInput.setText(null);
                    recyclerView.smoothScrollToPosition(messages.size() - 1);
                } else {
                    Toast.makeText(ChatClient.this, "ERROR: Empty fields.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_viewer);
        adapter = new ChatAdapter(messages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}