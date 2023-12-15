package fileio.input;

import java.util.ArrayList;

public final class FilterInput {
    private String owner;
    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear;
    private String artist;
    private String description;

    public FilterInput() {
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAlbum(final String album) {
        this.album = album;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setTags(final ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }

    public String getLyrics() {
        return this.lyrics;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setReleaseYear(final String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getReleaseYear() {
        return this.releaseYear;
    }

    public void setArtist(final String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
