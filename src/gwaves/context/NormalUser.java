package gwaves.context;

import java.util.ArrayList;

import fileio.input.UserInput;
import fileio.input.FilterInput;
import fileio.output.MusicPlayerStatusOutput;
import fileio.output.PlaylistOutput;

import gwaves.sample.Song;
import gwaves.storage.DataBase;
import gwaves.tools.Musicplayer;
import gwaves.tools.Searchbar;
import gwaves.ui.HomePageCreator;
import gwaves.ui.LikedPageCreator;
import gwaves.ui.PageCreator;
import gwaves.collection.Playlist;

public final class NormalUser extends User {
    private ArrayList<Song> likedSongs;
    private ArrayList<Playlist> personalPlaylists;
    private ArrayList<Playlist> followedPlaylists;

    private Searchbar searchbar;
    private Musicplayer musicplayer;

    private boolean active;

    private PageCreator loadedCreator;

    private HomePageCreator homeCreator;
    private LikedPageCreator likedCreator;

    /**
     * Default Constructor
     */
    public NormalUser(final String username, final int age, final String city) {
        super(username, age, city);
        this.personalPlaylists = new ArrayList<>();
        this.followedPlaylists = new ArrayList<>();
        this.likedSongs = new ArrayList<>();
        this.searchbar = new Searchbar(this);
        this.musicplayer = new Musicplayer();
        this.commandMessage = null;
        this.active = true;
        this.homeCreator = new HomePageCreator(likedSongs, followedPlaylists);
        this.likedCreator = new LikedPageCreator(likedSongs, followedPlaylists);
        this.loadedCreator = this.homeCreator;
    }

    /**
     * Default Constructor
     */
    public NormalUser(final UserInput userInput) {
        super(userInput);
        this.personalPlaylists = new ArrayList<>();
        this.followedPlaylists = new ArrayList<>();
        this.likedSongs = new ArrayList<>();
        this.searchbar = new Searchbar(this);
        this.musicplayer = new Musicplayer();
        this.commandMessage = null;
        this.active = true;
        this.homeCreator = new HomePageCreator(likedSongs, followedPlaylists);
        this.likedCreator = new LikedPageCreator(likedSongs, followedPlaylists);
        this.loadedCreator = this.homeCreator;
    }

    /**
     *
     * @param timeInterval
     */
    public void runMusicPlayer(final int timeInterval) {
        this.musicplayer.playFor(timeInterval);
    }

    /**
     *
     * @param type
     * @param filter
     * @return
     */
    public ArrayList<String> doSearch(final String type, final FilterInput filter) {
        ArrayList<String> results = null;

        this.musicplayer.unloadCurrent();

        switch (type) {
            case "song":
                results = this.searchbar.searchSongs(filter);
                break;
            case "playlist":
                results = this.searchbar.searchPlaylists(filter);
                break;
            case "album":
                results = this.searchbar.searchAlbums(filter);
                break;
            case "podcast":
                results = this.searchbar.searchPodcasts(filter);
                break;
            case "artist":
                results = this.searchbar.searchArtists(filter);
                break;
            case "host":
                results = this.searchbar.searchHosts(filter);
                break;
            default:
                break;
        }

        this.commandMessage = "Search returned " + this.searchbar.getResultsNumber() + " results";

        return results;
    }

    /**
     *
     * @param itemNumber
     */
    public void doSelect(final int itemNumber) {
        if (this.searchbar.isEmpty()) {
            this.commandMessage = "Please conduct a search before making a selection.";
            return;
        }

        if (itemNumber > this.searchbar.getResultsNumber()) {
            this.commandMessage = "The selected ID is too high.";
            return;
        }

        this.searchbar.selectResult(itemNumber - 1);

        if (this.searchbar.searchedForArtists()) {
            this.commandMessage = "Successfully selected " + this.searchbar.getSelectedResultName() + "'s page.";
            this.loadedCreator = this.searchbar.getSelectedArtist().getPageCreator();
        } else if (this.searchbar.searchedForHosts()) {
            this.commandMessage = "Successfully selected " + this.searchbar.getSelectedResultName() + "'s page.";
            this.loadedCreator = this.searchbar.getSelectedHost().getPageCreator();
        } else {
            this.commandMessage = "Successfully selected " + this.searchbar.getSelectedResultName() + ".";
        }
    }

    /**
     *
     */
    public void doLoad() {
        if (!this.searchbar.isSelected()) {
            this.commandMessage = "Please select a source before attempting to load.";
            return;
        }

        if (this.searchbar.searchedForSongs()) {
            this.musicplayer.loadSong(this.searchbar.getSelectedSong());
        } else if (this.searchbar.searchedForPlaylists()) {
            this.musicplayer.loadPlaylist(this.searchbar.getSelectedPlaylist());
        } else if (this.searchbar.searchedForPodcasts()) {
            this.musicplayer.loadPodcast(this.searchbar.getSelectedPodcast());
        } else if (this.searchbar.searchedForAlbums()) {
            this.musicplayer.loadAlbum(this.searchbar.getSelectedAlbum());
        }

        this.commandMessage = "Playback loaded successfully.";
    }

    /**
     *
     */
    public void doPlayPause() {
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before attempting to pause or resume playback.";
            return;
        }

        this.musicplayer.togglePlayPause();

        if (this.musicplayer.isPaused()) {
            this.commandMessage = "Playback paused successfully.";
        } else {
            this.commandMessage = "Playback resumed successfully.";
        }
    }

    /**
     *
     */
    public void doRepeat() {
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before setting the repeat status.";
            return;
        }

        this.musicplayer.switchRepeat();

        this.commandMessage = "Repeat mode changed to " + this.musicplayer.getRepeatModeName() + ".";
    }

    /**
     *
     * @param seed
     */
    public void doShuffle(final int seed) {
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before using the shuffle function.";
            return;
        }

        if (!this.musicplayer.isPlaylistLoaded() && !this.musicplayer.isAlbumLoaded()) {
            this.commandMessage = "The loaded source is not a playlist or an album.";
            return;
        }

        this.musicplayer.toggleShuffle(seed);

        if (this.musicplayer.isShuffled()) {
            this.commandMessage = "Shuffle function activated successfully.";
        } else {
            this.commandMessage = "Shuffle function deactivated successfully.";
        }
    }

    /**
     *
     */
    public void doForward() {
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before attempting to forward.";
            return;
        }

        if (!this.musicplayer.isPodcastLoaded()) {
            this.commandMessage = "The loaded source is not a podcast.";
            return;
        }

        this.musicplayer.forward();

        this.commandMessage = "Skipped forward successfully.";
    }

    /**
     *
     */
    public void doBackward() {
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please select a source before rewinding.";
            return;
        }

        if (!this.musicplayer.isPodcastLoaded()) {
            this.commandMessage = "The loaded source is not a podcast.";
            return;
        }

        this.musicplayer.backward();

        this.commandMessage = "Rewound successfully.";
    }

    /**
     *
     */
    public void doLike() {
        Song currentSong;

        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before liking or unliking.";
            return;
        }

        if (!this.musicplayer.isSongLoaded()) {
            this.commandMessage = "Loaded source is not a song.";
            return;
        }

        currentSong = this.musicplayer.getLoadedSong();

        if (this.likedSongs.contains(currentSong)) {
            this.likedSongs.remove(currentSong);
            currentSong.removeLike();
            this.commandMessage = "Unlike registered successfully.";
        } else {
            this.likedSongs.add(currentSong);
            currentSong.addLike();
            this.commandMessage = "Like registered successfully.";
        }
    }

    /**
     *
     */
    public void doNext() {
        String trackName;

        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before skipping to the next track.";
            return;
        }

        this.musicplayer.next();

        trackName = this.musicplayer.getCurrentRecName();

        if (this.musicplayer.isLoaded()) {
            this.commandMessage = "Skipped to next track successfully. The current track is " + trackName + ".";
        } else {
            this.commandMessage = "Please load a source before skipping to the next track.";
        }
    }

    /**
     *
     */
    public void doPrev() {
        String trackName;

        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before returning to the previous track.";
            return;
        }

        this.musicplayer.prev();

        trackName = this.musicplayer.getCurrentRecName();

        this.commandMessage = "Returned to previous track successfully. The current track is " + trackName + ".";
    }

    /**
     *
     * @param playlistId
     */
    public void doAddRemoveInPlaylist(final int playlistId) {
        int playlistIndex;
        Song loadedSong;
        Playlist selectedPlaylist;

        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before adding to or removing from the playlist.";
            return;
        }

        if (!this.musicplayer.isSongLoaded()) {
            this.commandMessage = "The loaded source is not a song.";
            return;
        }

        if (playlistId > this.personalPlaylists.size()) {
            this.commandMessage = "The specified playlist does not exist.";
            return;
        }

        playlistIndex = playlistId - 1;
        loadedSong = this.musicplayer.getLoadedSong();
        selectedPlaylist = this.personalPlaylists.get(playlistIndex);

        if (selectedPlaylist.hasSong(loadedSong)) {
            selectedPlaylist.removeSong(loadedSong);
            this.commandMessage = "Successfully removed from playlist.";
        } else {
            selectedPlaylist.addSong(loadedSong);
            this.commandMessage = "Successfully added to playlist.";
        }
    }

    /**
     *
     * @return
     */
    public MusicPlayerStatusOutput doMPlayerStatus() {
        MusicPlayerStatusOutput mpStatusOutput = new MusicPlayerStatusOutput();

        if (this.musicplayer.isLoaded()) {
            mpStatusOutput.setName(this.musicplayer.getCurrentRecName());
        } else {
            mpStatusOutput.setName("");
        }

        mpStatusOutput.setRemainedTime(this.musicplayer.getRemainedTime());

        switch (this.musicplayer.getRepeatModeName()) {
            case "no repeat":
                mpStatusOutput.setRepeat("No Repeat");
                break;
            case "repeat all":
                mpStatusOutput.setRepeat("Repeat All");
                break;
            case "repeat current song":
                mpStatusOutput.setRepeat("Repeat Current Song");
                break;
            case "repeat once":
                mpStatusOutput.setRepeat("Repeat Once");
                break;
            case "repeat infinite":
                mpStatusOutput.setRepeat("Repeat Infinite");
                break;
            default:
                break;
        }

        mpStatusOutput.setShuffle(this.musicplayer.isShuffled());
        mpStatusOutput.setPaused(this.musicplayer.isPaused());

        this.commandMessage = null;

        return mpStatusOutput;
    }

    /**
     *
     * @param playlistName
     */
    public void doCreatePlaylist(final String playlistName) {
        Playlist newPlaylist;
        DataBase database;

        for (var playlist : this.personalPlaylists) {
            if (playlist.getName().equals(playlistName)) {
                this.commandMessage = "A playlist with the same name already exists.";
                return;
            }
        }

        newPlaylist = new Playlist(playlistName, this.getUserName());
        database = DataBase.getInstance();

        this.personalPlaylists.add(newPlaylist);
        database.addPlaylist(newPlaylist);

        this.commandMessage = "Playlist created successfully.";
    }

    /**
     *
     * @param playlistId
     */
    public void doSwitchVisibility(final int playlistId) {
        int playlistIndex;
        Playlist selectedPlaylist;

        if (playlistId > this.personalPlaylists.size()) {
            this.commandMessage = "The specified playlist ID is too high.";
            return;
        }

        playlistIndex = playlistId - 1;
        selectedPlaylist = this.personalPlaylists.get(playlistIndex);
        selectedPlaylist.changeVisibility();

        if (selectedPlaylist.isVisible()) {
            this.commandMessage = "Visibility status updated successfully to public.";
        } else {
            this.commandMessage = "Visibility status updated successfully to private.";
        }
    }

    /**
     *
     */
    public void doFollowPlaylist() {
        Playlist selectedPlaylist;

        if (!this.searchbar.isSelected()) {
            this.commandMessage = "Please select a source before following or unfollowing.";
            return;
        }

        if (!this.searchbar.searchedForPlaylists()) {
            this.commandMessage = "The selected source is not a playlist.";
            return;
        }

        // selectedPlaylist = this.searchbar.getSelectedPlaylist();
        selectedPlaylist = this.searchbar.getPlaylistForFollow();

        if (this.personalPlaylists.contains(selectedPlaylist)) {
            this.commandMessage = "You cannot follow or unfollow your own playlist.";
            return;
        }

        if (this.followedPlaylists.contains(selectedPlaylist)) {
            this.followedPlaylists.remove(selectedPlaylist);
            selectedPlaylist.removeFollower();
            this.commandMessage = "Playlist unfollowed successfully.";
            return;
        }

        this.followedPlaylists.add(selectedPlaylist);
        selectedPlaylist.addFollower();
        this.commandMessage = "Playlist followed successfully.";
    }

    /**
     *
     * @return
     */
    public ArrayList<PlaylistOutput> doShowPlaylists() {
        PlaylistOutput plOutput;
        ArrayList<PlaylistOutput> result = new ArrayList<>();

        for (var playlist : this.personalPlaylists) {
            plOutput = new PlaylistOutput();

            plOutput.setName(playlist.getName());
            plOutput.setSongs(playlist.getSongsNameList());

            if (playlist.isVisible()) {
                plOutput.setVisibility("public");
            } else {
                plOutput.setVisibility("private");
            }

            plOutput.setFollowers(playlist.getNrOfFollowers());

            result.add(plOutput);
        }

        this.commandMessage = null;

        return result;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getPreferredSongsName() {
        ArrayList<String> result = new ArrayList<>();

        for (var song : this.likedSongs) {
            result.add(song.getName());
        }

        return result;
    }

    /**
     *
     */
    public void doSwitchStatus() {
        this.active = !this.active;

        this.commandMessage = this.getUserName() + " has changed status successfully.";
    }

    /**
     *
     * @param page
     */
    public void doChangePage(final String page) {
        if (!page.equals("Home") && !page.equals("LikedContent")) {
            this.commandMessage = this.getUserName() + " is trying to access a non-existent page.";
            return;
        }

        switch (page) {
            case "Home":
                this.loadedCreator = this.homeCreator;
                break;
            case "LikedContent":
                this.loadedCreator = this.likedCreator;
                break;
            default:
                break;
        }

        this.commandMessage = this.getUserName() + " accessed " + page + " successfully.";
    }

    /**
     *
     */
    public String doGetPage() {
        return this.loadedCreator.createPage();
    }

    /**
     *
     */
    public void rmvAllHistory() {
        DataBase database = DataBase.getInstance();

        for (var playlist : this.personalPlaylists) {
            database.removePlaylist(playlist);
        }

        for (var playlist : this.followedPlaylists) {
            playlist.removeFollower();
        }

        for (var song : this.likedSongs) {
            song.removeLike();
        }
    }

    /**
     *
     * @param song
     */
    public void checkRemoveSong(final Song song) {
        if (this.likedSongs.contains(song)) {
            this.likedSongs.remove(song);
        }

        for (var playlist : this.personalPlaylists) {
            if (playlist.hasSong(song)) {
                playlist.removeSong(song);
            }
        }
    }

    /**
     *
     * @param playlist
     */
    public void checkRemovePlaylist(final Playlist playlist) {
        // for (var followedplaylist : this.followedPlaylists) {
        //     if (followedplaylist == playlist) {
        //         this.followedPlaylists.remove(playlist);
        //         return;
        //     }
        // }

        if (this.followedPlaylists.contains(playlist)) {
            this.followedPlaylists.remove(playlist);
        }
    }

    /**
     *
     * @return
     */
    public ArrayList<Playlist> getOwnPlaylists() {
        return this.personalPlaylists;
    }

    /**
     *
     * @return
     */
    public PageCreator getPageCreator() {
        return this.loadedCreator;
    }

    /**
     *
     * @return
     */
    public String getListeningColl() {
        return this.musicplayer.getListeningColl();
    }

    /**
     *
     * @return
     */
    public String getListeningSong() {
        return this.musicplayer.getListeningSong();
    }

    /**
     * 
     */
    public String getListeningSongName() {
        if (this.musicplayer.isSongLoaded())
            return this.musicplayer.getLoadedSong().getName();

        return null;
    }

    /**
     *
     */
    public String getListeningCollName() {
        if (this.musicplayer.isPlaylistLoaded())
            return this.musicplayer.getLoadedPlaylist().getName();

        if (this.musicplayer.isAlbumLoaded())
            return this.musicplayer.getLoadedAlbum().getName();

        if (this.musicplayer.isPodcastLoaded())
            return this.musicplayer.getLoadedPodcast().getName();
        
        return null;
    }

    /**
     *
     */
    public ArrayList<Song> getListeningSongs() {
        if (this.musicplayer.isPlaylistLoaded())
            return this.musicplayer.getLoadedPlaylist().getSongs();

        if (this.musicplayer.isAlbumLoaded())
            return this.musicplayer.getLoadedAlbum().getSongs();
        
        return null;
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean hasPlaylistWithName(final String name) {
        for (var playlist : this.personalPlaylists) {
            if (playlist.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     */
    public boolean isActive() {
        return this.active;
    }
}
