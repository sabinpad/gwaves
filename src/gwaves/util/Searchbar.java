package gwaves.util;

import java.util.ArrayList;

import fileio.input.FilterInput;
import gwaves.sample.Song;
import gwaves.collection.Playlist;
import gwaves.collection.Podcast;

public class Searchbar {
    private User ownerUser;

    private ArrayList<Song> resultsSong;
    private ArrayList<Playlist> resultsPlaylist;
    private ArrayList<Podcast> resultsPodcast;

    private int resultsIndex;
    private int resultsNumber;

    public Searchbar(User ownerUser)
    {
        this.ownerUser = ownerUser;
        this.resultsIndex = -1;
        this.resultsNumber = 0;
    }
 
    public ArrayList<String> searchSongs(FilterInput filter)
    {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsSong = DataBase.getInstance().querySongs(filter);
        this.resultsPlaylist = null;
        this.resultsPodcast = null;

        this.resultsNumber = ((this.resultsSong.size() > 5) ? 5 : this.resultsSong.size());
        this.resultsSong.retainAll(this.resultsSong.subList(0, this.resultsNumber));

        for (var song : this.resultsSong)
            resultsName.add(song.getName());

        this.resultsIndex = -1;

        return resultsName;
    }

    public ArrayList<String> searchPlaylists(FilterInput filter)
    {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsPlaylist = DataBase.getInstance().queryVisiblePlaylistsAndOwnedBy(filter, this.ownerUser.getUserName());
        this.resultsSong = null;
        this.resultsPodcast = null;

        this.resultsNumber = ((this.resultsPlaylist.size() > 5) ? 5 : this.resultsPlaylist.size());
        this.resultsPlaylist.retainAll(this.resultsPlaylist.subList(0, this.resultsNumber));

        for (var playlist : this.resultsPlaylist)
            resultsName.add(playlist.getName());

        this.resultsIndex = -1;

        return resultsName;
    }

    public ArrayList<String> searchPodcasts(FilterInput filter)
    {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsPodcast = DataBase.getInstance().queryPodcasts(filter);
        this.resultsSong = null;
        this.resultsPlaylist = null;

        this.resultsNumber = ((this.resultsPodcast.size() > 5) ? 5 : this.resultsPodcast.size());
        this.resultsPodcast.retainAll(this.resultsPodcast.subList(0, this.resultsNumber));

        for (var podcast : this.resultsPodcast)
            resultsName.add(podcast.getName());

        this.resultsIndex = -1;

        return resultsName;
    }

    public void selectResult(int resultIndex)
    {
        if (resultIndex >= 0 && resultIndex < this.resultsNumber)
            this.resultsIndex = resultIndex;
    }

    public int getResultsNumber()
    {
        return this.resultsNumber;
    }

    public String getSelectedResultName()
    {
        if (this.resultsSong != null)
            return this.resultsSong.get(this.resultsIndex).getName();
        else if (this.resultsPlaylist != null)
            return this.resultsPlaylist.get(this.resultsIndex).getName();
        else if (this.resultsPodcast != null)
            return this.resultsPodcast.get(this.resultsIndex).getName();

        return null;
    }

    public Song getSelectedSong()
    {
        Song song = null;

        if (this.resultsSong != null) {
            song = this.resultsSong.get(this.resultsIndex);
            this.resultsSong = null;
            this.resultsIndex = -1;
            this.resultsNumber = 0;
        }

        return song;
    }

    public Playlist getSelectedPlaylist()
    {
        Playlist playlist = null;

        if (this.resultsPlaylist != null) {
            playlist =  this.resultsPlaylist.get(this.resultsIndex);
            this.resultsPlaylist = null;
            this.resultsIndex = -1;
            this.resultsNumber = 0;
        }
           
        return playlist;
    }

    public Podcast getSelectedPodcast()
    {
        Podcast podcast = null;

        if (this.resultsPodcast != null) {
            podcast = this.resultsPodcast.get(this.resultsIndex);
            this.resultsPodcast = null;
            this.resultsIndex = -1;
            this.resultsNumber = 0;
        }
           
        return podcast;
    }

    public boolean isEmpty()
    {
        return this.resultsSong == null && this.resultsPlaylist == null && this.resultsPodcast == null;
    }

    public boolean isSelected()
    {
        return this.resultsIndex != -1;
    }

    public boolean searchedForSongs()
    {
        return this.resultsSong != null;
    }

    public boolean searchedForPlaylists()
    {
        return this.resultsPlaylist != null;
    }

    public boolean searchedForPodcasts()
    {
        return this.resultsPodcast != null;
    }
}
