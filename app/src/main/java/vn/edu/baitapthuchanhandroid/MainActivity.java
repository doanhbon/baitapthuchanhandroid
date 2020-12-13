package vn.edu.baitapthuchanhandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import vn.edu.baitapthuchanhandroid.adapters.MusicItemAdapter;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MusicItemAdapter musicItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_list_music);
        musicItemAdapter = new MusicItemAdapter(this);
        recyclerView.setAdapter(musicItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}