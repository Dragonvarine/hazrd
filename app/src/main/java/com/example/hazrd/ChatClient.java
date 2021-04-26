package com.example.hazrd;

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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class ChatClient extends AppCompatActivity {

    TextView messageInput;
    Button messageButton;
    ArrayList<String> messages = new ArrayList<>(); //TODO: Remove this line, and replace with bottom line.
    //private ArrayList<MessageObject> messages = new ArrayList<>();
    ChatAdapter adapter;
    RecyclerView recyclerView;
    private ClientSocket clientSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);

        //UI elements
        messageInput = (EditText) findViewById(R.id.message_input);
        messageButton = (Button) findViewById(R.id.message_button);


        Thread thread = new Thread(clientSocket = new ClientSocket());
        thread.start();

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
                    //Initialise background task that the server reads.


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