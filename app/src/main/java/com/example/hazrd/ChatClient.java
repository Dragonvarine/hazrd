package com.example.hazrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatClient extends AppCompatActivity {

    TextView messageInput;
    Button messageButton;
    ArrayList<String> messages = new ArrayList<>(); //TODO: Remove this line, and replace with bottom line.
    //private ArrayList<MessageObject> messages = new ArrayList<>();
    ChatAdapter adapter;
    RecyclerView recyclerView;

    //Database
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, String> messageMap = new HashMap<>();

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
                    //recyclerView.smoothScrollToPosition(messages.size() - 1);
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