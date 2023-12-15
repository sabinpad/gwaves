package gwaves.storage;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;

import fileio.input.FilterInput;
import fileio.input.LibraryInput;

import gwaves.context.*;

import gwaves.sample.Song;
import gwaves.collection.Playlist;
import gwaves.collection.Album;
import gwaves.collection.Podcast;

public final class DataBase {
    private static DataBase instance;

    private HashMap<String, User> users;
    private HashMap<String, Artist> artists;
    private HashMap<String, Host> hosts;

    private ArrayList<Song> library;
    private ArrayList<Playlist> playlists;
    private ArrayList<Album> albums;
    private ArrayList<Podcast> podcasts;

    static {
        instance = new DataBase();
    }

    private DataBase() {
        this.users = new HashMap<>();
        this.library = new ArrayList<>();
        this.podcasts = new ArrayList<>();
        this.playlists = new ArrayList<>();
    }

    /**
     * @return
     */
    public static void changeInstance() {
        instance = new DataBase();
    }

    /**
     * @return unique database instance
     */
    public static DataBase getInstance() {
        return instance;
    }

    /**
     * @param libInput class to load from
     */
    public void loadDataBase(final LibraryInput libInput) {
        for (var userInput : libInput.getUsers()) {
            this.users.put(userInput.getUsername(), new User(userInput));
        }

        for (var songInput : libInput.getSongs()) {
            this.library.add(new Song(songInput));
        }

        for (var podcastInput : libInput.getPodcasts()) {
            this.podcasts.add(new Podcast(podcastInput));
        }
    }

    /**
     * @param playlist to add
     */
    public void addUser(final User user) {
        this.users.put(user.getUserName(), user);
    }

    /**
     * @param playlist to remove
     */
    public void removeUser(final User user) {
        users.remove(user.getUserName());
    }

    /**
     * @param playlist to add
     */
    public void addArtist(final Artist artist) {
        this.artists.put(artist.getUserName(), artist);
    }

    /**
     * @param playlist to remove
     */
    public void removeArtist(final Artist artist) {
        artists.remove(artist.getUserName());
    }

    /**
     * @param playlist to add
     */
    public void addHost(final Host host) {
        this.hosts.put(host.getUserName(), host);
    }

    /**
     * @param playlist to remove
     */
    public void removeHost(final Host host) {
        hosts.remove(host.getUserName());
    }

    /**
     * @param playlist to add
     */
    public void addSong(final Song song) {
        this.library.add(song);
    }

    /**
     * @param playlist to remove
     */
    public void removeSong(final Song song) {
        this.library.remove(song);
    }

    /**
     * @param playlist to add
     */
    public void addPlaylist(final Playlist playlist) {
        this.playlists.add(playlist);
    }

    /**
     * @param playlist to remove
     */
    public void removePlaylist(final Playlist playlist) {
        this.playlists.remove(playlist);
    }

    /**
     * @param playlist to add
     */
    public void addAlbum(final Album album) {
        this.albums.add(album);
    }

    /**
     * @param playlist to remove
     */
    public void removeAlbum(final Album album) {
        this.albums.remove(album);
    }

    /**
     * @param playlist to add
     */
    public void addPodcast(final Podcast podcast) {
        this.podcasts.add(podcast);
    }

    /**
     * @param playlist to remove
     */
    public void removePodcast(final Podcast podcast) {
        this.podcasts.remove(podcast);
    }

    /**
     * @param username name of user
     * @return user queried by name
     */
    public User queryUser(final String username) {
        if (this.users.containsKey(username))
            return this.users.get(username);

        if (this.artists.containsKey(username))
            return this.artists.get(username);

        if (this.hosts.containsKey(username))
            return this.hosts.get(username);

        return null;
    }

    /**
     * @param username name of artist
     * @return artist queried by name
     */
    public Artist queryArtist(final String username) {
        return this.artists.get(username);
    }

    /**
     * @param username name of host
     * @return host queried by name
     */
    public Host queryHost(final String username) {
        return this.hosts.get(username);
    }

    public ArrayList<User> queryAllUsers() {
        ArrayList<User> list = new ArrayList<>();

        for (var entry : this.users.entrySet())
            list.add(entry.getValue());

        return list;
    }

    /**
     * @param filter used to match
     * @return ArrayList of matched songs
     */
    public ArrayList<Song> querySongs(final FilterInput filter) {
        ArrayList<Song> result = new ArrayList<>();

        for (var song : this.library) {
            if (song.isMatchedByFilter(filter)) {
                result.add(song);
            }
        }

        return result;
    }

    /**
     * Returns an ArrayList of playlists that are either public(visible) or
     * are owned by the user with name {@code owner}
     * @param filter used to match
     * @param owner name of playlist owner
     * @return ArrayList of matched playlists
     */
    public ArrayList<Playlist> queryVisiblePlaylistsAndOwnedBy(final FilterInput filter, final String owner) {
        ArrayList<Playlist> result = new ArrayList<>();

        for (var playlist : this.playlists) {
            if (playlist.isMatchedByFilter(filter)) {
                if (playlist.isVisible()
                    || (!playlist.isVisible() && owner.equals(playlist.getOwner()))) {
                    result.add(playlist);
                }
            }
        }

        return result;
    }

    /**
     * @param filter used to match
     * @return ArrayList of matched podcasts
     */
    public ArrayList<Podcast> queryPodcasts(final FilterInput filter) {
        ArrayList<Podcast> result = new ArrayList<>();

        for (var podcast : this.podcasts) {
            if (podcast.isMatchedByFilter(filter)) {
                result.add(podcast);
            }
        }

        return result;
    }

    /**
     * @return ArrayList containing the names of the top 5 most liked songs
     */
    public ArrayList<String> getTop5SongsName() {
        int resultsNumber;
        ArrayList<Song> topSongsList = new ArrayList<>(this.library);
        ArrayList<String> result = new ArrayList<>();

        topSongsList.sort(new Comparator<Song>() {
            @Override
            public int compare(final Song song1, final Song song2) {
                if (song1.getNrOfLikes() < song2.getNrOfLikes()) {
                    return 1;
                } else if (song1.getNrOfLikes() > song2.getNrOfLikes()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        resultsNumber = ((topSongsList.size() > 5) ? 5 : topSongsList.size());
        topSongsList.retainAll(topSongsList.subList(0, resultsNumber));

        for (var song : topSongsList) {
            result.add(song.getName());
        }

        return result;
    }

    /**
     * @return ArrayList containing the names of the top 5 most followed
     * playlists
     */
    public ArrayList<String> getTop5PlaylistsName() {
        int resultsNumber;
        ArrayList<Playlist> topPlaylistsList = new ArrayList<>(this.playlists);
        ArrayList<String> result = new ArrayList<>();

        topPlaylistsList.sort(new Comparator<Playlist>() {
            @Override
            public int compare(final Playlist playlist1, final Playlist playlist2) {
                if (playlist1.getNrOfFollowers() < playlist2.getNrOfFollowers()) {
                    return 1;
                } else if (playlist1.getNrOfFollowers() > playlist2.getNrOfFollowers()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        resultsNumber = ((topPlaylistsList.size() > 5) ? 5 : topPlaylistsList.size());
        topPlaylistsList.retainAll(topPlaylistsList.subList(0, resultsNumber));

        for (var playlist : topPlaylistsList) {
            result.add(playlist.getName());
        }

        return result;
    }
}
