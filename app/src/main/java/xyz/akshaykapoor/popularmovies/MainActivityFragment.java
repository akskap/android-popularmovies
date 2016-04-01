package xyz.akshaykapoor.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private String BASE_PATH;
    private String TOP_RATED_PATH;
    private String POPULAR_PATH;
    private String API_KEY_PARAM;
    private ImageAdapter adapter = null;

    private URL url = null;
    private HttpURLConnection urlConnection = null;
    private BufferedReader bufferedReader = null;
    private StringBuffer lineDataBuffer = new StringBuffer();


    private String LOG_TAG = getClass().getName();

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        BASE_PATH = getString(R.string.thumbnail_base_path);
        TOP_RATED_PATH = getString(R.string.top_rated_path);
        POPULAR_PATH = getString(R.string.popular_path);
        API_KEY_PARAM = getString(R.string.api_key_param);

        MovieData movieData = new MovieData();

        movieData.execute(POPULAR_PATH);


        GridView thumbnails = (GridView) view.findViewById(R.id.movie_snaps);
        adapter = new ImageAdapter(getActivity());
        thumbnails.setAdapter(adapter);

        thumbnails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("TITLE",adapter.getItem(position).getTitle());
                intent.putExtra("BACKDROP",adapter.getItem(position).getBackdrop_path());
                intent.putExtra("SYNOPSIS",adapter.getItem(position).getOverview());
                intent.putExtra("VOTE_AVG",adapter.getItem(position).getVote_average()  + "");
                intent.putExtra("RELEASE_DATE",adapter.getItem(position).getRelease_date());
                startActivity(intent);
            }
        });
        return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        MovieData movieData = new MovieData();

        if (id == R.id.action_most_popular) {
            movieData.execute(POPULAR_PATH);
        }
        else if(id == R.id.action_top_rated) {
            movieData.execute(TOP_RATED_PATH);
        }
        return true;
    }



    private class MovieData extends AsyncTask<String, Void, Movie[]>{

        @Override
        protected Movie[] doInBackground(String... params) {
            Uri uri = Uri.parse(BASE_PATH).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(API_KEY_PARAM,getString(R.string.api_key_value))
                    .build();
            try {
                url = new URL(uri.toString());
                Log.v("MainActivityFragment","URL : " + uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String lineData = bufferedReader.readLine();

                while(lineData != null){
                    lineDataBuffer.append(lineData + "\n");
                    lineData = bufferedReader.readLine();
                }
            }
            catch(MalformedURLException ex){
                Log.e("MainActivityFragment","Malformed URL exception " + ex.getMessage());
            }
            catch(IOException ex){
                Log.e("MainActivityFragment","IOException " + ex.getMessage());
            }
            catch(Exception e){
                Log.e("MainActivityFragment","Exception " + e.getMessage());
            }
            finally{
                try{
                    if(bufferedReader != null)
                        bufferedReader.close();
                }
                catch(IOException e){
                    Log.e("MainActviityFragment","Error closing bufferedReader " + e.getMessage());
                }
            }
            return populateMovieData(lineDataBuffer.toString());
        }

        private Movie[] populateMovieData(String data){
            try{
                JSONObject moviesData = new JSONObject(lineDataBuffer.toString());
                Gson gson = new Gson();
                JSONArray arrayOfObjects = moviesData.getJSONArray("results");
                Movie[] movies = new Movie[arrayOfObjects.length()];
                for (int counter = 0; counter<arrayOfObjects.length(); counter++){
                    JSONObject movie = (JSONObject) arrayOfObjects.get(counter);
                    movies[counter] = gson.fromJson(movie.toString(), Movie.class);
                }
                return movies;
            }
            catch(JSONException e){
                Log.e("MainActivityFragment","Error in JSON conversion " + e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(Movie[] movies){
            if(movies != null) {
                adapter.clear();
                adapter.addMovies(movies);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
