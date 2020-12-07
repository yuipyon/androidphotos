package com.example.photos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import app.Album;

public class MainActivity extends AppCompatActivity{
//testing
    private ListView albumList;
    private Button create;
    private EditText albumName;

    ArrayList<Album> items;
    ArrayAdapter<Album> listAdapter;
    Album album;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albumList = (ListView) findViewById(R.id.albumView);

        loadAlbumList();

        listAdapter = new ArrayAdapter<Album>(this, android.R.layout.simple_list_item_1, items);
        albumList.setAdapter(listAdapter);

        Context context = this;

        albumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){

                album = (Album) adapterView.getAdapter().getItem(position);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Options");
                builder1.setCancelable(true);

                final EditText input = new EditText(context);
                input.setText("Rename selected album", TextView.BufferType.EDITABLE);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder1.setView(input);

                builder1.setPositiveButton(
                        "View Album",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(context, Album_View.class);
                                intent.putExtra("Album Name", album);
                                startActivity(intent);
                            }
                        });

                builder1.setNeutralButton(
                        "Rename Album",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listAdapter.remove(album);

                                Album replace = new Album(input.getText().toString());
                                listAdapter.add(replace);


                                saveAlbumList();
                            }
                        });

                builder1.setNegativeButton(
                        "Delete Album",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listAdapter.remove(album);
                                listAdapter.notifyDataSetChanged();
                                saveAlbumList();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        create = (Button) findViewById(R.id.create);
        albumName = (EditText) findViewById(R.id.albumName);
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
    private static boolean albumExists(Album toInsert, ArrayList<Album> items){
        for(Album album: items){
            if(album.equals(toInsert)){
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createAlbum(View view) throws IOException {
        String name = albumName.getText().toString();

        Album newAlbum = new Album(name);

        boolean exists = albumExists(newAlbum, items);

        if(exists == true){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("This Album already exists");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Go Back",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            /*builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });*/

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            listAdapter.add(newAlbum);
            listAdapter.notifyDataSetChanged();
            saveAlbumList();
        }

        System.out.println(items);
        System.out.println(listAdapter);

    }
}