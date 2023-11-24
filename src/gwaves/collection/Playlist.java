package gwaves.collection;

import java.util.ArrayList;

import fileio.input.FilterInput;
import gwaves.sample.Song;
import gwaves.collection.AudioCollection;

public class Playlist extends AudioCollection {
    private ArrayList<Song> songs;
    private Boolean visibility;

    private Integer followers;

    public Playlist(String name, String owner)
    {
        super(name, owner);

        this.songs = new ArrayList<>();
        this.visibility = true;
        this.followers = 0;
    }

    public void addSong(Song song)
    {
        this.songs.add(song);
        this.entireDuration += song.getDuration();
    }

    public void removeSong(Song song)
    {
        if (this.songs.contains(song)) {
            this.songs.remove(song);
            this.entireDuration -= song.getDuration();
        }
    }

    public void addFollower()
    {
        this.followers++;
    }

    public void removeFollower()
    {
        if (this.followers > 0)
            this.followers--;
    }

    public void changeVisibility()
    {
        this.visibility = !this.visibility;
    }

    public ArrayList<String> getSongsNameList()
    {
        ArrayList<String> nameList = new ArrayList<>();

        for (var song : this.songs)
            nameList.add(song.getName());

        return nameList;
    }

    public int getNrOfSongs()
    {
        return this.songs.size();
    }

    public int getNrOfFollowers()
    {
        return this.followers.intValue();
    }

    public int getSongNr(Song song)
    {
        return this.songs.indexOf(song);
    }

    public Song getSong(int number)
    {
        if (number >= this.songs.size())
            return null;

        return this.songs.get(number);
    }

    public Song getSongAfter(Song song)
    {
        int i;

        i = (this.songs.indexOf(song) + 1) % this.songs.size();

        return this.songs.get(i);
    }

    public Song getSongBefore(Song song)
    {
        int i;

        i = (this.songs.indexOf(song) - 1) % this.songs.size();

        if (i == -1)
            i = this.songs.size() - 1;

        return this.songs.get(i);
    }

    public boolean isVisible()
    {
        return this.visibility;
    }

    public boolean hasSong(Song song)
    {
        return this.songs.contains(song);
    }

    public boolean isFirst(Song song)
    {
        return (this.songs.indexOf(song) == 0);
    }

    public boolean isLast(Song song)
    {
        return (this.songs.indexOf(song) == (this.songs.size() - 1));
    }

    public boolean isMatchedByFilter(FilterInput filter)
    {
        if (filter.getName() != null) {
            if (!this.name.startsWith(filter.getName()))
                return false;
        }

        if (filter.getOwner() != null) {
            if (!this.owner.equals(filter.getOwner()))
                return false;
        }

        return true;
    }
}
