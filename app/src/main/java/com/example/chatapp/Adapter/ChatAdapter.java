package com.example.chatapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Models.Message;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    public static int left=0;
    public static int right=1;
    FirebaseUser firebaseUser;

    private Context context;
    private List<Message> messages;

    public ChatAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==right){
            View view= LayoutInflater.from(context).inflate(R.layout.message_sent_layout,parent,false);
            return new ChatAdapter.ViewHolder(view);
        }
        else {
            View view= LayoutInflater.from(context).inflate(R.layout.message_received_layout,parent,false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message=messages.get(position);
        holder.show.setText(message.getMessage());

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show=itemView.findViewById(R.id.show);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getSender().equals(firebaseUser.getUid())){
            return right;
        }
        else
            return left;
    }
}
