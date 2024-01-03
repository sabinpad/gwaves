package gwaves.tools;

import java.util.ArrayList;

import fileio.input.FilterInput;
import gwaves.storage.DataBase;

import gwaves.sample.Song;
import gwaves.collection.Playlist;
import gwaves.collection.Album;
import gwaves.collection.Podcast;

import gwaves.context.NormalUser;
import gwaves.context.Artist;
import gwaves.context.Host;

public final class Searchbar {
    private NormalUser ownerUser;

    private ArrayList<Song> resultsSong;
    private ArrayList<Playlist> resultsPlaylist;
    private ArrayList<Album> resultsAlbum;
    private ArrayList<Podcast> resultsPodcast;

    private ArrayList<Artist> resultsArtist;
    private ArrayList<Host> resultsHost;

    private int resultsIndex;
    private int resultsNumber;

    public Searchbar(final NormalUser ownerUser) {
        this.ownerUser = ownerUser;
        this.resultsIndex = -1;
        this.resultsNumber = 0;
    }

    private void clearResults() {
        this.resultsSong = null;
        this.resultsPlaylist = null;
        this.resultsAlbum = null;
        this.resultsPodcast = null;
        this.resultsArtist = null;
        this.resultsHost = null;
    }

    /**
     * @param filter used to search for the songs
     * @return an ArrayList containg the name of the songs that have been found
     */
    public ArrayList<String> searchSongs(final FilterInput filter) {
        ArrayList<String> resultsName = new ArrayList<>();

        this.clearResults();
        this.resultsSong = DataBase.getInstance().querySongs(filter);

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
     *         found
     */
    public ArrayList<String> searchPlaylists(final FilterInput filter) {
        ArrayList<String> resultsName = new ArrayList<>();

        this.clearResults();
        this.resultsPlaylist = DataBase.getInstance().queryVisiblePlaylistsAndOwnedBy(filter,
                this.ownerUser.getUsername());

        this.resultsNumber = ((this.resultsPlaylist.size() > 5) ? 5 : this.resultsPlaylist.size());
        this.resultsPlaylist.retainAll(this.resultsPlaylist.subList(0, this.resultsNumber));

        for (var playlist : this.resultsPlaylist) {
            resultsName.add(playlist.getName());
        }

        this.resultsIndex = -1;

        return resultsName;
    }

    /**
     * @param filter used to search for the playlists
     * @return an ArrayList containg the name of the playlists that have been
     *         found
     */
    public ArrayList<String> searchAlbums(final FilterInput filter) {
        ArrayList<String> resultsName = new ArrayList<>();

        this.clearResults();
        this.resultsAlbum = DataBase.getInstance().queryAlbums(filter);

        this.resultsNumber = ((this.resultsAlbum.size() > 5) ? 5 : this.resultsAlbum.size());
        this.resultsAlbum.retainAll(this.resultsAlbum.subList(0, this.resultsNumber));

        for (var album : this.resultsAlbum) {
            resultsName.add(album.getName());
        }

        this.resultsIndex = -1;

        return resultsName;
    }

    /**
     * @param filter used to search for the podcasts
     * @return an ArrayList containg the name of the podcasts that have been
     *         found
     */
    public ArrayList<String> searchPodcasts(final FilterInput filter) {
        ArrayList<String> resultsName = new ArrayList<>();

        this.clearResults();
        this.resultsPodcast = DataBase.getInstance().queryPodcasts(filter);

        this.resultsNumber = ((this.resultsPodcast.size() > 5) ? 5 : this.resultsPodcast.size());
        this.resultsPodcast.retainAll(this.resultsPodcast.subList(0, this.resultsNumber));

        for (var podcast : this.resultsPodcast) {
            resultsName.add(podcast.getName());
        }

        this.resultsIndex = -1;

        return resultsName;
    }

    /**
     *
     * @param filter
     * @return
     */
    public ArrayList<String> searchArtists(final FilterInput filter) {
        ArrayList<String> resultsName = new ArrayList<>();

        this.clearResults();
        this.resultsArtist = DataBase.getInstance().queryArtistsWithFilter(filter);

        this.resultsNumber = ((this.resultsArtist.size() > 5) ? 5 : this.resultsArtist.size());
        this.resultsArtist.retainAll(this.resultsArtist.subList(0, this.resultsNumber));

        for (var artist : this.resultsArtist) {
            resultsName.add(artist.getUsername());
        }

        this.resultsIndex = -1;

        return resultsName;
    }

    /**
     *
     * @param filter
     * @return
     */
    public ArrayList<String> searchHosts(final FilterInput filter) {
        ArrayList<String> resultsName = new ArrayList<>();

        this.clearResults();
        this.resultsHost = DataBase.getInstance().queryHostsWithFilter(filter);

        this.resultsNumber = ((this.resultsHost.size() > 5) ? 5 : this.resultsHost.size());
        this.resultsHost.retainAll(this.resultsHost.subList(0, this.resultsNumber));

        for (var host : this.resultsHost) {
            resultsName.add(host.getUsername());
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
        } else if (this.resultsAlbum != null) {
            return this.resultsAlbum.get(this.resultsIndex).getName();
        } else if (this.resultsPodcast != null) {
            return this.resultsPodcast.get(this.resultsIndex).getName();
        } else if (this.resultsArtist != null) {
            return this.resultsArtist.get(this.resultsIndex).getUsername();
        } else if (this.resultsHost != null) {
            return this.resultsHost.get(this.resultsIndex).getUsername();
        }

        return null;
    }

    /**
     * After the call the search results are cleared
     *
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
     *
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
     *
     * @return
     */
    public Playlist getPlaylistForFollow() {
        return this.resultsPlaylist.get(this.resultsIndex);
    }

    /**
     * After the call the search results are cleared
     *
     * @return selected album
     */
    public Album getSelectedAlbum() {
        Album album = null;

        if (this.resultsAlbum != null) {
            album = this.resultsAlbum.get(this.resultsIndex);
            this.resultsAlbum = null;
            this.resultsIndex = -1;
            this.resultsNumber = 0;
        }

        return album;
    }

    /**
     * After the call the search results are cleared
     *
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
     * After the call the search results are cleared
     *
     * @return selected artist
     */
    public Artist getSelectedArtist() {
        Artist artist = null;

        if (this.resultsArtist != null) {
            artist = this.resultsArtist.get(this.resultsIndex);
            this.resultsArtist = null;
            this.resultsIndex = -1;
            this.resultsNumber = 0;
        }

        return artist;
    }

    /**
     * After the call the search results are cleared
     *
     * @return selected host
     */
    public Host getSelectedHost() {
        Host host = null;

        if (this.resultsHost != null) {
            host = this.resultsHost.get(this.resultsIndex);
            this.resultsHost = null;
            this.resultsIndex = -1;
            this.resultsNumber = 0;
        }

        return host;
    }

    /**
     * @return true if no search was conducted
     */
    public boolean isEmpty() {
        return this.resultsSong == null
                && this.resultsPlaylist == null
                && this.resultsAlbum == null
                && this.resultsPodcast == null
                && this.resultsArtist == null
                && this.resultsHost == null;
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
     * @return true if a search for albums was conducted
     */
    public boolean searchedForAlbums() {
        return this.resultsAlbum != null;
    }

    /**
     * @return true if a search for podcasts was conducted
     */
    public boolean searchedForPodcasts() {
        return this.resultsPodcast != null;
    }

    /**
     * @return true if a search for artists was conducted
     */
    public boolean searchedForArtists() {
        return this.resultsArtist != null;
    }

    /**
     * @return true if a search for hosts was conducted
     */
    public boolean searchedForHosts() {
        return this.resultsHost != null;
    }
}
