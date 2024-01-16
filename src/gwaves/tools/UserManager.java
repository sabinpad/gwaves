package gwaves.tools;

import java.util.List;
import java.util.ArrayList;
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
    private LinkedList<NormalUser> normalUsers;

    static {
        instance = new UserManager();
    }

    private UserManager() {
        this.lastTimestamp = 0;
        this.normalUsers = new LinkedList<>();
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
        this.normalUsers.addAll(users);
    }

    /**
     * Unload all users from the user
     */
    public void reset() {
        this.normalUsers.clear();
    }

    /**
     * Adds the specified user to the list to be managed
     *
     * @param user to be added
     */
    public void addUser(final NormalUser user) {
        this.normalUsers.add(user);
    }

    /**
     * Removes the specified user from the list to be managed
     *
     * @param user to be removed
     */
    public void rmvUser(final NormalUser user) {
        this.normalUsers.remove(user);
    }

    /**
     * Updates all users on the platform
     */
    public void updateAll(final int currentTimeStamp) {
        int timeInterval = currentTimeStamp - this.lastTimestamp;

        for (var normalUser : this.normalUsers) {
            if (normalUser.isActive()) {
                normalUser.runMusicPlayer(timeInterval);
            }
        }

        this.lastTimestamp = currentTimeStamp;
    }

    /**
     * Executes pay method on all premium users
     */
    public void payRemainingAll() {
        for (var normalUser : this.normalUsers) {
            if (normalUser.isPremium()) {
                normalUser.payRemaining();
            }
        }
    }

    /**
     *
     * @param song
     */
    private void toUnlink(final Song song) {
        for (var normalUser : this.normalUsers) {
            normalUser.checkRemoveSong(song);
        }
    }

    /**
     *
     * @param song
     */
    public void toUnlink(final Album album) {
        for (var song : album.getAudRecs()) {
            this.toUnlink(song);
        }
    }

    /**
     *
     * @param song
     */
    public void toUnlink(final Playlist playlist) {
        for (var normalUser : this.normalUsers) {
            normalUser.checkRemovePlaylist(playlist);
        }
    }

    /**
     *
     * @param song
     */
    public void toUnlink(final NormalUser normalUser) {
        for (var playlist : normalUser.getPersonalPlaylists()) {
            this.toUnlink(playlist);
        }
    }

    /**
     *
     * @param song
     */
    public void toUnlink(final Artist artist) {
        for (var album : artist.getAlbums()) {
            this.toUnlink(album);
        }
    }

    /**
     *
     * @param user to be checked
     * @return true if no user is interacting with the specified user
     */
    public boolean isSafeToRemove(final Album album) {
        for (var normalUser: this.normalUsers) {
            if (album.equals(normalUser.getListeningCollec())) {
                return false;
            }

            if (album.hasAudRec(normalUser.getListeningAudRec())) {
                return false;
            }

            if (normalUser.getListeningCollec() != null) {
                for (var audRec : normalUser.getListeningCollec().getAudRecs()) {
                    if (album.hasAudRec(audRec)) {
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
        for (var normalUser : this.normalUsers) {
            if (podcast.equals(normalUser.getListeningCollec())) {
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
    public boolean isSafeToRemove(final NormalUser normalUser) {
        ArrayList<Playlist> playlists = normalUser.getPersonalPlaylists();

        for (var otherNormalUser : this.normalUsers) {
            if (playlists.contains(otherNormalUser.getListeningCollec())) {
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

        for (var normalUser : this.normalUsers) {
            if (normalUser.getPage() == artist.getPage()) {
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

        for (var normalUser : this.normalUsers) {
            if (host.getPage() == normalUser.getPage()) {
                return false;
            }
        }

        return true;
    }
}
