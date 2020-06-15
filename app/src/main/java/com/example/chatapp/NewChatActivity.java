package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.chatapp.Adapter.UserAdapter;
import com.example.chatapp.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> users;
    private TextView name;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        recyclerView=findViewById(R.id.all_users_RV);
        pd=new ProgressDialog(this);
        name=findViewById(R.id.name);
        pd.setMessage("Please Wait...");
        pd.show();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        users=new ArrayList<>();
        userAdapter=new UserAdapter(users,NewChatActivity.this);

        recyclerView.setAdapter(userAdapter);

        getUsers();

    }

    private void getUsers() {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    Log.i("user",user.getEmail());
                    if (!user.getId().equals(firebaseUser.getUid())){
                        users.add(user);
                    }
                    else{
                        name.setText(user.getName());
                    }
                }
                userAdapter.notifyDataSetChanged();
                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                 pd.dismiss();
            }
        });
    }
}
