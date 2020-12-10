package com.example.photos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;

import app.Album;
import app.Photo;

public class SearchPhotos extends AppCompatActivity {

    ArrayList<Album> albums;
    ArrayList<Photo> allPhotos = new ArrayList<Photo>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photos);

        if(getIntent().getExtras() != null){
            albums = (ArrayList<Album>) getIntent().getSerializableExtra("Album Content");
            //populate allPhotos
            for (Album a : albums) {
                if (!a.photos.isEmpty()) {
                    for (Photo p : a.photos) {
                        allPhotos.add(p);
                    }
                }
            }
        }

    }
}