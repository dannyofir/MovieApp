package com.dannyofir.www.movieapp;

public class Movie {

    private int id;
    private static int idCounter;
    private String name;
    private String about;
    private String url;
    private String year;
    private String rating;
    private String runtime;
    private String genre;
    private String director;
    private String actors;
    private boolean watched = false;
    private String imdbId;

    public Movie(){
    }

    public Movie(String name, String about, String url) {
        this.name = name;
        this.about = about;
        this.url = url;
        setId();
    }

    public int getId() {
        return id;
    }

    //This sets the id of the created movie to a different id every time (using the counter)
    //Because the idCounter is static it can be accessed from every created movie without bein changed.
    public void setId() {
        this.id = idCounter;
        idCounter++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    @Override
    //toString is changed so when we search for a movie each movie will be shown with this toString (in the list).
    public String toString() {
        return name + " | " + year + " | Rating: " + rating;
    }
}
