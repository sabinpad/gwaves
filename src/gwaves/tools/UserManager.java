package gwaves.tools;

import java.util.List;
import java.util.LinkedList;

import gwaves.sample.Song;
import gwaves.collection.Playlist;
import gwaves.collection.Album;
import gwaves.collection.Podcast;
import gwaves.context.NormalUser;
import gwaves.context.Artist;
import gwaves.context.Host;

public final class UserManager {
    private static UserManager instance;

    private int lastTimestamp;
    private LinkedList<NormalUser> normalusers;

    static {
        instance = new UserManager();
    }

    private UserManager() {
        this.lastTimestamp = 0;
        this.normalusers = new LinkedList<>();
    }

    /**
     *
     */
    public static void changeInstance() {
        instance = new UserManager();
    }

    /**
     * @return unique instance
     */
    public static UserManager getInstance() {
        return instance;
    }

    /**
     * Load users into the manager
     *
     * @param users list of users to be loaded
     */
    public void loadUsers(final List<NormalUser> users) {
        this.normalusers.addAll(users);
    }

    /**
     * Unload all users from the user
     */
    public void reset() {
        this.normalusers.clear();
    }

    /**
     * Adds the specified user to the list to be managed
     *
     * @param user to be added
     */
    public void addUser(final NormalUser user) {
        this.normalusers.add(user);
    }

    /**
     * Removes the specified user from the list to be managed
     *
     * @param user to be removed
     */
    public void rmvUser(final NormalUser user) {
        this.normalusers.remove(user);
    }

    /**
     * Updates all users on the platform
     */
    public void updateAll(final int currentTimeStamp) {
        int timeInterval = currentTimeStamp - this.lastTimestamp;

        for (var normaluser : this.normalusers) {
            if (normaluser.isActive()) {
                normaluser.runMusicPlayer(timeInterval);
            }
        }

        this.lastTimestamp = currentTimeStamp;
    }

    /**
     *
     * @param user to be checked
     * @return true if no user is interacting with the specified user
     */
    public boolean isSafeToRemove(final Album album) {
        for (var normaluser : this.normalusers) {
            if (album.getName().equals(normaluser.getListeningCollName())) {
                return false;
            }

            if (album.hasSongWithName(normaluser.getListeningSongName())) {
                return false;
            }

            if (normaluser.getListeningSongs() != null) {
                for (var song : normaluser.getListeningSongs()) {
                    if (album.hasSongWithName(song.getName())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     *
     * @param user to be checked
     * @return true if no user is interacting with the specified user
     */
    public boolean isSafeToRemove(final Podcast podcast) {
        for (var normaluser : this.normalusers) {
            if (podcast.getName().equals(normaluser.getListeningCollName())) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param user to be checked
     * @return true if no user is interacting with the specified user
     */
    public boolean isSafeToRemove(final NormalUser normaluser) {
        for (var othernormaluser : this.normalusers) {
            if (normaluser.hasPlaylistWithName(othernormaluser.getListeningCollName())) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @param user to be checked
     * @return true if no user is interacting with the specified user
     */
    public boolean isSafeToRemove(final Artist artist) {
        for (var album : artist.getAlbums()) {
            if (!this.isSafeToRemove(album)) {
                return false;
            }
        }

        for (var normaluser : this.normalusers) {
            if (normaluser.getPageCreator() == artist.getPageCreator()) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @param user to be checked
     * @return true if no user is interacting with the specified user
     */
    public boolean isSafeToRemove(final Host host) {
        for (var podcast : host.getPodcasts()) {
            if (!this.isSafeToRemove(podcast)) {
                return false;
            }
        }

        for (var normaluser : this.normalusers) {
            if (host.getPageCreator() == normaluser.getPageCreator()) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @param song
     */
    private void toUnlink(Song song) {
        for (var normaluser : this.normalusers) {
            normaluser.checkRemoveSong(song);
        }
    }

    /**
     *
     * @param song
     */
    public void toUnlink(Album album) {
        // for (var normaluser : this.normalusers) {
        //     for (var song : album.getSongs())
        //         normaluser.checkRemoveSong(song);
        // }
        for (var song : album.getSongs()) {
            this.toUnlink(song);
        }
    }

    /**
     *
     * @param song
     */
    public void toUnlink(Playlist playlist) {
        for (var normaluser : this.normalusers) {
            normaluser.checkRemovePlaylist(playlist);
        } 
    }

    /**
     *
     * @param song
     */
    public void toUnlink(NormalUser normaluser) {
        for (var playlist : normaluser.getOwnPlaylists()) {
            this.toUnlink(playlist);
        }
    }

    /**
     *
     * @param song
     */
    public void toUnlink(Artist artist) {
        for (var album : artist.getAlbums()) {
            this.toUnlink(album);
        }
    }
}
