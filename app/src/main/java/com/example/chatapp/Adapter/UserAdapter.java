package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.DetailedChatActivity;
import com.example.chatapp.Models.User;
import com.example.chatapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> users;
    private Context context;

    public UserAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user=users.get(position);
        if(user.getImageurl().equals("default")){
             holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Picasso.get().load(user.getImageurl()).into(holder.imageView);
        }
        holder.names.setText(user.getName());
      //  Log.i("email",user.getEmail());
        holder.names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailedChatActivity.class);
                intent.putExtra("userId",user.getId());
             //   Log.i("id",user.getId());
               context.startActivity(intent);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailedChatActivity.class);
                intent.putExtra("userId",user.getId());
                context.startActivity(new Intent(context, DetailedChatActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView names;
        public CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            names=itemView.findViewById(R.id.name);
            imageView=itemView.findViewById(R.id.profile_image);
        }
    }
}
