package gwaves.storage;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;

import fileio.input.FilterInput;
import fileio.input.LibraryInput;

import gwaves.sample.Song;
import gwaves.collection.Podcast;
import gwaves.context.User;
import gwaves.collection.Playlist;

public final class DataBase {
    private static DataBase instance;

    private HashMap<String, User> users;
    private ArrayList<Song> library;
    private ArrayList<Podcast> podcasts;
    private ArrayList<Playlist> playlists;

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
     * @return
     */
    public static DataBase getInstance() {
        return instance;
    }

    /**
     * @param libInput
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
     * @param playlist
     */
    public void addPlaylist(final Playlist playlist) {
        this.playlists.add(playlist);
    }

    /**
     * @param playlist
     */
    public void removePlaylist(final Playlist playlist) {
        this.playlists.remove(playlist);
    }

    /**
     * @param username
     * @return
     */
    public User queryUser(final String username) {
        return this.users.get(username);
    }

    /**
     * @param filter
     * @return
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
     * @param filter
     * @param owner
     * @return
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
     * @param filter
     * @return
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
     * @return
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
     * @return
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
