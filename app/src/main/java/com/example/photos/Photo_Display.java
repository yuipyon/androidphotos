package com.example.photos;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import app.Album;


public class Photo_Display extends AppCompatActivity {

    ImageView image;
    Button goBack;
    Button goForward;

    Album curr_album;
    int index = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);

        if(getIntent().getExtras() != null){
            curr_album = (Album) getIntent().getSerializableExtra("Album Name");
            index = (int) getIntent().getSerializableExtra("index");
        }

        image = findViewById(R.id.picture);
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(curr_album.photos.get(index).uri));
        } catch (IOException e) {
            e.printStackTrace();
        }

        image.setImageBitmap(bitmap);

        goBack = findViewById(R.id.goBack);
        goForward = findViewById(R.id.goForward);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void forward(View view) {
        index++;
        if(index >= 0){
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(curr_album.photos.get(index).uri));
            } catch (IOException e) {
                e.printStackTrace();
            }

            image.setImageBitmap(bitmap);
        } else {
            index--;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void back(View view) {
        index--;
        if(index >= 0){
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(curr_album.photos.get(index).uri));
            } catch (IOException e) {
                e.printStackTrace();
            }

            image.setImageBitmap(bitmap);
        } else {
            index++;
        }
    }
}
