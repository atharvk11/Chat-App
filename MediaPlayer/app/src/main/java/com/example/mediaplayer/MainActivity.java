package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

import static android.os.Environment.getExternalStorageDirectory;

interface SongInterface {
    void onItemClicked(int position);
}

public class MainActivity extends AppCompatActivity implements SongInterface {

    RecyclerView recyclerView;
    SongsAdapter adapter;
    ArrayList<File> mySongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySongs = new ArrayList<>();

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        show();

    }

    private void show() {
        mySongs = findSong(getExternalStorageDirectory());
        Log.i("iji",""+mySongs.size());

        adapter = new SongsAdapter(this,mySongs,this);
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<File> findSong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.addAll(findSong(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3")) {
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(MainActivity.this,"Playing Song...",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this,MediaActivity.class);

        intent.putExtra("songsList", mySongs).putExtra("position", position);
        startActivity(intent);
    }
}