package gwaves.collection;

import java.util.ArrayList;

import fileio.input.SongInput;
import fileio.input.FilterInput;

import gwaves.sample.Song;
import gwaves.util.Filterable;

public final class Album extends AudioCollection<Song> implements Filterable {
    private int releaseYear;
    private String description;

    /**
     * Create new Album object
     *
     * @param name        of Album
     * @param owner       name of Album owner
     * @param releaseYear the year the album was launched
     * @param description of the album
     * @param songsInput  list of songs
     */
    public Album(final String name, final String owner,
                 final Integer releaseYear, final String description,
            final ArrayList<SongInput> songsInput) {
        super(name, owner);

        this.releaseYear = releaseYear;
        this.description = description;

        for (var songInput : songsInput) {
            this.getAudRecs().add(new Song(songInput));
        }
    }

    /**
     *
     * @return
     */
    public int getNrOfLikes() {
        int sum = 0;

        for (var rec : this.getAudRecs()) {
            sum += rec.getLikes();
        }

        return sum;
    }

    public boolean hasSongWithName(String name) {
        for (var song : this.getAudRecs()) {
            if (song.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param filter used to match
     * @return true if the album is matched by the filter
     */
    public boolean isMatchedByFilter(final FilterInput filter) {
        if (filter.getName() != null) {
            if (!this.getName().startsWith(filter.getName())) {
                return false;
            }
        }

        if (filter.getOwner() != null) {
            if (!this.getOwner().equals(filter.getOwner())) {
                return false;
            }
        }

        if (filter.getDescription() != null) {
            if (!this.description.contains(filter.getDescription())) {
                return false;
            }
        }

        return true;
    }
}
