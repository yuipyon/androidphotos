package com.example.photos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import app.Album;
import app.Photo;
import app.Tag;


public class Photo_Display extends AppCompatActivity {

    ImageView image;
    Button goBack;
    Button goForward;
    Button addTag;
    Button removeTag;
    TextView tags;
    ArrayList<String> tagtypes = new ArrayList<String>();
    ArrayList<Album> items;

    Album curr_album;
    int index;
    Photo curr_photo;
    ArrayList<Tag> tagsList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);

        loadAlbumList();

        if(getIntent().getExtras() != null){
            curr_album = (Album) getIntent().getSerializableExtra("Album Name");
            index = (int) getIntent().getSerializableExtra("index");
            curr_photo = curr_album.photos.get(index);
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
        addTag = findViewById(R.id.addTag);
        removeTag = findViewById(R.id.removeTag);
        tags = findViewById(R.id.tags);

        tagtypes.add("person");
        tagtypes.add("location");
        tags.setText(curr_photo.printTags());
    }

    private void saveAlbumList(){
        SharedPreferences sp = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(items);
        e.putString("task list", json);
        e.apply();
    }

    private void loadAlbumList(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Album>>() {}.getType();
        items = gson.fromJson(json, type);
        if (items == null) {
            items = new ArrayList<>();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void forward(View view) {
        index++;
        if(index < curr_album.photos.size()){
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(curr_album.photos.get(index).uri));
            } catch (IOException e) {
                e.printStackTrace();
            }

            image.setImageBitmap(bitmap);
            curr_photo = curr_album.photos.get(index);
            tags.setText(curr_photo.printTags());
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
            curr_photo = curr_album.photos.get(index);
            tags.setText(curr_photo.printTags());
        } else {
            index++;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
        saveData();
        Intent intent = new Intent(this, Album_View.class);
        intent.putExtra("Album Name", curr_album);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveData(){
        SharedPreferences sp = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(curr_album.photos);
        e.putString("task list", json);
        e.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addTag(View view) {
        Context context = this;
        curr_photo = curr_album.photos.get(index);
        tagsList = curr_photo.tags;

        LayoutInflater factory = LayoutInflater.from(this);

//text_entry is an Layout XML file containing two text field to display in alert dialog
        final View addTagView = factory.inflate(R.layout.add_tag_contents, null);

        final EditText tagValue = (EditText) addTagView.findViewById(R.id.tagValue);
        final Spinner tagTypes = (Spinner) addTagView.findViewById(R.id.tagTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, tagtypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagTypes.setAdapter(adapter);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Add a new tag:").setCancelable(true).setView(addTagView).setPositiveButton("Save",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    boolean proceed = true;
                    String type = (String) tagTypes.getSelectedItem();
                    String value = tagValue.getText().toString();
                    for (Tag t : tagsList) {
                        if (type.equals("location") && t.getTagName().equals("location")) {
                            alert(view, type);
                            proceed = false;
                            break;
                        }
                        if (type.equals("person") && t.getTagName().equals("person") && t.getTagValue().equals(value)) {
                            alert(view, type);
                            proceed = false;
                            break;
                        }
                    }
                    if (proceed) {
                        Tag newTag = new Tag(type, value);
                        curr_photo.tags.add(newTag);

                        for(int i = 0; i <= curr_album.photos.size() - 1; i++){
                            if(curr_album.photos.get(i).equals(curr_photo)){
                                curr_album.photos.remove(i);
                                break;
                            }
                        }
                        curr_album.photos.add(curr_photo);

                        for(int i = 0; i <= items.size() - 1; i++){
                            if(items.get(i).equals(curr_album)){
                                items.remove(i);
                                break;
                            }
                        }

                        items.add(curr_album);

                        saveAlbumList();

                        if(tags != null){
                            tags.setText(curr_photo.printTags());
                        } else {
                            tags.setText(tags.getText() + curr_photo.printTags());
                        }

                        System.out.println("Saving");
                    }
                }
                });

        alert.setNegativeButton(
            "Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }
        );
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void removeTag(View view) {
        Context context = this;
        curr_photo = curr_album.photos.get(index);
        tagsList = curr_photo.tags;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Choose a tag to remove from the dropdown list.");
        builder.setCancelable(true);

        Spinner spinner = new Spinner(Photo_Display.this);
        ArrayAdapter<Tag> adapter = new ArrayAdapter<Tag>(context, android.R.layout.simple_list_item_1, tagsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        builder.setView(spinner);

        builder.setPositiveButton(
            "Remove",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Tag tagToRemove = (Tag) spinner.getSelectedItem();
                    if (tagToRemove != null) {
                        tagsList.remove(tagToRemove);
                        tags.setText(curr_photo.printTags());

                        if(curr_photo.tags == null){
                            curr_photo.tags = new ArrayList<Tag>();
                            curr_photo.tags = tagsList;

                            for(int i = 0; i <= curr_album.photos.size() - 1; i++){
                                if(curr_album.photos.get(i).equals(curr_photo)){
                                    curr_album.photos.remove(i);
                                    break;
                                }
                            }
                            curr_album.photos.add(curr_photo);

                            for(int i = 0; i <= items.size() - 1; i++){
                                if(items.get(i).equals(curr_album)){
                                    items.remove(i);
                                    break;
                                }
                            }

                            items.add(curr_album);

                            saveAlbumList();
                        } else {
                            curr_photo.tags = tagsList;

                            for(int i = 0; i <= curr_album.photos.size() - 1; i++){
                                if(curr_album.photos.get(i).equals(curr_photo)){
                                    curr_album.photos.remove(i);
                                    break;
                                }
                            }
                            curr_album.photos.add(curr_photo);

                            for(int i = 0; i <= items.size() - 1; i++){
                                if(items.get(i).equals(curr_album)){
                                    items.remove(i);
                                    break;
                                }
                            }

                            items.add(curr_album);

                            saveAlbumList();
                        }
                    }
                }
            }
        );

        builder.setNegativeButton(
            "Cancel",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }
        );
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    public void alert(View view, String type) {
        Context context = this;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        if (type.equals("location")) {
            builder1.setMessage("Only one location tag per photo is allowed.");
        }
        else {
            builder1.setMessage("This tag already exists on this photo.");
        }
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert1 = builder1.create();
        alert1.show();
    }
}
