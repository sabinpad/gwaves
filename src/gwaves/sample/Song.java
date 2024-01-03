package gwaves.sample;

import java.util.ArrayList;

import lombok.Getter;

import fileio.input.FilterInput;
import fileio.input.SongInput;

import gwaves.context.Artist;
import gwaves.storage.DataBase;
import gwaves.util.Filterable;

public final class Song extends AudioRec implements Filterable {
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private Integer releaseYear;
    // TODO in loc de String sa fie referinta la artist
    @Getter
    private Artist artist;
    @Getter
    private int likes;

    public Song(final SongInput songInput) {
        super(songInput.getName(), songInput.getDuration());
        
        this.album = songInput.getAlbum();
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
     * @param filter used to match
     * @return true if the song is matched by the filter
     */
    public boolean isMatchedByFilter(final FilterInput filter) {
        int filterReleaseYear;
        String fmtReleaseYear;

        if (filter.getName() != null) {
            if (!this.getName().startsWith(filter.getName())) {
                return false;
            }
        }

        if (filter.getAlbum() != null) {
            if (!this.album.equals(filter.getAlbum())) {
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
            if (!this.artist.equals(filter.getArtist())) {
                return false;
            }
        }

        return true;
    }

    // TODO de incercat Override la metoda equals doar pentru comparare intre songuri

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            return ((Song)obj).getName() == this.getName()
                    && ((Song)obj).getArtist() == this.getArtist();
        }

        return super.equals(obj);
    }
}
