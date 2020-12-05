package com.example.mediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    ArrayList<File> myFiles;
    Context context;
    SongInterface songInterface;

    public SongsAdapter(Context context, ArrayList<File> myFiles, SongInterface songInterface) {
        this.myFiles = myFiles;
        this.context = context;
        this.songInterface = songInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_song,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.name.setText(myFiles.get(position).getName().replace(".mp3", ""));

            holder.itemView.setOnClickListener(v -> songInterface.onItemClicked(position));

    }

    @Override
    public int getItemCount() {
        return myFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
        }
    }
}
