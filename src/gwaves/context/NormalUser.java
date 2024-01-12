package gwaves.context;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.node.ObjectNode;

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
import gwaves.ui.HomePage;
import gwaves.ui.LikedPage;
import gwaves.ui.Page;
import gwaves.collection.AudioCollection;
import gwaves.collection.Playlist;
import gwaves.collection.Album;
import gwaves.sample.Episode;
import gwaves.misc.UserNotification;
import gwaves.misc.ArtistMerch;

public final class NormalUser extends User {
    @Getter
    private ArrayList<Playlist> personalPlaylists;
    private ArrayList<Playlist> followedPlaylists;
    private ArrayList<Song> likedSongs;

    private Searchbar searchbar;
    private Musicplayer musicplayer;

    private boolean active;
    // private boolean premium;

    private HomePage homePage;
    private LikedPage likedPage;

    private int pageIndex;
    private ArrayList<Page> pageHistory;

    private LinkedHashMap<Artist, Integer> listenedArtists;
    private LinkedHashMap<String, Integer> listenedGenres;
    // private LinkedHashMap<Song, Integer> listenedSongs;
    private LinkedHashMap<String, Integer> listenedSongs;
    // private LinkedHashMap<Album, Integer> listenedAlbums;
    private LinkedHashMap<String, Integer> listenedAlbums;
    private LinkedHashMap<Episode, Integer> listenedEpisodes;

    public enum RecommendationType {
        NA,
        SONG,
        PLAYLIST,
        FANSPLAYLIST
    }

    private RecommendationType lastRecommendation;
    private ArrayList<Song> recommendedSongs;
    private ArrayList<Playlist> recommendedPlaylists;
    private ArrayList<Playlist> fansPlaylists;

    private ArrayList<ArtistMerch> boughtMerches;

    private ArrayList<UserNotification> notifications;

    /**
     * Default Constructor
     */
    public NormalUser(final String username, final int age, final String city) {
        super(username, age, city);
        this.initHelper();
    }

    /**
     * Default Constructor
     */
    public NormalUser(final UserInput userInput) {
        super(userInput);
        this.initHelper();
    }

    private void initHelper() {
        this.personalPlaylists = new ArrayList<>();
        this.followedPlaylists = new ArrayList<>();
        this.likedSongs = new ArrayList<>();
        this.searchbar = new Searchbar(this);
        this.musicplayer = new Musicplayer(this);
        this.commandMessage = null;
        this.active = true;
        this.listenedArtists = new LinkedHashMap<>();
        this.listenedAlbums = new LinkedHashMap<>();
        this.listenedSongs = new LinkedHashMap<>();
        this.listenedGenres = new LinkedHashMap<>();
        this.listenedEpisodes = new LinkedHashMap<>();
        this.recommendedSongs = new ArrayList<>();
        this.recommendedPlaylists = new ArrayList<>();
        this.fansPlaylists = new ArrayList<>();
        // this.homePage = new HomePage(this, likedSongs, followedPlaylists);
        this.homePage = new HomePage(this, this.likedSongs,
                                     this.followedPlaylists,
                                     this.recommendedSongs,
                                     this.recommendedPlaylists,
                                     this.fansPlaylists);
        this.likedPage = new LikedPage(this, this.likedSongs, this.followedPlaylists);
        this.pageIndex = 0;
        this.pageHistory = new ArrayList<>();
        this.pageHistory.add(this.homePage);
        this.boughtMerches = new ArrayList<>();
        this.notifications = new ArrayList<>();
        // this.lastRecommendation = RecommendationType.NA;
        this.lastRecommendation = RecommendationType.NA;
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
            this.pageHistory.add(this.searchbar.getSelectedArtist().getPage());
            this.pageIndex++;
        } else if (this.searchbar.searchedForHosts()) {
            this.commandMessage = "Successfully selected " + this.searchbar.getSelectedResultName() + "'s page.";
            this.pageHistory.add(this.searchbar.getSelectedHost().getPage());
            this.pageIndex++;
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
    public void doChangePage(final String pageName) {
        Page nextPage;

        // TODO de facut review la cast aici
        switch (pageName) {
            case "Home":
                nextPage = this.homePage;
                break;
            case "LikedContent":
                nextPage = this.likedPage;
                break;
            case "Artist":
                nextPage = this.musicplayer.getLoadedSong().getArtist().getPage();
                break;
            case "Host":
                nextPage = DataBase.getInstance().queryHost(this.musicplayer.getLoadedPodcast().getOwner()).getPage();
                break;
            default:
                this.commandMessage = this.getUsername() + " is trying to access a non-existent page.";
                return;
        }

        // if (this.pageIndex + 1 != this.pageHistory.size()) {
        //     ListIterator<Page> iter = this.pageHistory.listIterator(this.pageIndex);

        //     while (iter.hasNext()) {
        //         iter.remove();
        //         iter.next();
        //     }
        // }
        ListIterator<Page> iter = this.pageHistory.listIterator(this.pageIndex + 1);

        while (iter.hasNext()) {
            // System.out.println("am intrat");
            iter.next();
            iter.remove();
        }

        this.pageHistory.add(nextPage);
        this.pageIndex++;

        // System.out.println("---- " + this.pageIndex);
        // for (var page : this.pageHistory) {
        //     System.out.println(page.getClass() + " ");
        // }

        this.commandMessage = this.getUsername() + " accessed " + pageName + " successfully.";
    }

    /**
     *
     */
    public String doGetPage() {
        // System.out.println("size - " + this.pageHistory.size());
        // return null;
        return this.pageHistory.get(this.pageIndex).strigify();
    }

    public WrappedOutput doWrapped() {
        // if (this.listenedArtists.isEmpty()
        //     && this.listenedAlbums.isEmpty()
        //     && this.listenedSongs.isEmpty()
        //     && this.listenedGenres.isEmpty()
        //     && this.listenedEpisodes.isEmpty()) {
        //     this.commandMessage = "No data to show for user " + this.getUsername() + ".";
        //     return null;
        // }

        if (this.listenedSongs.isEmpty()
            && this.listenedEpisodes.isEmpty()) {
            this.commandMessage = "No data to show for user " + this.getUsername() + ".";
            return null;
        }

        WrappedOutput wrappedOutput = new WrappedOutput();
        ObjectNode objNode;

        objNode = NormalUser.objMapper.createObjectNode();
        List<Artist> topArtists = this.listenedArtists.keySet().stream()
                                            .sorted(new Comparator<Artist>() {
                                                public int compare(Artist artist1, Artist artist2) {
                                                    if (listenedArtists.get(artist2) == listenedArtists.get(artist1)) {
                                                        return artist1.getUsername().compareTo(artist2.getUsername());
                                                    }

                                                    return listenedArtists.get(artist2) - listenedArtists.get(artist1);
                                                }
                                            })
                                            .limit(5)
                                            .collect(Collectors.toList());

        for (var artist : topArtists) {
            objNode.put(artist.getUsername(), this.listenedArtists.get(artist));
        }
        wrappedOutput.setTopArtists(objNode);

        objNode = NormalUser.objMapper.createObjectNode();
        List<String> topGenres = this.listenedGenres.keySet().stream()
                                            .sorted(new Comparator<String>() {
                                                public int compare(String genre1, String genre2) {
                                                    if (listenedGenres.get(genre2) == listenedGenres.get(genre1)) {
                                                        return genre1.compareTo(genre2);
                                                    }

                                                    return listenedGenres.get(genre2) - listenedGenres.get(genre1);
                                                }
                                            })
                                            .limit(5)
                                            .collect(Collectors.toList());

        for (var genre : topGenres) {
            objNode.put(genre, this.listenedGenres.get(genre));
        }
        wrappedOutput.setTopGenres(objNode);

        
        // System.out.println("--------");
        // for (var songName : this.listenedSongs.keySet()) {
        //     System.out.println(songName + " - " + this.listenedSongs.get(songName));
        // }
        // System.out.println("--------");
        

        objNode = NormalUser.objMapper.createObjectNode();
        List<String> topSongsName = this.listenedSongs.keySet().stream()
                                            .sorted(new Comparator<String>() {
                                                public int compare(String songName1, String songName2) {
                                                    if (listenedSongs.get(songName2) == listenedSongs.get(songName1)) {
                                                        return songName1.compareTo(songName2);
                                                    }

                                                    return listenedSongs.get(songName2) - listenedSongs.get(songName1);
                                                }
                                            })
                                            // .sorted(Comparator.comparing(song -> this.listenedSongs.get(song)))
                                            // .sorted(Comparator.comparing(Song::getName))
                                            .limit(5)
                                            .collect(Collectors.toList());

        for (var songName : topSongsName) {
            objNode.put(songName, this.listenedSongs.get(songName));
        }
        wrappedOutput.setTopSongs(objNode);

        objNode = NormalUser.objMapper.createObjectNode();
        List<String> topAlbumsName = this.listenedAlbums.keySet().stream()
                                            .sorted(new Comparator<String>() {
                                                public int compare(String albumName1, String albumName2) {
                                                    if (listenedAlbums.get(albumName2) == listenedAlbums.get(albumName1)) {
                                                        return albumName1.compareTo(albumName2);
                                                    }

                                                    return listenedAlbums.get(albumName2) - listenedAlbums.get(albumName1);
                                                }
                                            })
                                            .limit(5)
                                            .collect(Collectors.toList());

        for (var albumName : topAlbumsName) {
            objNode.put(albumName, this.listenedAlbums.get(albumName));
        }
        wrappedOutput.setTopAlbums(objNode);

        objNode = NormalUser.objMapper.createObjectNode();
        List<Episode> topEpisodes = this.listenedEpisodes.keySet().stream()
                                            .sorted(new Comparator<Episode>() {
                                                public int compare(Episode episode1, Episode episode2) {
                                                    if (listenedEpisodes.get(episode2) == listenedEpisodes.get(episode1)) {
                                                        return episode1.getName().compareTo(episode2.getName());
                                                    }

                                                    return listenedEpisodes.get(episode2) - listenedEpisodes.get(episode1);
                                                }
                                            })
                                            .limit(5)
                                            .collect(Collectors.toList());

        for (var episode : topEpisodes) {
            objNode.put(episode.getName(), this.listenedEpisodes.get(episode));
        }
        wrappedOutput.setTopEpisodes(objNode);

        return wrappedOutput;
    }

    public void doBuyMerch(String merchName) {
        Page currentPage = this.pageHistory.get(this.pageIndex);

        if (currentPage.type() != Page.Type.OFARTIST) {
            this.commandMessage = "Cannot buy merch from this page.";
            return;
        }

        Artist artist = (Artist)currentPage.owner();
        ArtistMerch merch = artist.buyMerch(merchName);

        if (merch == null) {
            this.commandMessage = "The merch " + merchName + " doesn't exist.";
            return;
        }

        this.boughtMerches.add(merch);

        this.commandMessage = this.getUsername() + " has added new merch successfully.";
    }

    public ArrayList<String> doSeeMerch() {
        ArrayList<String> merchesName = new ArrayList<>();

        for (var merch : this.boughtMerches) {
            merchesName.add(merch.getName());
        }

        return merchesName;
    }

    public void doBuyPremium() {
        if (this.musicplayer.isUpgraded()) {
            this.commandMessage = this.getUsername() + " is already a premium user.";
            return;
        }

        this.musicplayer.upgrade();

        this.commandMessage = this.getUsername() + " bought the subscription successfully.";
    }

    public void doCancelPremium() {
        if (!this.musicplayer.isUpgraded()) {
            this.commandMessage = this.getUsername() + " is not a premium user.";
            return;
        }

        this.musicplayer.downgrade();

        this.commandMessage = this.getUsername() + " cancelled the subscription successfully.";
    }

    public void doAdBreak(int adPrice) {
        if (this.musicplayer.isPaused()) {
            this.commandMessage = this.getUsername() + " is not playing any music.";
            return;
        }

        this.musicplayer.loadAdBreak(adPrice);

        this.commandMessage = "Ad inserted successfully.";
    }

    public void doSubscribe() {
        Page currentPage = this.pageHistory.get(this.pageIndex);
        User contentCreator = currentPage.owner();

        if (currentPage.type() == Page.Type.OFNUSER)  {
            this.commandMessage = "To subscribe you need to be on the page of an artist or host.";
            return;
        }
        
        if (currentPage.type() == Page.Type.OFARTIST) {
            if (((Artist)contentCreator).hasSubscriber(this)) {
                ((Artist)contentCreator).removeSubscriber(this);
                this.commandMessage = this.getUsername() + " unsubscribed from " + contentCreator.getUsername() + " successfully.";
            } else {
                ((Artist)contentCreator).addSubscriber(this);
                this.commandMessage = this.getUsername() + " subscribed to " + contentCreator.getUsername() + " successfully.";
            }
        } else {
            if (((Host)contentCreator).hasSubscriber(this)) {
                ((Host)contentCreator).removeSubscriber(this);
                this.commandMessage = this.getUsername() + " unsubscribed from " + contentCreator.getUsername() + " successfully.";
            } else {
                ((Host)contentCreator).removeSubscriber(this);
                this.commandMessage = this.getUsername() + " subscribed to " + contentCreator.getUsername() + " successfully.";
            }
        }
    }

    public ArrayList<UserNotification> doGetNotifications() {
        ArrayList<UserNotification> currentNotifications = new ArrayList<>(this.notifications);
        this.notifications.clear();
        return currentNotifications;
    }

    public void doUpdateRecommendations(String recommendationType) {
        Song newSong;
        Playlist newPlaylist;

        switch (recommendationType) {
            case "random_song":
                // TODO de verificat daca merge si cu
                // if (this.musicplayer.getPlayedTime() < 30) {
                //     break;
                // }

                newSong = DataBase.getInstance().queryRandomSong(this.musicplayer.getLoadedSong().getGenre(),
                                                                 this.musicplayer.getPlayedTime());

                this.recommendedSongs.add(0, newSong);
                this.lastRecommendation = RecommendationType.SONG;
                break;
            case "random_playlist":
                newPlaylist = this.createRecommendedPlaylist();

                if (newPlaylist == null) {
                    this.commandMessage = "No new recommendations were found";
                    return;
                }

                this.recommendedPlaylists.add(0, newPlaylist);
                this.lastRecommendation = RecommendationType.PLAYLIST;
                break;
            case "fans_playlist":
                newPlaylist = this.createFansPlaylist();

                if (newPlaylist == null) {
                    this.commandMessage = "No new recommendations were found";
                    return;
                }

                this.fansPlaylists.add(0, newPlaylist);
                this.lastRecommendation = RecommendationType.FANSPLAYLIST;
                break;
        }

        // this.commandMessage = "No new recommendations found";
        this.commandMessage = "The recommendations for user " + this.getUsername()
                               + " have been updated successfully.";
    }

    private Playlist createRecommendedPlaylist() {
        Playlist newPlaylist = new Playlist(this.getUsername() + "'s recommendations", this.getUsername());
        List<String> topGenres = this.top3Genres();
        List<Song> topSongsGenre;

        if (topGenres == null) {
            return null;
        }

        if (topGenres.size() >= 1) {
            topSongsGenre = this.topSongsByGenre(5, topGenres.get(0));

            for (var song : topSongsGenre) {
                newPlaylist.addAudRec(song);
            }
        }

        if (topGenres.size() >= 2) {
            topSongsGenre = this.topSongsByGenre(3, topGenres.get(1));

            for (var song : topSongsGenre) {
                newPlaylist.addAudRec(song);
            }
        }

        if (topGenres.size() >= 3) {
            topSongsGenre = this.topSongsByGenre(2, topGenres.get(2));

            for (var song : topSongsGenre) {
                newPlaylist.addAudRec(song);
            }
        }

        return newPlaylist;
    }

    private Playlist createFansPlaylist() {
        Playlist newPlaylist = new Playlist(this.musicplayer.getLoadedSong().getArtist().getUsername() + " Fan Club recommendations", 
                                            this.getUsername());
        List<NormalUser> fans = this.musicplayer.getLoadedSong().getArtist().gettop5Fans();

        for (var fan : fans) {
            for (var song : fan.getTop5LikedSongs()) {
                newPlaylist.addAudRec(song);
            }
        }

        // TODO de verificat
        if (newPlaylist.getAudRecs().isEmpty()) {
            return null;
        }
        
        return newPlaylist;
    }

    private List<Song> topSongsByGenre(int limit, String genre) {
        ArrayList<Song> songs = new ArrayList<>();

        for (var song : this.likedSongs) {
            if (song.getGenre().equals(genre)) {
                songs.add(song);
            }
        }

        for (var playlist : this.personalPlaylists) {
            for (var song : playlist.getAudRecs()) {
                if (song.getGenre().equals(genre)) {
                    songs.add(song);
                }
            }
        }

        for (var playlist : this.followedPlaylists) {
            for (var song : playlist.getAudRecs()) {
                if (song.getGenre().equals(genre)) {
                    songs.add(song);
                }
            }
        }

        // De verificat daca trebuie si dupa nume
        return songs.stream()
                    .sorted(Comparator.comparing(Song::getLikes))
                    .limit(limit)
                    .collect(Collectors.toList());
    }

    private List<String> top3Genres() {
        Integer val;
        HashMap<String, Integer> genreDistrib = new HashMap<>();

        for (var song : this.likedSongs) {
            if (genreDistrib.containsKey(song.getGenre())) {
                val = genreDistrib.get(song.getGenre());
            } else {
                val = 0;
            }

            genreDistrib.put(song.getGenre(), val + 1);
        }

        for (var playlist : this.personalPlaylists) {
            for (var song : playlist.getAudRecs()) {
                if (genreDistrib.containsKey(song.getGenre())) {
                    val = genreDistrib.get(song.getGenre());
                } else {
                    val = 0;
                }
    
                genreDistrib.put(song.getGenre(), val + 1);
            }
        }

        for (var playlist : this.followedPlaylists) {
            for (var song : playlist.getAudRecs()) {
                if (genreDistrib.containsKey(song.getGenre())) {
                    val = genreDistrib.get(song.getGenre());
                } else {
                    val = 0;
                }
    
                genreDistrib.put(song.getGenre(), val + 1);
            }
        }

        if (genreDistrib.isEmpty()) {
            return null;
        }

        // De verificat daca trebuie si dupa nume
        return genreDistrib.keySet().stream()
                                    .sorted(Comparator.comparing(genre -> genreDistrib.get(genre)))
                                    .limit(3)
                                    .collect(Collectors.toList());
    }

    public void doLoadRecommendations() {
        switch (this.lastRecommendation) {
            case NA:
                this.commandMessage = "No recommendations available.";
                return;
            case SONG:
                if (this.recommendedSongs.isEmpty()) {
                    this.commandMessage = "No recommendations available.";
                    return;
                }

                this.musicplayer.loadSong(this.recommendedSongs.get(0));
                break;
            case PLAYLIST:
                if (this.recommendedPlaylists.isEmpty()) {
                    this.commandMessage = "No recommendations available.";
                    return;
                }

                this.musicplayer.loadPlaylist(this.recommendedPlaylists.get(0));
                break;
            case FANSPLAYLIST:
                if (this.fansPlaylists.isEmpty()) {
                    this.commandMessage = "No recommendations available.";
                    return;
                }

                this.musicplayer.loadPlaylist(this.fansPlaylists.get(0));
                break;
            default:
                break;
        }

        this.commandMessage = "Playback loaded successfully.";
    }

    public void doPreviousPage() {
        if (this.pageIndex == 0) {
            this.commandMessage = "There are no pages left to go back.";
            return;
        }

        this.pageIndex--;

        this.commandMessage = "The user " + this.getUsername() + " has navigated successfully to the previous page.";
    }

    public void doNextPage() {
        if (this.pageIndex == this.pageHistory.size() - 1) {
            this.commandMessage = "There are no pages left to go forward.";
            return;
        }

        this.pageIndex++;
        
        this.commandMessage = "The user " + this.getUsername() + " has navigated successfully to the next page.";
    }

    public void updateStatistics(Song song) {
        Integer val;

        // if (this.listenedSongs.containsKey(song)) {
        //     val = this.listenedSongs.get(song);
        //     // val++;
        //     // val = val + 1;
        //     this.listenedSongs.put(song, val + 1);
        //     System.out.println("song - " + song.getName() + ", listen - " + /*val*/this.listenedSongs.get(song));
        // } else {
        //     this.listenedSongs.put(song, 1);
        // }

        if (this.listenedSongs.containsKey(song.getName())) {
            val = this.listenedSongs.get(song.getName());
        } else {
            val = 0;
        }

        this.listenedSongs.put(song.getName(), val + 1);

        if (this.listenedAlbums.containsKey(song.getAlbum().getName())) {
            val = this.listenedAlbums.get(song.getAlbum().getName());
        } else {
            val = 0;
        }

        this.listenedAlbums.put(song.getAlbum().getName(), val + 1);

        if (this.listenedArtists.containsKey(song.getArtist())) {
            val = this.listenedArtists.get(song.getArtist());
        } else {
            val = 0;
        }

        this.listenedArtists.put(song.getArtist(), val + 1);

        if (this.listenedGenres.containsKey(song.getGenre())) {
            val = this.listenedGenres.get(song.getGenre());
        } else {
            val = 0;
        }

        this.listenedGenres.put(song.getGenre(), val + 1);
    }

    public void updateStatistics(Episode episode) {
        Integer val;

        if (this.listenedEpisodes.containsKey(episode)) {
            val = this.listenedEpisodes.get(episode);
        } else {
            val = 0;
        }

        this.listenedEpisodes.put(episode, val + 1);
    }

    // public void updateStatistics(Album album) {
    //     Integer val;

    //     if (this.listenedAlbums.containsKey(album)) {
    //         val = this.listenedAlbums.get(album);
    //     } else {
    //         val = 0;
    //     }

    //     this.listenedAlbums.put(album, val + 1);
    // }

    public void payRemaining() {
        this.musicplayer.downgrade();
    }

    public void addNotification(String name, String description) {
        this.notifications.add(new UserNotification(name, description));
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

    public List<Song> getTop5LikedSongs() {
        // De verificat daca trebuie dupa nume
        return this.likedSongs.stream()
                              .sorted(Comparator.comparing(Song::getLikes))
                              .collect(Collectors.toList());
    }

    /**
     *
     * @return
     */
    public Page getPage() {
        return this.pageHistory.get(this.pageIndex);
    }

    /**
     *
     */
    public boolean isActive() {
        return this.active;
    }

    public boolean isPremium() {
        return this.musicplayer.isUpgraded();
    }
}
