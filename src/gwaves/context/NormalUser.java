package gwaves.context;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import fileio.input.UserInput;
import fileio.input.FilterInput;
import fileio.output.MusicPlayerStatusOutput;
import fileio.output.PlaylistOutput;
import fileio.output.WrappedOutput;

import gwaves.sample.AudioRec;
import gwaves.sample.Song;
import gwaves.storage.DataBase;
import gwaves.tools.Musicplayer;
import gwaves.tools.Searchbar;
import gwaves.ui.HomePageCreator;
import gwaves.ui.LikedPageCreator;
import gwaves.ui.PageCreator;
import gwaves.collection.AudioCollection;
import gwaves.collection.Playlist;
import gwaves.collection.Album;
import gwaves.sample.Episode;

public final class NormalUser extends User {
    @Getter
    private ArrayList<Playlist> personalPlaylists;
    private ArrayList<Playlist> followedPlaylists;
    private ArrayList<Song> likedSongs;

    private Searchbar searchbar;
    private Musicplayer musicplayer;

    private boolean active;

    private PageCreator loadedCreator;

    private HomePageCreator homeCreator;
    private LikedPageCreator likedCreator;

    private LinkedHashMap<Artist, Integer> listenedArtists;
    private LinkedHashMap<String, Integer> listenedGenres;
    private LinkedHashMap<Song, Integer> listenedSongs;
    private LinkedHashMap<Album, Integer> listenedAlbums;
    private LinkedHashMap<Episode, Integer> listenedEpisodes;

    private ArrayList<AppNotification> notifications;

    /**
     * Default Constructor
     */
    public NormalUser(final String username, final int age, final String city) {
        super(username, age, city);
        this.personalPlaylists = new ArrayList<>();
        this.followedPlaylists = new ArrayList<>();
        this.likedSongs = new ArrayList<>();
        this.searchbar = new Searchbar(this);
        this.musicplayer = new Musicplayer(this);
        this.commandMessage = null;
        this.active = true;
        this.homeCreator = new HomePageCreator(likedSongs, followedPlaylists);
        this.likedCreator = new LikedPageCreator(likedSongs, followedPlaylists);
        this.loadedCreator = this.homeCreator;
        this.notifications = new ArrayList<>();
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
        this.musicplayer = new Musicplayer(this);
        this.commandMessage = null;
        this.active = true;
        this.homeCreator = new HomePageCreator(likedSongs, followedPlaylists);
        this.likedCreator = new LikedPageCreator(likedSongs, followedPlaylists);
        this.loadedCreator = this.homeCreator;
        this.notifications = new ArrayList<>();
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
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before skipping to the next track.";
            return;
        }

        this.musicplayer.next();

        if (this.musicplayer.isLoaded()) {
            this.commandMessage = "Skipped to next track successfully. The current track is " 
                                  + this.musicplayer.getCurrentRec().getName() + ".";
        } else {
            this.commandMessage = "Please load a source before skipping to the next track.";
        }
    }

    /**
     *
     */
    public void doPrev() {
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before returning to the previous track.";
            return;
        }

        this.musicplayer.prev();

        this.commandMessage = "Returned to previous track successfully. The current track is "
                              + this.musicplayer.getCurrentRec().getName() + ".";
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

        if (selectedPlaylist.hasAudRec(loadedSong)) {
            selectedPlaylist.removeAudRec(loadedSong);
            this.commandMessage = "Successfully removed from playlist.";
        } else {
            selectedPlaylist.addAudRec(loadedSong);
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
            mpStatusOutput.setName(this.musicplayer.getCurrentRec().getName());
        } else {
            mpStatusOutput.setName("");
        }

        mpStatusOutput.setRemainedTime(this.musicplayer.getRemainingTime());

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

        newPlaylist = new Playlist(playlistName, this.getUsername());
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
        PlaylistOutput playlistOutput;
        ArrayList<PlaylistOutput> result = new ArrayList<>();

        for (var playlist : this.personalPlaylists) {
            playlistOutput = new PlaylistOutput();

            playlistOutput.setName(playlist.getName());
            playlistOutput.setSongs(playlist.getAudRecsName());

            if (playlist.isVisible()) {
                playlistOutput.setVisibility("public");
            } else {
                playlistOutput.setVisibility("private");
            }

            playlistOutput.setFollowers(playlist.getNrOfFollowers());

            result.add(playlistOutput);
        }

        this.commandMessage = null;

        return result;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> doShowPreferredSongs() {
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

        this.commandMessage = this.getUsername() + " has changed status successfully.";
    }

    /**
     *
     * @param page
     */
    public void doChangePage(final String page) {
        if (!page.equals("Home") && !page.equals("LikedContent")) {
            this.commandMessage = this.getUsername() + " is trying to access a non-existent page.";
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

        this.commandMessage = this.getUsername() + " accessed " + page + " successfully.";
    }

    /**
     *
     */
    public String doGetPage() {
        return this.loadedCreator.createPage();
    }

    public WrappedOutput doWrapped() {
        WrappedOutput wrOut = new WrappedOutput();

        // TODO

        return wrOut;
    }

    public void updateStatistics(Song song) {
        Integer val;

        if (this.listenedSongs.containsKey(song)) {
            val = this.listenedSongs.get(song);
            val++;
        } else {
            this.listenedSongs.put(song, 1);
        }

        if (this.listenedArtists.containsKey(song.getArtist())) {
            val = this.listenedArtists.get(song.getArtist());
            val++;
        } else {
            this.listenedArtists.put(song.getArtist(), 1);
        }

        if (this.listenedGenres.containsKey(song.getGenre())) {
            val = this.listenedGenres.get(song.getGenre());
            val++;
        } else {
            this.listenedGenres.put(song.getGenre(), 1);
        }
    }

    public void updateStatistics(Episode episode) {
        Integer val;

        if (this.listenedEpisodes.containsKey(episode)) {
            val = this.listenedEpisodes.get(episode);
            val++;
        } else {
            this.listenedEpisodes.put(episode, 1);
        }
    }

    public void updateStatistics(Album album) {
        Integer val;

        if (this.listenedAlbums.containsKey(album)) {
            val = this.listenedAlbums.get(album);
            val++;
        } else {
            this.listenedAlbums.put(album, 1);
        }
    }

    public void addNotification(String name, String description) {
        this.notifications.add(new AppNotification(name, description));
    }

    /**
     *
     */
    public void clearAll() {
        DataBase database = DataBase.getInstance();

        for (var playlist : this.personalPlaylists) {
            database.removePlaylist(playlist);
        }

        this.personalPlaylists.clear();

        for (var playlist : this.followedPlaylists) {
            playlist.removeFollower();
        }

        this.followedPlaylists.clear();

        for (var song : this.likedSongs) {
            song.removeLike();
        }

        this.likedSongs.clear();
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
            if (playlist.hasAudRec(song)) {
                playlist.removeAudRec(song);
            }
        }
    }

    /**
     *
     * @param playlist
     */
    public void checkRemovePlaylist(final Playlist playlist) {
        if (this.followedPlaylists.contains(playlist)) {
            this.followedPlaylists.remove(playlist);
        }
    }

    public AudioRec getListeningAudRec() {
        return this.musicplayer.getCurrentRec();
    }

    public AudioCollection<?> getListeningCollec() {
        return this.musicplayer.getCurrentCollec();
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
     */
    public boolean isActive() {
        return this.active;
    }
}

@Getter @AllArgsConstructor
class AppNotification {
    private String name;
    private String description;
}
