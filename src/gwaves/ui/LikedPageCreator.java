package gwaves.ui;

import java.util.ArrayList;

import gwaves.sample.Song;
import gwaves.collection.Playlist;

public class LikedPageCreator implements PageCreator {
    private ArrayList<Song> songs;
    private ArrayList<Playlist> playlists;

    public LikedPageCreator(final ArrayList<Song> songs, final ArrayList<Playlist> playlists) {
        this.songs = songs;
        this.playlists = playlists;
    }

    /**
     *
     */
    public String createPage() {
        int i = 0;
        String page = "Liked songs:\n\t[";

        for (var song : this.songs) {
            page += (song.getName() + " - " + song.getArtist());

            i++;
            if (i != songs.size()) {
                page += ", ";
            }
        }

        page += "]\n\nFollowed playlists:\n\t[";
        i = 0;

        for (var playlist : this.playlists) {
            page += (playlist.getName() + " - " + playlist.getOwner());

            i++;
            if (i != playlists.size()) {
                page += ", ";
            }
        }

        page += "]";

        return page;
    }
}
