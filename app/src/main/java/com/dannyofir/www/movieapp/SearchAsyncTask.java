package com.dannyofir.www.movieapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//This async task takes our search and finds movies that resemble it.
//It sends an array of movies to show in a list and the user selects a single movie to be added to the list in our main activity.
public class SearchAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private Activity activity;
    private ProgressDialog dialog;
    private ArrayList<Movie> searchMovies = new ArrayList<>();
    private ArrayAdapter<Movie> adapter;


    public SearchAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setTitle("Connecting...");
        dialog.setMessage("Please Wait...");
        dialog.show();
    }

    @Override
    //First we send our search string to the omdbapi and get back a jsonobject with our result.
    protected ArrayList doInBackground(String... params) {
        try {

            String search = params[0];

            URL url = new URL(("http://www.omdbapi.com/?s=" + search).replace(" ", "+"));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int httpStatusCode = connection.getResponseCode();

            if (httpStatusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                return null;
            }
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String searchResult = "";

            String oneLine = bufferedReader.readLine();

            while (oneLine != null) {
                searchResult += oneLine;
                oneLine = bufferedReader.readLine();
            }


            // Basically first create jsonobject for all the movies in search, and then when creating each movie,
            // we go to getMoviePlot(); , which goes to the server and gets the rest of the info,
            // All of this happens in the same asynctask.
            JSONObject jsonObject = new JSONObject(searchResult);
            JSONArray jsonArray;
            jsonArray = jsonObject.getJSONArray("Search");
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String movieName = jsonObject.getString("Title");
                String movieImage = jsonObject.getString("Poster");
                String movieId = jsonObject.getString("imdbID");
                JSONObject jsonObjectInfo = getMovieInfo(movieId);
                String moviePlot = jsonObjectInfo.getString("Plot");
                Movie newMovie = new Movie(movieName, moviePlot, movieImage);
                newMovie.setImdbId(movieId);
                newMovie.setGenre(jsonObjectInfo.getString("Genre"));
                newMovie.setDirector(jsonObjectInfo.getString("Director"));
                newMovie.setActors(jsonObjectInfo.getString("Actors"));
                newMovie.setRating(jsonObjectInfo.getString("imdbRating"));
                newMovie.setRuntime(jsonObjectInfo.getString("Runtime"));
                newMovie.setYear(jsonObjectInfo.getString("Year"));
                searchMovies.add(i, newMovie);
            }

            return searchMovies;
        } catch (Exception ex) {
        }
        return null;
    }

    //This method gets called in the doInBackground. It is written here for aesthetics.
    public JSONObject getMovieInfo(String movieId) {
        try {
            URL url = new URL("http://www.omdbapi.com/?i=" + movieId);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int httpStatusCode = connection.getResponseCode();

            if (httpStatusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                return null;
            }
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String result = "";

            String oneLine = bufferedReader.readLine();

            while (oneLine != null) {
                result += oneLine;
                oneLine = bufferedReader.readLine();
            }

            JSONObject jsonObject = new JSONObject(result);
//            String moviePlot = jsonObject.getString("Plot");

            return jsonObject;
        } catch (Exception e) {
        }
        return null;
    }

    //On Post Execute the array is taken and put into an adapter so it can be shown in a list.
    //If array is empty nothing happens and if a previous list was shown is still appears
    //The adapter is edited so the text size and font is shown differently in the listview.
    @Override
    protected void onPostExecute(ArrayList array) {
        dialog.dismiss();
        if(array != null) {
            adapter = new ArrayAdapter<Movie>(activity, android.R.layout.simple_list_item_1, array) {
                @Override
                //Here we change the adapter so the font and size of text in our list is more aesthetic.
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    AssetManager am = activity.getApplicationContext().getAssets();
                    Typeface customFont = Typeface.createFromAsset(am, "fonts/Champagne & Limousines Bold.ttf");
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextSize(22);
                    textView.setTypeface(customFont);
                    return view;
                }
            };
            ListView listView = (ListView) activity.findViewById(R.id.listViewInternetSearch);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(activity, "Invalid Search! Please Try Again.", Toast.LENGTH_SHORT).show();
            EditText editTextSearch = (EditText) activity.findViewById(R.id.editTextAddMovieInternetSearch);
            editTextSearch.setText("");
        }
    }

}
