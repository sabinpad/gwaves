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

    public ArrayList<String> searchPlaylists(Filter filter)
    {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsPlaylist = this.database.queryPlaylists(filter);
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

    public Song getSelectedSong()
    {
        if (this.resultsType == 1)
            return this.resultsSong.get(resultsIndex);

        return null;
    }

    public Playlist getSelectedPlaylist()
    {
        if (this.resultsType == 2)
            return this.resultsPlaylist.get(resultsIndex);
           
        return null;
    }

    public Podcast getSelectedPodcast()
    {
        if (this.resultsType == 3)
            return this.resultsPodcast.get(resultsIndex);
           
        return null;
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
