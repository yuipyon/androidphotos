package app;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * @author Karun Kanda
 * @author Yulin Ni
 */

/**
 * The Photo class contains all the information pertaining to a photo.
 */

public class Photo implements Serializable{

    static final long serialVersionUID = 6266501183382371389L;

    /**
     * String photoName is the name of the photo that is stored in photo.
     */
    public String photoName;

    /**
     * String filepath stores the filepath of the user's photo.
     */
    public String filepath;

    /**
     * ArrayList<Tag> tags contains the tags pertaining to the photo.
     */
    public ArrayList<Tag> tags;

    transient Bitmap image;

    public String uri;



    /**
     * Photo creates a new instance of the Photo class.
     */
    public Photo() {
        this.tags = new ArrayList<Tag>();
    }


    public void setImage(Bitmap image){
        this.image = image;
    }


    /**
     * toString returns the name of the photo.
     */
    public String toString() {
        return photoName;
    }

    public String printTags() {
        StringBuffer sb = new StringBuffer();
        for (Tag t : tags) {
            sb.append(t);
            sb.append(", ");
        }
        String str = sb.toString();
        return str;
    }

    /**
     * Overridden version of equals(Object o) to find if two photos are equal.
     */
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }

        if(this == o) {
            return true;
        }

        if(getClass() != o.getClass()) {
            return false;
        }

        Photo p = (Photo)o;
        return photoName.equals(p.photoName);
    }

}
