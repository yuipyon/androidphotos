package com.example.photos;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
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
    Album curr_album;
    Photo photo;

    ThumbnailAdapter photoAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view);

        Context context = this;

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

        photoDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){

                photo = (Photo) adapterView.getAdapter().getItem(position);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Options");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "View Photo",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(context, Photo_Display.class);
                                intent.putExtra("Album Name", curr_album);
                                intent.putExtra("index", position);
                                startActivity(intent);
                            }
                        });

                builder1.setNeutralButton(
                        "Move Photo",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // First delete it from current album
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                                builder2.setMessage("Select the album which you want to move the photo too");
                                builder2.setCancelable(true);

                                final ArrayAdapter<Album> adp = new ArrayAdapter<Album>(context, android.R.layout.simple_list_item_1, items);

                                final Spinner sp = new Spinner(Album_View.this);
                                sp.setLayoutParams(new ViewGroup.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                                sp.setAdapter(adp);

                                builder2.setView(sp);

                                builder2.setNeutralButton(
                                        "Move Photo",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                photoAdapter.remove(photo);
                                                curr_album.photos.remove(photo);

                                                for(int i = 0; i <= items.size() - 1; i++){
                                                    if(items.get(i).equals(curr_album)){
                                                        items.remove(i);
                                                        break;
                                                    }
                                                }
                                                items.add(curr_album);

                                                // Now lets add it to the selected album
                                                Album selectedAlbum = (Album) sp.getSelectedItem();
                                                System.out.println("Selected Album is: " + selectedAlbum);

                                                if(selectedAlbum.photos == null){
                                                    selectedAlbum.photos = new ArrayList<Photo>();
                                                    selectedAlbum.photos.add(photo);
                                                } else {
                                                    selectedAlbum.photos.add(photo);
                                                }

                                                for(int i = 0; i <= items.size() - 1; i++){
                                                    if(items.get(i).equals(selectedAlbum)){
                                                        items.remove(i);
                                                        break;
                                                    }
                                                }
                                                items.add(selectedAlbum);

                                                saveData();
                                            }
                                        }
                                );





                                AlertDialog alert = builder2.create();
                                alert.show();
                            }
                        }
                );


                builder1.setNegativeButton(
                        "Delete Photo",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                photoAdapter.remove(photo);
                                curr_album.photos.remove(photo);

                                for(int i = 0; i <= items.size() - 1; i++){
                                    if(items.get(i).equals(curr_album)){
                                        items.remove(i);
                                        break;
                                    }
                                }
                                items.add(curr_album);

                                saveData();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

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

    @Override
    public void onBackPressed() {
        saveData();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Album Content", items);
        startActivity(intent);
    }

    public void addPhoto(View view) {
        Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, RESULT_LOAD_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == RESULT_LOAD_IMAGE){
                Uri imageUri = data.getData();

                ImageView iv = new ImageView(this);
                iv.setImageURI(imageUri);

                BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
                Bitmap selectedImage = drawable.getBitmap();

                File file = new File(imageUri.getPath());

                Photo toAdd = new Photo();
                toAdd.uri = data.getData().toString();
                toAdd.setImage(selectedImage);
                toAdd.photoName = file.getName();
                toAdd.filepath = file.getAbsolutePath();

                System.out.println("Picture we are adding: " + toAdd);

                if(curr_album.photos == null){
                    curr_album.photos = new ArrayList<Photo>();
                    curr_album.photos.add(toAdd);
                } else {
                    curr_album.photos.add(toAdd);
                }

                for(int i = 0; i <= items.size() - 1; i++){
                    if(items.get(i).equals(curr_album)){
                        items.remove(i);
                        break;
                    }
                }
                items.add(curr_album);

                saveData();

                photoAdapter.add(toAdd);
                photoAdapter.notifyDataSetChanged();
            }
        }
    }
}
