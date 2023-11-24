package gwaves.tools;

import java.util.ArrayList;

import fileio.input.FilterInput;
import gwaves.sample.Song;
import gwaves.storage.DataBase;
import gwaves.collection.Playlist;
import gwaves.collection.Podcast;
import gwaves.context.User;

public final class Searchbar {
    private User ownerUser;

    private ArrayList<Song> resultsSong;
    private ArrayList<Playlist> resultsPlaylist;
    private ArrayList<Podcast> resultsPodcast;

    private int resultsIndex;
    private int resultsNumber;

    public Searchbar(final User ownerUser) {
        this.ownerUser = ownerUser;
        this.resultsIndex = -1;
        this.resultsNumber = 0;
    }

    /**
     * @param filter used to search for the songs
     * @return an ArrayList containg the name of the songs that have been found
     */
    public ArrayList<String> searchSongs(final FilterInput filter) {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsSong = DataBase.getInstance().querySongs(filter);
        this.resultsPlaylist = null;
        this.resultsPodcast = null;

        this.resultsNumber = ((this.resultsSong.size() > 5) ? 5 : this.resultsSong.size());
        this.resultsSong.retainAll(this.resultsSong.subList(0, this.resultsNumber));

        for (var song : this.resultsSong) {
            resultsName.add(song.getName());
        }

        this.resultsIndex = -1;

        return resultsName;
    }

    /**
     * @param filter used to search for the playlists
     * @return an ArrayList containg the name of the playlists that have been
     * found
     */
    public ArrayList<String> searchPlaylists(final FilterInput filter) {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsPlaylist = DataBase.getInstance().queryVisiblePlaylistsAndOwnedBy(filter,
                this.ownerUser.getUserName());
        this.resultsSong = null;
        this.resultsPodcast = null;

        this.resultsNumber = ((this.resultsPlaylist.size() > 5) ? 5 : this.resultsPlaylist.size());
        this.resultsPlaylist.retainAll(this.resultsPlaylist.subList(0, this.resultsNumber));

        for (var playlist : this.resultsPlaylist) {
            resultsName.add(playlist.getName());
        }

        this.resultsIndex = -1;

        return resultsName;
    }

    /**
     * @param filter used to search for the podcasts
     * @return an ArrayList containg the name of the podcasts that have been
     * found
     */
    public ArrayList<String> searchPodcasts(final FilterInput filter) {
        ArrayList<String> resultsName = new ArrayList<>();

        this.resultsPodcast = DataBase.getInstance().queryPodcasts(filter);
        this.resultsSong = null;
        this.resultsPlaylist = null;

        this.resultsNumber = ((this.resultsPodcast.size() > 5) ? 5 : this.resultsPodcast.size());
        this.resultsPodcast.retainAll(this.resultsPodcast.subList(0, this.resultsNumber));

        for (var podcast : this.resultsPodcast) {
            resultsName.add(podcast.getName());
        }

        this.resultsIndex = -1;

        return resultsName;
    }

    /**
     * @param resultIndex preffered result
     */
    public void selectResult(final int resultIndex) {
        if (resultIndex >= 0 && resultIndex < this.resultsNumber) {
            this.resultsIndex = resultIndex;
        }
    }

    /**
     * @return number of results after search
     */
    public int getResultsNumber() {
        return this.resultsNumber;
    }

    /**
     * @return name of the selected result
     */
    public String getSelectedResultName() {
        if (this.resultsSong != null) {
            return this.resultsSong.get(this.resultsIndex).getName();
        } else if (this.resultsPlaylist != null) {
            return this.resultsPlaylist.get(this.resultsIndex).getName();
        } else if (this.resultsPodcast != null) {
            return this.resultsPodcast.get(this.resultsIndex).getName();
        }

        return null;
    }

    /**
     * After the call the search results are cleared
     * @return selected song
     */
    public Song getSelectedSong() {
        Song song = null;

        if (this.resultsSong != null) {
            song = this.resultsSong.get(this.resultsIndex);
            this.resultsSong = null;
            this.resultsIndex = -1;
            this.resultsNumber = 0;
        }

        return song;
    }

    /**
     * After the call the search results are cleared
     * @return selected playlist
     */
    public Playlist getSelectedPlaylist() {
        Playlist playlist = null;

        if (this.resultsPlaylist != null) {
            playlist = this.resultsPlaylist.get(this.resultsIndex);
            this.resultsPlaylist = null;
            this.resultsIndex = -1;
            this.resultsNumber = 0;
        }

        return playlist;
    }

    /**
     * After the call the search results are cleared
     * @return selected podcast
     */
    public Podcast getSelectedPodcast() {
        Podcast podcast = null;

        if (this.resultsPodcast != null) {
            podcast = this.resultsPodcast.get(this.resultsIndex);
            this.resultsPodcast = null;
            this.resultsIndex = -1;
            this.resultsNumber = 0;
        }

        return podcast;
    }

    /**
     * @return true if no search was conducted
     */
    public boolean isEmpty() {
        return this.resultsSong == null && this.resultsPlaylist == null && this.resultsPodcast == null;
    }

    /**
     * @return true if a result was selected
     */
    public boolean isSelected() {
        return this.resultsIndex != -1;
    }

    /**
     * @return true if a search for songs was conducted
     */
    public boolean searchedForSongs() {
        return this.resultsSong != null;
    }

    /**
     * @return true if a search for playlists was conducted
     */
    public boolean searchedForPlaylists() {
        return this.resultsPlaylist != null;
    }

    /**
     * @return true if a search for podcasts was conducted
     */
    public boolean searchedForPodcasts() {
        return this.resultsPodcast != null;
    }
}
