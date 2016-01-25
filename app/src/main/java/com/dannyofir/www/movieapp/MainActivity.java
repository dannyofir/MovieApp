package com.dannyofir.www.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_ADD_MOVIE_MANUAL = 100;
    private final static int REQUEST_ADD_MOVIE_INTERNET = 200;
    private final static int REQUEST_EDIT_MOVIE_MANUAL = 300;
    private int editMoviePosition;
    private ArrayList<Movie> myMovies = new ArrayList<>();
    private MovieListAdapter adapter;
    private ListView listViewMyMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Checks file for previous movies on application opening.
        try {
            readFromFile();
        } catch (Exception e) {
        }
        listViewMyMovies = (ListView) findViewById(R.id.listViewMain);
        registerForContextMenu(listViewMyMovies); //Registers context menu for listview longclick
        setMovieListAdapter();
        onListViewItemClick();
    }

    //This function adapts the arraylist of movies into an adapter that can be viewed in the list view.
    private void setMovieListAdapter() {
        adapter = new MovieListAdapter(this, myMovies);
        listViewMyMovies.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Options menu has two options: Delete all movies or exit app.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:
                try {
                    deleteAllMovies();
                    Toast.makeText(MainActivity.this, "All Movies Deleted", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
                break;
            case R.id.exit:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //Add Popup menu to the "+" button, so we can choose between adding a movie manually and adding a movie through the internet.
    public void imageButtonAddMovie_onClick(View view) {
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButtonAddMovie);
        final PopupMenu popupMenuAddMovie = new PopupMenu(this, imageButton);
        popupMenuAddMovie.getMenuInflater().inflate(R.menu.popup_add_menu, popupMenuAddMovie.getMenu());
        //Here we write what happens when we choose either of the option on the Popup menu (Manual or through the internet).
        popupMenuAddMovie.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.addManual) {
                    Intent intent = new Intent(MainActivity.this, EditMovieActivity.class);
                    startActivityForResult(intent, REQUEST_ADD_MOVIE_MANUAL);
                }
                if (item.getItemId() == R.id.addInternet) {
                    Intent intent = new Intent(MainActivity.this, AddMovieInternetActivity.class);
                    startActivityForResult(intent, REQUEST_ADD_MOVIE_INTERNET);
                }
                return true;
            }
        });
        popupMenuAddMovie.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If a RESULT_OK is not returned nothing changes. If it is, it will go on to check the request code.
        if (resultCode != RESULT_OK) {
            return;
        } else if (requestCode == REQUEST_ADD_MOVIE_MANUAL) {
            //Takes the data from EditMovieActivity and creates a new movie to put in the array list. It is then written to the file so it can be viewed
            //even after closing the app
            Movie newMovie = new Movie(data.getStringExtra("Name"), data.getStringExtra("About"), data.getStringExtra("Url"));
            newMovie.setGenre(data.getStringExtra("Genre"));
            newMovie.setDirector(data.getStringExtra("Director"));
            newMovie.setActors(data.getStringExtra("Actors"));
            newMovie.setRating(data.getStringExtra("Rating"));
            newMovie.setRuntime(data.getStringExtra("Runtime"));
            newMovie.setYear(data.getStringExtra("Year"));
            myMovies.add(0, newMovie);
            setMovieListAdapter();
            Toast.makeText(this, "Movie Added", Toast.LENGTH_SHORT).show();
            try {
                writeToFile(newMovie);
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_EDIT_MOVIE_MANUAL) {
            //Takes the data from EditMovieActivity and creates a new movie to put instead of the old one.
            //It then takes the new movie and sends it to editMovie();
            Movie newMovie = new Movie(data.getStringExtra("Name"), data.getStringExtra("About"), data.getStringExtra("Url"));
            newMovie.setGenre(data.getStringExtra("Genre"));
            newMovie.setDirector(data.getStringExtra("Director"));
            newMovie.setActors(data.getStringExtra("Actors"));
            newMovie.setRating(data.getStringExtra("Rating"));
            newMovie.setRuntime(data.getStringExtra("Runtime"));
            newMovie.setYear(data.getStringExtra("Year"));
            try {
                editMovie(newMovie);
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_ADD_MOVIE_INTERNET) {
            //Takes the data from the selected movie in the AddMovieInternetActivity listview.
            //It is then written to the file so it can be viewed even after closing the app.
            Movie newMovie = new Movie(data.getStringExtra("Name"), data.getStringExtra("About"), data.getStringExtra("Url"));
            newMovie.setGenre(data.getStringExtra("Genre"));
            newMovie.setDirector(data.getStringExtra("Director"));
            newMovie.setActors(data.getStringExtra("Actors"));
            newMovie.setRating(data.getStringExtra("Rating"));
            newMovie.setRuntime(data.getStringExtra("Runtime"));
            newMovie.setYear(data.getStringExtra("Year"));
            myMovies.add(0, newMovie);
            setMovieListAdapter();
            Toast.makeText(this, "Movie Added", Toast.LENGTH_SHORT).show();
            try {
                writeToFile(newMovie);
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Take a newly created movie and writes it's details down to the txt file, so we can use the data later.
    //This does not overwrite the existing text file only adds to it.
    public void writeToFile(Movie newMovie) throws Exception {
        String path = getFilesDir().getAbsolutePath();
        String fileName = path + "/Movies.txt";
        FileWriter writer = new FileWriter(fileName, true);
        writer.write(newMovie.getName() + "\r\n" + newMovie.getAbout() + "\r\n" + newMovie.getUrl() + "\r\n" + newMovie.getGenre() + "\r\n" +
                newMovie.getDirector() + "\r\n" + newMovie.getActors() + "\r\n" + newMovie.getRating() + "\r\n" + newMovie.getRuntime() + "\r\n" + newMovie.getYear() + "\r\n");
        writer.close();
    }

    //Takes the txt file and reads all the data from it to create Movies to put into the arraylist (and then adapter).
    public void readFromFile() throws Exception {
        String path = getFilesDir().getAbsolutePath();
        String fileName = path + "/Movies.txt";
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String movieName = bufferedReader.readLine();
        while (movieName != null) {
            String movieAbout = bufferedReader.readLine();
            String movieUrl = bufferedReader.readLine();
            String movieGenre = bufferedReader.readLine();
            String movieDirector = bufferedReader.readLine();
            String movieActors = bufferedReader.readLine();
            String movieRating = bufferedReader.readLine();
            String movieRuntime = bufferedReader.readLine();
            String movieYear = bufferedReader.readLine();
            Movie newMovie = new Movie(movieName, movieAbout, movieUrl);
            newMovie.setGenre(movieGenre);
            newMovie.setDirector(movieDirector);
            newMovie.setActors(movieActors);
            newMovie.setRating(movieRating);
            newMovie.setRuntime(movieRuntime);
            newMovie.setYear(movieYear);
            //We put the newest movies at the start of the array so they appear at the top of our list.
            myMovies.add(0, newMovie);
            movieName = bufferedReader.readLine();
        }
        bufferedReader.close();
        fileReader.close();
    }

    //Takes a edited movie and replaces it with the old one in the arraylist.
    //We use a temp int (editMoviePosition) to remember where in the array to replace the movie.
    //We basically update arraylist (and adapter), create new txt file, and rewrite all movies (with new arraylist) to the new txt.
    public void editMovie(Movie newMovie) throws Exception {
        myMovies.set(editMoviePosition, newMovie);
        setMovieListAdapter();
        String path = getFilesDir().getAbsolutePath();
        String fileName = path + "/Movies.txt";
        FileWriter writer = new FileWriter(fileName);
        //We start from the end of the array so the newest movies are written at the end of the txt file.
        for (int i = myMovies.size()-1; i >= 0; i--) {
            writeToFile(myMovies.get(i));
        }
        writer.close();
    }

    //This clears the arraylist and creates clean txt file, deleting all previous data.
    public void deleteAllMovies() throws Exception {
        myMovies.clear();
        setMovieListAdapter();
        String path = getFilesDir().getAbsolutePath();
        String fileName = path + "/Movies.txt";
        FileWriter writer = new FileWriter(fileName);
        writer.close();
    }

    //If item in list is clicked, we are sent to EditMovieActivity with all the movies data in the relevant fields.
    public void onListViewItemClick() {

        listViewMyMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this , EditMovieActivity.class);
                Movie newMovie = myMovies.get(i);
                intent.putExtra("Name", newMovie.getName());
                intent.putExtra("About", newMovie.getAbout());
                intent.putExtra("Url", newMovie.getUrl());
                intent.putExtra("Genre", newMovie.getGenre());
                intent.putExtra("Director", newMovie.getDirector());
                intent.putExtra("Actors", newMovie.getActors());
                intent.putExtra("Rating", newMovie.getRating());
                intent.putExtra("Runtime", newMovie.getRuntime());
                intent.putExtra("Year", newMovie.getYear());
                editMoviePosition = i;
                startActivityForResult(intent, REQUEST_EDIT_MOVIE_MANUAL);
            }
        });
    }

    //Take the movie in the arraylist at the position, and removes it.
    //Then a new txt file is created with the new arraylist (without the deleted movie).
    public void deleteFromFile(int position) throws Exception {
        myMovies.remove(position);
        setMovieListAdapter();
        String path = getFilesDir().getAbsolutePath();
        String fileName = path + "/Movies.txt";
        FileWriter writer = new FileWriter(fileName);
        //We start from the end of the array so the newest movies are written at the end of the txt file.
        for (int i = myMovies.size()-1; i >= 0; i--) {
            writeToFile(myMovies.get(i));
        }
        writer.close();
    }

    //This creates a context menu on a long click, and gives us two options: Edit or Delete
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listViewMain) {
//            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            editMoviePosition = acmi.position;
            menu.add(0, v.getId(), 0, "Edit Movie");
            menu.add(0, v.getId(), 1, "Delete Movie");
        }
    }

    //Here we write the code for each of the action(edit and delete)
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getOrder()) {
            case 0:
                Intent intent = new Intent(this, EditMovieActivity.class);
                Movie newMovie = myMovies.get(editMoviePosition);
                intent.putExtra("Name", newMovie.getName());
                intent.putExtra("About", newMovie.getAbout());
                intent.putExtra("Url", newMovie.getUrl());
                intent.putExtra("Genre", newMovie.getGenre());
                intent.putExtra("Director", newMovie.getDirector());
                intent.putExtra("Actors", newMovie.getActors());
                intent.putExtra("Rating", newMovie.getRating());
                intent.putExtra("Runtime", newMovie.getRuntime());
                intent.putExtra("Year", newMovie.getYear());
                startActivityForResult(intent, REQUEST_EDIT_MOVIE_MANUAL);
                break;
            case 1:
                try {
                    deleteFromFile(editMoviePosition);
                    Toast.makeText(this, "Movie Deleted", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

}
