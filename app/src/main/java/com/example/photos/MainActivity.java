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
import android.widget.Toast;

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
    Button search;
    ArrayList<Album> items;
    ArrayAdapter<Album> listAdapter;
    Album album;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albumList = (ListView) findViewById(R.id.albumView);

        if(getIntent().getExtras() != null){
            items = (ArrayList<Album>) getIntent().getSerializableExtra("Album Content");
        }

        loadAlbumList();



        listAdapter = new ArrayAdapter<Album>(this, android.R.layout.simple_list_item_1, items);
        albumList.setAdapter(listAdapter);

        Context context = this;

        albumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){

                album = (Album) adapterView.getAdapter().getItem(position);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setTitle("Options");
                builder1.setCancelable(true);

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
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Rename album");
                                alert.setCancelable(true);
                                final EditText input = new EditText(context);
                                input.setHint("Type in new name here");
                                input.setInputType(InputType.TYPE_CLASS_TEXT);
                                alert.setView(input);
                                alert.setPositiveButton(
                                    "Apply",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            album.setName(input.getText().toString());
                                            listAdapter.notifyDataSetChanged();
                                            saveAlbumList();
                                        }
                                    }
                                );
                                alert.setNegativeButton(
                                    "Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }
                                );
                                alert.show();
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
        search = findViewById(R.id.search);
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

        Context context = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Create an Album");
        builder.setCancelable(true);

        final EditText input = new EditText(this);
        input.setHint("Enter the album's name");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(
                "Create Album",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = input.getText().toString();

                        Album newAlbum = new Album(name);

                        boolean exists = albumExists(newAlbum, items);

                        if(exists == true){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                            builder1.setMessage("This Album already exists");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Go Back",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        } else {
                            listAdapter.add(newAlbum);
                            listAdapter.notifyDataSetChanged();
                            saveAlbumList();
                        }

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showSearchPhotos(View view){
        Intent intent=new Intent(this,SearchPhotos.class);
        intent.putExtra("Album Content", items);
        startActivity(intent);
    }
}