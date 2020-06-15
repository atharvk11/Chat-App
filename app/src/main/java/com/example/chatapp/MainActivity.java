package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chatapp.Adapter.UserAdapter;
import com.example.chatapp.Models.Message;
import com.example.chatapp.Models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton newChatButton;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> users;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //====================HOOKS======================
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        newChatButton=findViewById(R.id.newChatButton);
        recyclerView=findViewById(R.id.recycler_view_main);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        //==================recycler view================
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        users=new ArrayList<>();
        userAdapter=new UserAdapter(users,this);
        recyclerView.setAdapter(userAdapter);


        newChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NewChatActivity.class));
            }
        });

        readAllChats();

    }

    private void readAllChats() {
        FirebaseDatabase.getInstance().getReference().child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.getKey().contains(firebaseUser.getUid())){
                         String s=snapshot.getKey().replace(firebaseUser.getUid(),"");
                        final String s1=s.replace("_","");
                        Log.i("user",s1);
                        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot2:dataSnapshot.getChildren()){
                                    if(snapshot2.getKey().equals(s1)){
                                        Log.i("user","found");
                                        users.add(snapshot2.getValue(User.class));
                                    }
                                }
                                userAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
            FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast tt=Toast.makeText(getApplicationContext(),"Signed Out",Toast.LENGTH_SHORT);
                tt.show();
            break;
        }
        return true;
    }

}
