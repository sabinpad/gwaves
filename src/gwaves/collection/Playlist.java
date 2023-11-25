package gwaves.collection;

import java.util.ArrayList;

import fileio.input.FilterInput;

import gwaves.sample.Song;
import gwaves.util.Filterable;

public final class Playlist extends AudioCollection implements Filterable  {
    private ArrayList<Song> songs;
    private Boolean visibility;

    private Integer followers;

    public Playlist(final String name, final String owner) {
        super(name, owner);

        this.songs = new ArrayList<>();
        this.visibility = true;
        this.followers = 0;
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
     * @param song
     * @return index of song in playlist
     */
    public int getSongNr(final Song song) {
        return this.songs.indexOf(song);
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
     * @param song
     * @return song after the specified song
     */
    public Song getSongAfter(final Song song) {
        int i;

        i = (this.songs.indexOf(song) + 1) % this.songs.size();

        return this.songs.get(i);
    }

    /**
     * @param song
     * @return song before the specified song
     */
    public Song getSongBefore(final Song song) {
        int i;

        i = (this.songs.indexOf(song) - 1) % this.songs.size();

        if (i == -1) {
            i = this.songs.size() - 1;
        }

        return this.songs.get(i);
    }

    /**
     * @return true if the playlist ss visible
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
     * @param song
     * @return true if the song if the first in the playlist
     */
    public boolean isFirst(final Song song) {
        return (this.songs.indexOf(song) == 0);
    }

    /**
     * @param song
     * @return if the song is the last in the playlist
     */
    public boolean isLast(final Song song) {
        return (this.songs.indexOf(song) == (this.songs.size() - 1));
    }

    /**
     * @param filter used to match
     * @return true if the song is matched by the filter
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
