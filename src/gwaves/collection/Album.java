package gwaves.collection;

import java.util.ArrayList;

import lombok.Getter;

import fileio.input.SongInput;
import fileio.input.FilterInput;

import gwaves.sample.Song;
import gwaves.util.Filterable;

@Getter
public final class Album extends AudioCollection<Song> implements Filterable {
    private int releaseYear;
    private String description;
    private int listenings;

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
            Song song = new Song(songInput);
            song.setAlbum(this);
            this.getAudRecs().add(song);
            // this.getAudRecs().add(new Song(songInput));
        }
    }

    public void addListen() {
        this.listenings++;
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
