package xyz.akshaykapoor.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Akshay on 27/03/16.
 */
public class ImageAdapter extends BaseAdapter {

    private static final String TAG = "ImageAdapter";
    private Context context;
    private Movie[] movies;


    public ImageAdapter(Context cxt){
        context = cxt;
    }

    public void addMovies(Movie[] movies){
        this.movies = movies;
    }

    public void clear(){
        this.movies = null;
    }

    @Override
    public int getCount() {
        return (movies != null)? movies.length : 0;
    }

    @Override
    public Movie getItem(int position) {
        return movies[position];
    }

    @Override
    public long getItemId(int position) {
        return movies[position].getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView;
        }
        String imagePath = context.getString(R.string.backdrop_image_path) + movies[position].getPoster_path();
        Log.v(TAG, "Image URL : " + imagePath);
        Picasso.with(context).load(imagePath).into(imageView);

        return imageView;
    }

}
