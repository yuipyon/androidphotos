package app;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Karun
 * @author Yulin Ni
 */

/**
 * The Album class stores information related to the User's album
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class Album implements Serializable{

    /**
     * final long serialVersionUID makes the Album class serializable.
     */
    static final long serialVersionUID = -1456323770103513090L;

    /**
     * String name stores the name of the album.
     */
    String name;

    /**
     * ArrayList<Photo> photos stores the album's Photo objects.
     */
    public ArrayList<Photo> photos;

    /**
     * Album creates a new instance of Album.
     * @param name
     */
    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<Photo>();
    }

    /**
     * getName returns the name of the album.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * setName sets the name of the album.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the album.
     */
    public String toString() {
        return name;
    }

    /**
     * Overridden version of equals(Object o) to find if two albums are equal.
     */
    @Override
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

        Album a = (Album)o;
        return name.equals(a.name);
    }

}