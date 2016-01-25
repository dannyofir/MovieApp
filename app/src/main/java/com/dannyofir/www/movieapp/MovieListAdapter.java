package com.dannyofir.www.movieapp;


import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//Custom adapter for the main activity listview. Lets us see the movie poster and other details from the main activity.
public class MovieListAdapter extends ArrayAdapter<Movie>{

    private Activity activity;
    private ArrayList<Movie> movies;
    private LayoutInflater layoutInflater;

    public MovieListAdapter(Activity activity, ArrayList<Movie> movies) {
        super(activity, 0, movies);

        this.activity = activity;
        this.movies = movies;

        layoutInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = movies.get(position);
        View itemLayout = layoutInflater.inflate(R.layout.item_movie, null);
        String imageURL = movie.getUrl();
        ImageView movieImage = (ImageView) itemLayout.findViewById(R.id.imageViewItemMovieList);
        ImageAsyncTask imageAsyncTask = new ImageAsyncTask(activity, movieImage);
        imageAsyncTask.execute(imageURL);
        AssetManager am = activity.getApplicationContext().getAssets();
        Typeface customFont = Typeface.createFromAsset(am, "fonts/Champagne & Limousines Bold.ttf");
        TextView textViewMovieName = (TextView) itemLayout.findViewById(R.id.textViewItemMovieListName);
        textViewMovieName.setText(movie.getName());
        textViewMovieName.setTypeface(customFont);
        textViewMovieName.setTextColor(Color.BLACK);
        TextView textViewMovieYearRating = (TextView) itemLayout.findViewById(R.id.textViewItemMovieListYearRating);
        textViewMovieYearRating.setText(movie.getYear() +" | Rating: "+ movie.getRating());
        textViewMovieYearRating.setTypeface(customFont);
        TextView textViewMovieRuntimeGenre = (TextView) itemLayout.findViewById(R.id.textViewItemMovieListRuntimeGenre);
        textViewMovieRuntimeGenre.setText(movie.getRuntime() +" | "+ movie.getGenre());
        textViewMovieRuntimeGenre.setTypeface(customFont);
        return itemLayout;
    }
}
