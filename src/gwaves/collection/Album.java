package gwaves.collection;

import java.util.ArrayList;

import fileio.input.SongInput;
import fileio.input.FilterInput;

import gwaves.sample.Song;
import gwaves.util.Filterable;

public final class Album extends AudioCollection implements Filterable {
    private int releaseYear;
    private String description;

    private ArrayList<Song> songs;

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
        this.songs = new ArrayList<>();

        for (var songInput : songsInput) {
            this.songs.add(new Song(songInput));
        }
    }

    /**
     *
     * @return
     */
    public int getNrOfLikes() {
        int sum = 0;

        for (var song : this.songs) {
            sum += song.getNrOfLikes();
        }

        return sum;
    }

    /**
     * @return the number of songs the album contains
     */
    public int getNrOfSongs() {
        return this.songs.size();
    }

    /**
     * @param number
     * @return song that coresponds with the number
     */
    public Song getSong(final int number) {
        if (number >= this.songs.size()) {
            return null;
        }

        return this.songs.get(number);
    }

    /**
     *
     * @return
     */
    public ArrayList<Song> getSongs() {
        return this.songs;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getSongsNameList() {
        ArrayList<String> nameList = new ArrayList<>();

        for (var song : this.songs) {
            nameList.add(song.getName());
        }

        return nameList;
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean hasSongWithName(String name) {
        for (var song : this.songs) {
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
