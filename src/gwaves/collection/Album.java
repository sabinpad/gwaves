package gwaves.collection;

import java.util.ArrayList;

import fileio.input.SongInput;
import fileio.input.FilterInput;

import gwaves.sample.Song;
import gwaves.util.Filterable;

public class Album extends AudioCollection implements Filterable {
    private int releaseYear;
    private String description;

    private ArrayList<Song> songs;

    /**
     * Create new Album object 
     *
     * @param name of Album
     * @param owner name of Album owner
     * @param releaseYear the year the album was launched
     * @param description of the album
     * @param songsInput list of songs
     */
    public Album(final String name, final String owner, final Integer releaseYear, final String description, final ArrayList<SongInput> songsInput) {
        super(name, owner);

        this.releaseYear = releaseYear;
        this.description = description;
        this.songs = new ArrayList<>();

        for (var songInput : songsInput) {
            this.songs.add(new Song(songInput));
        }
    }

    public int getNrOfLikes() {
        int sum = 0;

        for (var song : this.songs)
            sum += song.getNrOfLikes();

        return sum;
    }

    public ArrayList<Song> getSongs() {
        return this.songs;
    }

    public ArrayList<String> getSongsNameList() {
        ArrayList<String> nameList = new ArrayList<>();

        for (var song : this.songs) {
            nameList.add(song.getName());
        }

        return nameList;
    }

    /**
     * @param filter used to match
     * @return true if the album is matched by the filter
     */
    public boolean isMatchedByFilter(final FilterInput filter) {
        if (filter.getName() != null) {
            if (!this.name.startsWith(filter.getName())) {
                return false;
            }
        }

        if (filter.getOwner() != null) {
            if (!this.owner.equals(filter.getOwner())) {
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
