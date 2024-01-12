package gwaves.sample;

import java.util.ArrayList;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

import fileio.input.FilterInput;
import fileio.input.SongInput;

import gwaves.collection.Album;
import gwaves.context.Artist;
import gwaves.storage.DataBase;
import gwaves.util.Filterable;

@Getter
public final class Song extends AudioRec implements Filterable {
    // private String album;
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
        
        // TODO sa nu uit aici
        // this.album = songInput.getAlbum();
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
            // if (!this.getName().startsWith(filter.getName())) {
            //     return false;
            // }
            if (!this.getName().toLowerCase().startsWith(filter.getName().toLowerCase())) {
                return false;
            }
        }

        // TODO de verificat
        // if (filter.getAlbum() != null) {
        //     if (!this.album.equals(filter.getAlbum())) {
        //         return false;
        //     }
        // }

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

        // TODO de verificat
        // if (filter.getArtist() != null) {
        //     if (!this.artist.equals(filter.getArtist())) {
        //         return false;
        //     }
        // }

        if (filter.getArtist() != null) {
            if (!this.artist.getUsername().equals(filter.getArtist())) {
                return false;
            }
        }

        return true;
    }

    // TODO de incercat Override la metoda equals doar pentru comparare intre songuri

    // @Override
    // public boolean equals(Object obj) {
    //     if (obj == this)
    //         return true;

    //     if (obj == null || (obj.getClass() != this.getClass()))
    //         return false;

    //     return ((Song)obj).getName().toLowerCase().equals(this.getName().toLowerCase());

    //     // if (obj.getClass() == this.getClass()) {
    //     //     return ((Song)obj).getName() == this.getName()
    //     //             && ((Song)obj).getArtist() == this.getArtist();
    //     // }

    //     // return super.equals(obj);
    //     // return this.hashCode() == ((Song)obj).hashCode();
    // }
}
