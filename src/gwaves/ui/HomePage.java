package gwaves.ui;

import java.util.Comparator;
import java.util.ArrayList;

import lombok.AllArgsConstructor;

import gwaves.sample.Song;
import gwaves.collection.Playlist;
import gwaves.context.User;
import gwaves.context.NormalUser;

@AllArgsConstructor
public final class HomePage implements Page {
    private static final int MAXRESULTS = 5;

    private NormalUser owner;
    private ArrayList<Song> songs;
    private ArrayList<Playlist> playlists;
    private ArrayList<Song> recommendedSongs;
    private ArrayList<Playlist> recommendedPlaylists;
    private ArrayList<Playlist> fansPlaylists;

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

        page += "]\n\nSong recommendations:\n\t[";
        i = 0;

        for (var song : this.recommendedSongs) {
            page += song.getName();

            i++;
            if (i != this.recommendedSongs.size()) {
                page += ", ";
            }
        }

        page += "]\n\nPlaylists recommendations:\n\t[";
        i = 0;

        for (var playlist : this.recommendedPlaylists) {
            page += playlist.getName();

            i++;
            if (i != this.recommendedPlaylists.size()) {
                page += ", ";
            }
        }

        i = 0;

        for (var playlist : this.fansPlaylists) {
            page += playlist.getName();

            i++;
            if (i != this.fansPlaylists.size()) {
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

        resultsNumber = ((sorted.size() > MAXRESULTS) ? MAXRESULTS : sorted.size());
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

        resultsNumber = ((sorted.size() > MAXRESULTS) ? MAXRESULTS : sorted.size());
        sorted.retainAll(sorted.subList(0, resultsNumber));

        return sorted;
    }
}
