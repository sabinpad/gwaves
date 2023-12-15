package gwaves.collection;

import java.util.ArrayList;

import fileio.input.FilterInput;

import gwaves.sample.Song;
import gwaves.util.Filterable;

public final class Playlist extends AudioCollection implements Filterable {
    private Integer followers;
    private Boolean visibility;

    private ArrayList<Song> songs;

    /**
     * Creates empty playlist 
     *
     * @param name of Playlist
     * @param owner name of Playlist owner
     */
    public Playlist(final String name, final String owner) {
        super(name, owner);

        this.followers = 0;
        this.visibility = true;
        this.songs = new ArrayList<>();
    }

    /**
     * @param song adds it to the playlist
     */
    public void addSong(final Song song) {
        this.songs.add(song);
        this.entireDuration += song.getDuration();
    }

    /**
     * @param song removes it from the playlist
     */
    public void removeSong(final Song song) {
        if (this.songs.contains(song)) {
            this.songs.remove(song);
            this.entireDuration -= song.getDuration();
        }
    }

    /**
     * Adds a follow to the song
     */
    public void addFollower() {
        this.followers++;
    }

    /**
     * Removes a like from the song
     */
    public void removeFollower() {
        if (this.followers > 0) {
            this.followers--;
        }
    }

    /**
     * changes the visibility of the playlist
     */
    public void changeVisibility() {
        this.visibility = !this.visibility;
    }

    /**
     * @return an ArrayList containing the songs name
     */
    public ArrayList<String> getSongsNameList() {
        ArrayList<String> nameList = new ArrayList<>();

        for (var song : this.songs) {
            nameList.add(song.getName());
        }

        return nameList;
    }

    /**
     * @return the number of songs the playlist contains
     */
    public int getNrOfSongs() {
        return this.songs.size();
    }

    /**
     * @return the number of followers the playlist has
     */
    public int getNrOfFollowers() {
        return this.followers.intValue();
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
     * @return true if the playlist is visible
     */
    public boolean isVisible() {
        return this.visibility;
    }

    /**
     * @param song
     * @return true if the playlist has song
     */
    public boolean hasSong(final Song song) {
        return this.songs.contains(song);
    }

    /**
     * @param filter used to match
     * @return true if the playlist is matched by the filter
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

        return true;
    }
}
