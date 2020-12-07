package CustomAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photos.R;

import java.io.IOException;
import java.util.ArrayList;

import app.Photo;

public class ThumbnailAdapter<Content> extends ArrayAdapter
{
    private Context context;
    private ArrayList data;

    public ThumbnailAdapter(Context context, ArrayList data){
        super(context, R.layout.grid_style, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View currentView, ViewGroup parent){

        if (currentView == null){
            LayoutInflater i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            currentView = (View) i.inflate(R.layout.grid_style, parent, false);
        }

        ImageView iv = (ImageView) currentView.findViewById(R.id.image);
        TextView tv = (TextView) currentView.findViewById(R.id.caption);

        Photo photo = (Photo) data.get(position);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photo.uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        iv.setImageBitmap(bitmap);
        tv.setText(photo.photoName);

        return currentView;
    }
}
