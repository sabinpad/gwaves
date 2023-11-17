package gwaves;

import java.util.ArrayList;
import java.util.HashMap;

import gwaves.sample.Song;
import gwaves.collection.Playlist;
import gwaves.collection.Podcast;

public class AudioDataBase {
    static final AudioDataBase instance;

    HashMap<Song, Integer> library;
    HashMap<Playlist, Integer> publicPlaylists;
    // HashMap<String, Podcast> podcasts;
    ArrayList<Podcast> podcasts;

    static {
        instance = new AudioDataBase();
    }

    private AudioDataBase() {}

    public AudioDataBase getInstance()
    {
        return instance;
    }

    public void addLikeSong(Song song)
    {
        Integer nrlikes = library.get(song);

        nrlikes++;
    }

    public void rmLikeSong(Song song)
    {
        Integer nrlikes = library.get(song);

        if (nrlikes > 0) {
            nrlikes--;
        }
    }

    public void addFollowPlaylist(Playlist playlist)
    {
        Integer nrfollows = publicPlaylists.get(playlist);

        nrfollows++;
    }

    public void rmFollowPlaylist(Playlist playlist)
    {
        Integer nrfollows = publicPlaylists.get(playlist);

        if (nrfollows > 0) {
            nrfollows--;
        }
    }
}
