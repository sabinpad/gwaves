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
    private int resultsNumber;

    public Searchbar()
    {
        this.database = DataBase.getInstance();
        this.resultsSong = new ArrayList<>();
        this.resultsPlaylist = new ArrayList<>();
        this.resultsPodcast = new ArrayList<>();
        this.resultsNumber = 0;
    }
 
    public ArrayList<String> searchSongs(Filter filter)
    {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsSong = this.database.querySongs(filter);

        this.resultsNumber = ((this.resultsSong.size() > 5) ? 5 : this.resultsSong.size());

        this.resultsSong.retainAll(this.resultsSong.subList(0, this.resultsNumber));

        for (var song : this.resultsSong)
            resultsName.add(song.getName());

        return resultsName;
    }

    public ArrayList<String> searchPlaylists(Filter filter)
    {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsPlaylist = this.database.queryPlaylists(filter);

        this.resultsNumber = ((this.resultsPlaylist.size() > 5) ? 5 : this.resultsPlaylist.size());

        this.resultsPlaylist.retainAll(this.resultsPlaylist.subList(0, this.resultsNumber));

        for (var playlist : this.resultsPlaylist)
            resultsName.add(playlist.getName());

        return resultsName;
    }

    public ArrayList<String> searchPodcasts(Filter filter)
    {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsPodcast = this.database.queryPodcasts(filter);

        this.resultsNumber = ((this.resultsPodcast.size() > 5) ? 5 : this.resultsPodcast.size());

        this.resultsPodcast.retainAll(this.resultsPodcast.subList(0, this.resultsNumber));

        for (var podcast : this.resultsPodcast)
            resultsName.add(podcast.getName());

        return resultsName;
    }

    public int getResultsNumber()
    {
        return this.resultsNumber;
    }
}
