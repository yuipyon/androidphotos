package com.example.photos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import CustomAdapters.ThumbnailAdapter;
import app.Album;
import app.Photo;
import app.Tag;

public class SearchPhotos extends AppCompatActivity {

    ArrayList<Album> albums;
    ArrayList<Photo> allPhotos = new ArrayList<Photo>();
    Button searchButton;
    Spinner tagType1;
    Spinner tagType2;
    Spinner andOr;
    String[] andOrChoices = {"and", "or"};
    String[] tagTypeChoices = {"person", "location"};
    EditText tagValue1;
    EditText tagValue2;
    ListView searchResults;
    ThumbnailAdapter photoAdapter;
    ArrayList<Photo> searchedPhotos = new ArrayList<Photo>();
//    photos = curr_album.photos;
//    photoAdapter = new ThumbnailAdapter(this, photos);


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

        searchButton = findViewById(R.id.searchButton);
        tagType1 = findViewById(R.id.tagtype1);
        tagType2 = findViewById(R.id.tagtype2);
        andOr = findViewById(R.id.andOr);
        tagValue1 = findViewById(R.id.tagvalue1);
        tagValue2 = findViewById(R.id.tagvalue2);
        searchResults = findViewById(R.id.searchResults);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tagTypeChoices);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, andOrChoices);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagType1.setAdapter(adapter1);
        tagType2.setAdapter(adapter1);
        andOr.setAdapter(adapter2);
    }

    public void search(View view) {
        searchedPhotos.clear();
        if (!tagValue1.getText().toString().isEmpty() && !tagValue2.getText().toString().isEmpty()) {
            Tag tag1 = new Tag(tagType1.getSelectedItem().toString(), tagValue1.getText().toString());
            Tag tag2 = new Tag(tagType2.getSelectedItem().toString(), tagValue2.getText().toString());
            if (andOr.getSelectedItem().toString().equals("and")) {
                for (Photo p : allPhotos) {
                    for (Tag t : p.tags) {
                        if (t.toString().equals(tag1.toString())) {
                            for (Tag u : p.tags) {
                                if (u.toString().equals(tag2.toString())) {
                                    searchedPhotos.add(p);
                                }
                            }
                        }
                    }
                }
            }
            else if (andOr.getSelectedItem().toString().equals("or")) {
                for (Photo p : allPhotos) {
                    for (Tag t : p.tags) {
                        if (t.toString().equals(tag1.toString()) || t.toString().equals(tag2.toString())) {
                            searchedPhotos.add(p);
                        }
                    }
                }
            }
        }
        else if (!tagValue1.getText().toString().isEmpty()) {
            Tag newTag = new Tag(tagType1.getSelectedItem().toString(), tagValue1.getText().toString());
            for (Photo p : allPhotos) {
                for (Tag t : p.tags) {
                    if (t.toString().equals(newTag.toString())) {
                        searchedPhotos.add(p);
                    }
                }
            }
        }
        else if (!tagValue2.getText().toString().isEmpty()) {
            Tag newTag = new Tag(tagType2.getSelectedItem().toString(), tagValue2.getText().toString());
            for (Photo p : allPhotos) {
                for (Tag t : p.tags) {
                    if (t.toString().equals(newTag.toString())) {
                        searchedPhotos.add(p);
                    }
                }
            }
        }

        if (searchedPhotos == null) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("No matching results were found. Please try a different search.");
            alert.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            alert.show();
        }
        else {
            photoAdapter = new ThumbnailAdapter(this, searchedPhotos);
            searchResults.setAdapter(photoAdapter);
        }
    }
}