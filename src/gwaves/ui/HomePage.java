package gwaves.ui;

import java.util.Comparator;
import java.util.ArrayList;

import gwaves.sample.Song;
import gwaves.collection.Playlist;
import gwaves.context.User;
import gwaves.context.NormalUser;

public class HomePage implements Page {
    private NormalUser owner;
    private ArrayList<Song> songs;
    private ArrayList<Playlist> playlists;

    public HomePage(final NormalUser owner, final ArrayList<Song> songs,
                    final ArrayList<Playlist> playlists) {
        this.owner = owner;
        this.songs = songs;
        this.playlists = playlists;
    }

    public Type type() {
        return Page.Type.OFNUSER;
    }

    public User owner() {
        return this.owner;
    }

    /**
     *
     */
    public String strigify() {
        int i = 0;
        String page = "Liked songs:\n\t[";
        ArrayList<Song> sortedSongs = this.makeSortedSongs();
        ArrayList<Playlist> sortedPlaylists = this.makeSortedPlaylists();

        for (var song : sortedSongs) {
            page += song.getName();

            i++;
            if (i != sortedSongs.size()) {
                page += ", ";
            }
        }

        page += "]\n\nFollowed playlists:\n\t[";
        i = 0;

        for (var playlist : sortedPlaylists) {
            page += playlist.getName();

            i++;
            if (i != sortedPlaylists.size()) {
                page += ", ";
            }
        }

        page += "]";

        return page;
    }

    private ArrayList<Song> makeSortedSongs() {
        int resultsNumber;
        ArrayList<Song> sorted = new ArrayList<>(this.songs);

        sorted.sort(new Comparator<Song>() {
            @Override
            public int compare(final Song song1, final Song song2) {
                if (song1.getLikes() < song2.getLikes()) {
                    return 1;
                } else if (song1.getLikes() > song2.getLikes()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        resultsNumber = ((sorted.size() > 5) ? 5 : sorted.size());
        sorted.retainAll(sorted.subList(0, resultsNumber));

        return sorted;
    }

    private ArrayList<Playlist> makeSortedPlaylists() {
        int resultsNumber;
        ArrayList<Playlist> sorted = new ArrayList<>(this.playlists);

        sorted.sort(new Comparator<Playlist>() {
            @Override
            public int compare(final Playlist playlist1, final Playlist playlist2) {
                if (playlist1.getNrOfLikes() < playlist2.getNrOfLikes()) {
                    return 1;
                } else if (playlist1.getNrOfLikes() > playlist2.getNrOfLikes()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        resultsNumber = ((sorted.size() > 5) ? 5 : sorted.size());
        sorted.retainAll(sorted.subList(0, resultsNumber));

        return sorted;
    }
}
