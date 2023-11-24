package gwaves.collection;

import java.util.ArrayList;

import fileio.input.FilterInput;
import gwaves.sample.Song;

public final class Playlist extends AudioCollection {
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
     * @param song
     */
    public void addSong(final Song song) {
        this.songs.add(song);
        this.entireDuration += song.getDuration();
    }

    /**
     * @param song
     */
    public void removeSong(final Song song) {
        if (this.songs.contains(song)) {
            this.songs.remove(song);
            this.entireDuration -= song.getDuration();
        }
    }

    /**
     * @return
     */
    public void addFollower() {
        this.followers++;
    }

    /**
     * @return
     */
    public void removeFollower() {
        if (this.followers > 0) {
            this.followers--;
        }
    }

    /**
     * @return
     */
    public void changeVisibility() {
        this.visibility = !this.visibility;
    }

    /**
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
     * @return
     */
    public int getNrOfSongs() {
        return this.songs.size();
    }

    /**
     * @return
     */
    public int getNrOfFollowers() {
        return this.followers.intValue();
    }

    /**
     * @param song
     * @return
     */
    public int getSongNr(final Song song) {
        return this.songs.indexOf(song);
    }

    /**
     * @param number
     * @return
     */
    public Song getSong(final int number) {
        if (number >= this.songs.size()) {
            return null;
        }

        return this.songs.get(number);
    }

    /**
     * @param song
     * @return
     */
    public Song getSongAfter(final Song song) {
        int i;

        i = (this.songs.indexOf(song) + 1) % this.songs.size();

        return this.songs.get(i);
    }

    /**
     * @param song
     * @return
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
     * @return
     */
    public boolean isVisible() {
        return this.visibility;
    }

    /**
     * @param song
     * @return
     */
    public boolean hasSong(final Song song) {
        return this.songs.contains(song);
    }

    /**
     * @param song
     * @return
     */
    public boolean isFirst(final Song song) {
        return (this.songs.indexOf(song) == 0);
    }

    /**
     * @param song
     * @return
     */
    public boolean isLast(final Song song) {
        return (this.songs.indexOf(song) == (this.songs.size() - 1));
    }

    /**
     * @param filter
     * @return
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
