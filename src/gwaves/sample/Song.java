package gwaves.sample;

import java.util.ArrayList;

import fileio.input.SongInput;

import gwaves.util.Filter;

public class Song extends AudioRec {
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private Integer releaseYear;
    private String artist;

    private Integer likes;

    public Song(SongInput songInput)
    {
        this.name = songInput.getName();
        this.duration = songInput.getDuration();
        this.album = songInput.getAlbum();
        this.tags = songInput.getTags();
        this.lyrics = songInput.getLyrics();
        this.genre = songInput.getGenre();
        this.releaseYear = songInput.getReleaseYear();
        this.artist = songInput.getArtist();
        this.likes = 0;
    }

    public void addLike()
    {
        this.likes++;
    }

    public void removeLike()
    {
        if (this.likes > 0)
            this.likes--;
    }

    public int getNrOfLikes()
    {
        return this.likes.intValue();
    }

    public boolean isMatchedByFilter(Filter filter)
    {
        int filterReleaseYear;
        String fmtReleaseYear;

        if (filter.getName() != null) {
            if (this.name.startsWith(filter.getName()) == false)
                return false;
        }

        if (filter.getAlbum() != null) {
            if (this.album.equals(filter.getAlbum()) == false)
                return false;
        }

        if (filter.getTags() != null) {
            for (var tag : filter.getTags())
                if (this.tags.contains(tag) == false)
                    return false;
        }

        if (filter.getLyrics() != null) {
            if (this.lyrics.indexOf(filter.getLyrics()) == -1)
                return false;
        }

        if (filter.getGenre() != null) {
            if (this.genre.equalsIgnoreCase(filter.getGenre()) == false)
                return false;
        }

        if (filter.getReleaseYear() != null) {
            fmtReleaseYear = filter.getReleaseYear();
            filterReleaseYear = Integer.parseInt(fmtReleaseYear.substring(1));

            if (fmtReleaseYear.charAt(0) == '<' && this.releaseYear >= filterReleaseYear)
                return false;
            
            if (fmtReleaseYear.charAt(0) == '>' && this.releaseYear <= filterReleaseYear)
                return false;
        }

        if (filter.getArtist() != null) {
            if (this.artist.equals(filter.getArtist()) == false)
                return false;
        }

        return true;
    }
}
