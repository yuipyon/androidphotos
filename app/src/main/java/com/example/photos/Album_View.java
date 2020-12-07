package com.example.photos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import CustomAdapters.ThumbnailAdapter;
import app.Album;
import app.Photo;

public class Album_View extends AppCompatActivity {

    ListView photoDisplay;
    Button delete;
    Button add;
    Button display;

    private static int RESULT_LOAD_IMAGE = 1;

    ArrayList<Album> items; ArrayList<Photo> photos;
    //ArrayAdapter<Photo> photoAdapter;
    Album curr_album;

    ThumbnailAdapter photoAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view);

        photoDisplay = (ListView) findViewById(R.id.photoDisplay);

        loadData();

        if(getIntent().getExtras() != null){
            curr_album = (Album) getIntent().getSerializableExtra("Album Name");
        }

        if(curr_album.photos != null){
            photos = curr_album.photos;
            photoAdapter = new ThumbnailAdapter(this, photos);
            photoDisplay.setAdapter(photoAdapter);
        } else {
            photos = new ArrayList<Photo>();
            photoAdapter = new ThumbnailAdapter(this, photos);
            photoDisplay.setAdapter(photoAdapter);
        }

        delete = (Button) findViewById(R.id.delete);
        add = (Button) findViewById(R.id.add);
        display = (Button) findViewById(R.id.display);
    }

    private void saveData(){
        SharedPreferences sp = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(items);
        e.putString("task list", json);
        e.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Album>>() {}.getType();
        items = gson.fromJson(json, type);
        if (items == null) {
            items = new ArrayList<>();
        }
    }

    public void addPhoto(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, RESULT_LOAD_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == RESULT_LOAD_IMAGE){
                Uri imageUri = data.getData();
                Photo toAdd = new Photo();
                toAdd.photoName = imageUri.toString();
                toAdd.filepath = imageUri.getPath();
                toAdd.uri = imageUri;
                photoAdapter.add(toAdd);

                curr_album.photos = photos;
                for(int i = 0; i <= items.size() - 1; i++){
                    if(items.get(i).equals(curr_album)){
                        items.remove(i);
                        break;
                    }
                }
                items.add(curr_album);

                saveData();
            }
        }
    }
}
