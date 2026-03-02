package dev.sabin.gwaves.model.ui;

import java.util.ArrayList;

import dev.sabin.gwaves.model.sample.Song;
import dev.sabin.gwaves.model.collection.Playlist;
import dev.sabin.gwaves.model.context.User;
import dev.sabin.gwaves.model.context.NormalUser;

public final class LikedPage implements Page {
    private NormalUser owner;
    private ArrayList<Song> songs;
    private ArrayList<Playlist> playlists;

    public LikedPage(final NormalUser owner,
                     final ArrayList<Song> songs, final ArrayList<Playlist> playlists) {
        this.owner = owner;
        this.songs = songs;
        this.playlists = playlists;
    }

    /**
     * @return type of page (useful for casting owner)
     */
    public Type type() {
        return Page.Type.OFNUSER;
    }

    /**
     * @return owner user of the page
     */
    public User owner() {
        return this.owner;
    }

    /**
     * @return the contens of the page in String format
     */
    public String strigify() {
        int i = 0;
        String page = "Liked songs:\n\t[";

        for (var song : this.songs) {
            page += (song.getName() + " - " + song.getArtist().getUsername());

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
