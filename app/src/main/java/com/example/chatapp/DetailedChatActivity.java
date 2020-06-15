package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Adapter.ChatAdapter;
import com.example.chatapp.Models.Message;
import com.example.chatapp.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailedChatActivity extends AppCompatActivity {
    private CircleImageView imageView;
    private TextView name;
    private EditText message;
    private ImageView send;
    private Toolbar toolbar;
    private FirebaseUser firebaseUser;
    DatabaseReference reference;
    private RecyclerView recyclerView;
    private List<Message> messageList;
    private ChatAdapter chatAdapter;
    String received,sent,messageID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_chat);

//=================hooks===============

        imageView = findViewById(R.id.profile_image_detail);
        name = findViewById(R.id.name_detail);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        message = findViewById(R.id.message_EditText);
        send = findViewById(R.id.send);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//============= recycler view==============

        recyclerView=findViewById(R.id.recycle_view_chatDetails);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageList=new ArrayList<>();
        chatAdapter=new ChatAdapter(this,messageList);
        recyclerView.setAdapter(chatAdapter);

        Intent intent = getIntent();
        sent=firebaseUser.getUid();
        received=intent.getStringExtra("userId");
        setData();
        if(sent.compareTo(received)>0){
            messageID=sent+"_"+received;
        }
        else {
            messageID=received+"_"+sent;
        }
        readMessages();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(message.getText())) {
                    sendMessage(message.getText().toString());
                    chatAdapter.notifyDataSetChanged();
                }
                else{
                    Toast tt= Toast.makeText(getApplicationContext(),"Can't send empty message",Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER,0,0);
                    tt.show();
                }
                message.setText("");
                message.setHint("Type a message");
            }
        });


    }

    private void setData() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(received).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                name.setText(user.getName());
                if (user.getImageurl().equals("default")) {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImageurl()).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String message){
        HashMap<String,Object> map=new HashMap<>();
        map.put("sender",sent);
        map.put("receiver",received);
        map.put("message",message);
        map.put("time","default");

        Log.i("messageID",messageID);
        reference=FirebaseDatabase.getInstance().getReference().child("Chats").child(messageID);
        String s=reference.push().getKey();
        reference.child(s).setValue(map);

    }

    private void readMessages(){
        FirebaseDatabase.getInstance().getReference().child("Chats").child(messageID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    Log.i("message",message.getMessage());
                    messageList.add(message);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


