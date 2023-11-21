package gwaves.util;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;

import fileio.input.UserInput;
import fileio.input.SongInput;
import fileio.input.PodcastInput;
import fileio.input.LibraryInput;
import gwaves.util.User;
import gwaves.sample.Song;
import gwaves.collection.Podcast;
import gwaves.collection.Playlist;

public class DataBase {
    private static final DataBase instance;

    private HashMap<String, User> users;
    private ArrayList<Song> library;
    private ArrayList<Podcast> podcasts;
    private ArrayList<Playlist> publicPlaylists;

    static {
        instance = new DataBase();
    }

    private DataBase() {
        this.users = new HashMap<>();
        this.library = new ArrayList<>();
        this.podcasts = new ArrayList<>();
        this.publicPlaylists = new ArrayList<>();
    }

    public static DataBase getInstance()
    {
        return instance;
    }

    public void loadDataBase(LibraryInput libInput)
    {
        for (var userInput : libInput.getUsers())
            this.users.put(userInput.getUsername(), new User(userInput));

        for (var songInput : libInput.getSongs())
            this.library.add(new Song(songInput));

        for (var podcastInput : libInput.getPodcasts())
            this.podcasts.add(new Podcast(podcastInput));
    }

    public void addPlaylist(Playlist playlist)
    {
        this.publicPlaylists.add(playlist);
    }

    public void removePlaylist(Playlist playlist)
    {
        this.publicPlaylists.remove(playlist);
    }

    public User queryUser(String username)
    {
        return this.users.get(username);
    }

    public ArrayList<Song> querySongs(Filter filter)
    {
        ArrayList<Song> result = new ArrayList<>();

        for (var song : this.library)
            if (song.isMatchedByFilter(filter))
                result.add(song);

        return result;
    }

    public ArrayList<Playlist> queryPlaylists(Filter filter)
    {
        ArrayList<Playlist> result = new ArrayList<>();

        for (var playlist : this.publicPlaylists)
            if (playlist.isVisible() && playlist.isMatchedByFilter(filter))
                result.add(playlist);

        return result;
    }

    public ArrayList<Podcast> queryPodcasts(Filter filter)
    {
        ArrayList<Podcast> result = new ArrayList<>();

        for (var podcast : this.podcasts)
            if (podcast.isMatchedByFilter(filter))
                result.add(podcast);

        return result;
    }

    public ArrayList<Song> queryTop5Songs()
    {
        ArrayList<Song> result = new ArrayList<>(this.library);

        result.sort(new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2)
            {
                if (song1.getNrOfLikes() < song2.getNrOfLikes())
                    return -1;
                else if (song1.getNrOfLikes() > song2.getNrOfLikes())
                    return 1;
                else
                    return 0;
            }
        });

        result.retainAll(result.subList(0, 5));

        return result;
    }

    public ArrayList<Playlist> queryTop5Playlists()
    {
        ArrayList<Playlist> result = new ArrayList<>(this.publicPlaylists);

        result.sort(new Comparator<Playlist>() {
            @Override
            public int compare(Playlist playlist1, Playlist playlist2)
            {
                if (playlist1.getNrOfFollowers() < playlist2.getNrOfFollowers())
                    return -1;
                else if (playlist1.getNrOfFollowers() > playlist2.getNrOfFollowers())
                    return 1;
                else
                    return 0;
            }
        });

        result.retainAll(result.subList(0, 5));

        return result;
    }
}
