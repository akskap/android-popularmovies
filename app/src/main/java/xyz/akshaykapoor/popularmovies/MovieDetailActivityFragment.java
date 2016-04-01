package xyz.akshaykapoor.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();
        //Set backdrop image
        ImageView imageView = (ImageView) view.findViewById(R.id.movie_thumbnail);
        String imagePath = getString(R.string.backdrop_image_path)+ intent.getStringExtra("BACKDROP");
        Picasso.with(getContext()).load(imagePath).into(imageView);
        //Set Movie Title
        TextView title = (TextView) view.findViewById(R.id.movie_title);
        title.setText(intent.getStringExtra("TITLE"));
        //Set Movie Synopsis
        TextView synopsis = (TextView) view.findViewById(R.id.movie_synopsis);
        synopsis.setText(intent.getStringExtra("SYNOPSIS"));
        //Set Vote Average
        TextView rating = (TextView) view.findViewById(R.id.movie_vote);
        rating.setText(intent.getStringExtra("VOTE_AVG") + "/10");
        //Set Movie Release Date
        TextView movieRelease = (TextView) view.findViewById(R.id.movie_release_date);
        movieRelease.setText("Released on : " + intent.getStringExtra("RELEASE_DATE"));

        return view;
    }
}
