package gwaves.util;

import java.util.ArrayList;

import gwaves.sample.Song;
import gwaves.collection.Playlist;
import gwaves.collection.Podcast;

public class Searchbar {
    private DataBase database;

    private ArrayList<Song> resultsSong;
    private ArrayList<Playlist> resultsPlaylist;
    private ArrayList<Podcast> resultsPodcast;

    private int resultsType;
    private int resultsIndex;
    private int resultsNumber;

    public Searchbar()
    {
        this.database = DataBase.getInstance();
        this.resultsSong = new ArrayList<>();
        this.resultsPlaylist = new ArrayList<>();
        this.resultsPodcast = new ArrayList<>();
        this.resultsType = 0;
        this.resultsIndex = -1;
        this.resultsNumber = 0;
    }
 
    public ArrayList<String> searchSongs(Filter filter)
    {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsSong = this.database.querySongs(filter);
        this.resultsPlaylist.clear();
        this.resultsPodcast.clear();

        this.resultsNumber = ((this.resultsSong.size() > 5) ? 5 : this.resultsSong.size());
        this.resultsSong.retainAll(this.resultsSong.subList(0, this.resultsNumber));

        for (var song : this.resultsSong)
            resultsName.add(song.getName());

        this.resultsType = 1;
        this.resultsIndex = -1;

        return resultsName;
    }

    public ArrayList<String> searchPlaylistsAndOwnedBy(Filter filter, String owner)
    {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsPlaylist = this.database.queryPlaylistsAndOwnedBy(filter, owner);
        this.resultsSong.clear();
        this.resultsPodcast.clear();

        this.resultsNumber = ((this.resultsPlaylist.size() > 5) ? 5 : this.resultsPlaylist.size());
        this.resultsPlaylist.retainAll(this.resultsPlaylist.subList(0, this.resultsNumber));

        for (var playlist : this.resultsPlaylist)
            resultsName.add(playlist.getName());

        this.resultsType = 2;
        this.resultsIndex = -1;

        return resultsName;
    }

    public ArrayList<String> searchPodcasts(Filter filter)
    {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsPodcast = this.database.queryPodcasts(filter);
        this.resultsSong.clear();
        this.resultsPlaylist.clear();

        this.resultsNumber = ((this.resultsPodcast.size() > 5) ? 5 : this.resultsPodcast.size());
        this.resultsPodcast.retainAll(this.resultsPodcast.subList(0, this.resultsNumber));

        for (var podcast : this.resultsPodcast)
            resultsName.add(podcast.getName());

        this.resultsType = 3;
        this.resultsIndex = -1;

        return resultsName;
    }

    public void selectResult(int number)
    {
        if (this.resultsNumber > 0 && number <= this.resultsNumber)
            this.resultsIndex = number - 1;
    }

    public int getResultsNumber()
    {
        return this.resultsNumber;
    }

    public String getSelectedName()
    {
        if (this.resultsType == 1)
            return this.resultsSong.get(this.resultsIndex).getName();
        else if (this.resultsType == 2)
            return this.resultsPlaylist.get(this.resultsIndex).getName();
        else if (this.resultsType == 3)
            return this.resultsPodcast.get(this.resultsIndex).getName();

        return null;
    }

    public Song getSelectedSong()
    {
        Song song = null;

        if (this.resultsType == 1) {
            song = this.resultsSong.get(this.resultsIndex);
            this.resultsIndex = -1;
        }

        return song;
    }

    public Playlist getSelectedPlaylist()
    {
        Playlist playlist = null;

        if (this.resultsType == 2) {
            playlist =  this.resultsPlaylist.get(this.resultsIndex);
            this.resultsIndex = -1;
        }
           
        return playlist;
    }

    public Podcast getSelectedPodcast()
    {
        Podcast podcast = null;

        if (this.resultsType == 3) {
            podcast = this.resultsPodcast.get(this.resultsIndex);
            this.resultsIndex = -1;
        }
           
        return podcast;
    }

    public boolean isSelected()
    {
        return this.resultsIndex != -1;
    }

    public boolean searchedForSongs()
    {
        return this.resultsType == 1;
    }

    public boolean searchedForPlaylists()
    {
        return this.resultsType == 2;
    }

    public boolean searchedForPodcasts()
    {
        return this.resultsType == 3;
    }
}
