package dev.sabin.gwaves.model.sample;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import dev.sabin.gwaves.model.io.input.FilterInput;
import dev.sabin.gwaves.model.io.input.SongInput;

import dev.sabin.gwaves.model.collection.Album;
import dev.sabin.gwaves.model.context.Artist;
import dev.sabin.gwaves.repository.DataBase;
import dev.sabin.gwaves.service.util.Filterable;

@Getter
public final class Song extends AudioRec implements Filterable {
    @Setter
    private Album album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private Integer releaseYear;
    private Artist artist;
    private int likes;
    @Setter
    private int listenings;

    public Song(final SongInput songInput) {
        super(songInput.getName(), songInput.getDuration());

        this.tags = songInput.getTags();
        this.lyrics = songInput.getLyrics();
        this.genre = songInput.getGenre();
        this.releaseYear = songInput.getReleaseYear();
        this.artist = DataBase.getInstance().queryArtist(songInput.getArtist());
        this.likes = 0;
    }

    /**
     * Adds a like to the song
     */
    public void addLike() {
        this.likes++;
    }

    /**
     * Removes a like from the song
     */
    public void removeLike() {
        if (this.likes > 0) {
            this.likes--;
        }
    }

    /**
     * Adds listening to the song
     */
    public void addListen() {
        this.listenings++;
    }

    /**
     * @param filter used to match
     * @return true if the song is matched by the filter
     */
    public boolean isMatchedByFilter(final FilterInput filter) {
        int filterReleaseYear;
        String fmtReleaseYear;

        if (filter.getName() != null) {
            if (!this.getName().toLowerCase().startsWith(filter.getName().toLowerCase())) {
                return false;
            }
        }

        if (filter.getAlbum() != null) {
            if (!this.album.getName().equals(filter.getAlbum())) {
                return false;
            }
        }

        if (filter.getTags() != null) {
            for (var tag : filter.getTags()) {
                if (!this.tags.contains(tag)) {
                    return false;
                }
            }
        }

        if (filter.getLyrics() != null) {
            if (!this.lyrics.toLowerCase().contains(filter.getLyrics().toLowerCase())) {
                return false;
            }
        }

        if (filter.getGenre() != null) {
            if (!this.genre.equalsIgnoreCase(filter.getGenre())) {
                return false;
            }
        }

        if (filter.getReleaseYear() != null) {
            fmtReleaseYear = filter.getReleaseYear();
            filterReleaseYear = Integer.parseInt(fmtReleaseYear.substring(1));

            if (fmtReleaseYear.charAt(0) == '<' && this.releaseYear >= filterReleaseYear) {
                return false;
            }

            if (fmtReleaseYear.charAt(0) == '>' && this.releaseYear <= filterReleaseYear) {
                return false;
            }
        }

        if (filter.getArtist() != null) {
            if (!this.artist.getUsername().equals(filter.getArtist())) {
                return false;
            }
        }

        return true;
    }
}
