package gwaves.util;

import java.util.ArrayList;
import java.util.HashMap;

import fileio.input.CommandInput;
import fileio.output.CommandOutput;
import fileio.output.UserCommandOutput;
import fileio.output.MusicPlayerStatusOutput;
import fileio.output.PlaylistOutput;

import fileio.input.UserInput;

import gwaves.sample.Song;
import gwaves.collection.Playlist;

public class User {
    private String username;
    private Integer age;
    private String city;

    private ArrayList<Playlist> personalPlaylists;
    private ArrayList<Playlist> followedPlaylists;
    private ArrayList<Song> likedSongs;

    private Searchbar searchbar;
    private Musicplayer musicplayer;

    private String commandMessage;

    public User(UserInput userInput)
    {
        this.username = userInput.getUsername();
        this.age = userInput.getAge();
        this.city = userInput.getCity();
        this.personalPlaylists = new ArrayList<>();
        this.followedPlaylists = new ArrayList<>();
        this.likedSongs = new ArrayList<>();
        this.searchbar = new Searchbar();
        this.musicplayer = new Musicplayer();
    }

    public ArrayList<String> getPreferredSongsName()
    {
        ArrayList<String> result = new ArrayList<>();

        for (var song : this.likedSongs)
            result.add(song.getName());

        return result;
    }

    public UserCommandOutput executeCommand(CommandInput commandInput)
    {
        int timeInterval = 0;
        UserCommandOutput userCommandOutput = new UserCommandOutput(commandInput);

        userCommandOutput.setCommand(commandInput.getCommand());
        userCommandOutput.setUser(commandInput.getUsername());
        userCommandOutput.setTimestamp(commandInput.getTimestamp());

        timeInterval = commandInput.getTimestamp() - GlobalTime.getTime();
        GlobalTime.setTime(commandInput.getTimestamp());

        this.musicplayer.playFor(timeInterval);

        switch (commandInput.getCommand()) {
        case "search":
            userCommandOutput.setResults(this.doSearch(commandInput.getType(), commandInput.getFilters()));
            break;
        case "select":
            this.doSelect(commandInput.getItemNumber());
            break;
        case "load":
            this.doLoad();
            break;
        case "playPause":
            this.doPlayPause();
            break;
        case "repeat":
            this.doRepeat();
            break;
        case "shuffle":
            this.doShuffle(commandInput.getSeed());
            break;
        case "forward":
            this.doForward();
            break;
        case "backward":
            this.doBackward();
            break;
        case "like":
            this.doLike();
            break;
        case "next":
            this.doNext();
            break;
        case "prev":
            this.doPrev();
            break;
        case "addRemoveInPlaylist":
            this.doAddRemoveInPlaylist(commandInput.getPlaylistId());
            break;
        case "status":
            userCommandOutput.setStats(this.doStatus());
            break;
        case "createPlaylist":
            this.doCreatePlaylist(commandInput.getPlaylistName());
            break;
        case "switchVisibility":
            this.doSwitchVisibility(commandInput.getPlaylistId());
            break;
        case "followPlaylist":
            this.doFollowPlaylist();
            break;
        case "showPlaylists":
            userCommandOutput.setResult(this.doShowPlaylists());
            break;
        }

        userCommandOutput.setMessage(this.commandMessage);

        return userCommandOutput;
    }

    private ArrayList<String> doSearch(String type, Filter filter)
    {
        ArrayList<String> results = null;

        this.musicplayer.unloadCurrent();

        switch (type) {
        case "song":
            results = this.searchbar.searchSongs(filter);
            break;
        case "playlist":
            results = this.searchbar.searchPlaylists(filter);
            break;
        case "podcast":
            results = this.searchbar.searchPodcasts(filter);
            break;
        }

        this.commandMessage = "Search returned " + this.searchbar.getResultsNumber() + " results";

        return results;
    }

    private void doSelect(int itemNumber)
    {
        if (this.searchbar.getResultsNumber() == 0) {
            this.commandMessage = "Please conduct a search before making a selection.";
            return;
        }

        if (itemNumber > this.searchbar.getResultsNumber()) {
            this.commandMessage = "The selected ID is too high.";
            return;
        }

        this.searchbar.selectResult(itemNumber);

        if (this.searchbar.searchedForSongs())
            this.commandMessage = "Successfully selected " + this.searchbar.getSelectedSong() + ".";
        else if (this.searchbar.searchedForPlaylists())
            this.commandMessage = "Successfully selected " + this.searchbar.getSelectedPlaylist() + ".";
        else if (this.searchbar.searchedForPodcasts())
            this.commandMessage = "Successfully selected " + this.searchbar.getSelectedPodcast() + ".";
    }

    private void doLoad()
    {
        if (!this.searchbar.isSelected()) {
            this.commandMessage = "Please select a source before attempting to load.";
            return;
        }

        if (this.searchbar.searchedForSongs())
            this.musicplayer.loadSong(this.searchbar.getSelectedSong());
        else if (this.searchbar.searchedForPlaylists())
            this.musicplayer.loadPlaylist(this.searchbar.getSelectedPlaylist());
        else if (this.searchbar.searchedForPodcasts())
            this.musicplayer.loadPodcast(this.searchbar.getSelectedPodcast());

        this.commandMessage = "Playback loaded successfully.";
    }

    private void doPlayPause()
    {
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before attempting to pause or resume playback.";
            return;
        }

        this.musicplayer.playPause();

        if (this.musicplayer.isPaused())
            this.commandMessage = "Playback paused successfully.";
        else
            this.commandMessage = "Playback resumed successfully";
    }

    private void doRepeat()
    {
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before setting the repeat status.";
            return;
        }

        this.musicplayer.changeRepeatMode();

        this.commandMessage = "Repeat mode changed to " + this.musicplayer.getRepeatModeName();
    }

    private void doShuffle(long seed)
    {
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before using the shuffle function.";
            return;
        }

        if (!this.musicplayer.isPlaylistLoaded()) {
            this.commandMessage = "The loaded source is not a playlist.";
            return;
        }

        this.musicplayer.shufflePlaylist(seed);

        if (this.musicplayer.isShuffled())
            this.commandMessage = "Shuffle function activated successfully.";
        else
            this.commandMessage = "Shuffle function deactivated successfully.";
    }

    private void doForward()
    {
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before skipping forward.";
            return;
        }

        if (!this.musicplayer.isPodcastLoaded()) {
            this.commandMessage = "The loaded source is not a podcast.";
            return;
        }

        this.musicplayer.forward();

        this.commandMessage = "Skipped forward successfully.";
    }

    private void doBackward()
    {
        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please select a source before rewinding.";
            return;
        }

        if (!this.musicplayer.isPodcastLoaded()) {
            this.commandMessage = "The loaded source is not a podcast. ";
            return;
        }

        this.musicplayer.backward();

        this.commandMessage = "Rewound successfully.";
    }

    private void doLike()
    {
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

    private void doNext()
    {
        String trackName;

        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before skipping to the next track.";
            return;
        }

        this.musicplayer.next();

        trackName = this.musicplayer.getCurrentRecName();

        if (this.musicplayer.isLoaded())
            this.commandMessage = "Skipped to next track successfully. The current track is" + trackName + ".";
        else
            this.commandMessage = "Please load a source before skipping to the next track.";
    }

    private void doPrev()
    {
        String trackName;

        if (!this.musicplayer.isLoaded()) {
            this.commandMessage = "Please load a source before returning to the previous track.";
            return;
        }

        this.musicplayer.prev();

        trackName = this.musicplayer.getCurrentRecName();

        this.commandMessage = "Returned to previous track successfully. The current track is" + trackName + ".";
    }

    private void doAddRemoveInPlaylist(int playlistId)
    {
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

        loadedSong = this.musicplayer.getLoadedSong();
        selectedPlaylist = this.personalPlaylists.get(playlistId);

        if (selectedPlaylist.hasSong(loadedSong)) {
            selectedPlaylist.removeSong(loadedSong);
            this.commandMessage = "Successfully removed from playlist.";
        } else {
            selectedPlaylist.addSong(loadedSong);
            this.commandMessage = "Successfully added to playlist.";
        }
    }

    private MusicPlayerStatusOutput doStatus()
    {
        MusicPlayerStatusOutput mpStatusOutput = new MusicPlayerStatusOutput();

        mpStatusOutput.setName(this.musicplayer.getCurrentRecName());
        mpStatusOutput.setRemainedTime(this.musicplayer.getRemainedTime());
        mpStatusOutput.setRepeat(this.musicplayer.getRepeatModeName());
        mpStatusOutput.setShuffle(this.musicplayer.isShuffled());
        mpStatusOutput.setPaused(this.musicplayer.isPaused());

        return mpStatusOutput;
    }

    private void doCreatePlaylist(String playlistName)
    {
        Playlist newPlaylist;
        DataBase database;

        for (var playlist : this.personalPlaylists)
            if (playlist.getName().equals(playlistName)) {
                this.commandMessage = "A playlist with the same name already exists.";
                return;
            }

        newPlaylist = new Playlist(playlistName, this.username);
        database = DataBase.getInstance();

        this.personalPlaylists.add(newPlaylist);
        database.addPlaylist(newPlaylist);

        this.commandMessage = "Playlist created successfully.";
    }

    private void doSwitchVisibility(int playlistId)
    {
        Playlist selectedPlaylist;

        if (playlistId > this.personalPlaylists.size()) {
            this.commandMessage = "The specified playlist ID is too high.";
            return;
        }

        selectedPlaylist = this.personalPlaylists.get(playlistId);
        selectedPlaylist.changeVisibility();

        this.commandMessage = "Visibility status updated successfully to " + selectedPlaylist.isVisible() + ".";
    }

    private void doFollowPlaylist()
    {
        Playlist selectedPlaylist;

        if (!this.searchbar.isSelected()) {
            this.commandMessage = "Please select a source before following or unfollowing.";
            return;
        }

        if (!this.searchbar.searchedForPlaylists()) {
            this.commandMessage = "The selected source is not a playlist.";
            return;
        }

        selectedPlaylist = this.searchbar.getSelectedPlaylist();

        if (this.personalPlaylists.contains(selectedPlaylist)) {
            this.commandMessage = "You cannot follow or unfollow your own playlist.";
            return;
        }

        
        if (this.followedPlaylists.contains(selectedPlaylist)) {
            this.followedPlaylists.remove(selectedPlaylist);
            selectedPlaylist.removeFollower();
            this.commandMessage = "Playlist unfollowed successfully.";
        }

        this.followedPlaylists.add(selectedPlaylist);
        selectedPlaylist.addFollower();
        this.commandMessage = "Playlist followed successfully.";
    }

    private ArrayList<PlaylistOutput> doShowPlaylists()
    {
        PlaylistOutput plOutput;
        ArrayList<PlaylistOutput> result = new ArrayList<>();

        for (var playlist : this.personalPlaylists) {
            plOutput = new PlaylistOutput();

            plOutput.setName(playlist.getName());
            plOutput.setSongs(playlist.getSongsNameList());
            
            if (playlist.isVisible())
                plOutput.setVisibility("public");
            else
                plOutput.setVisibility("private");

            plOutput.setFollowers(playlist.getNrOfFollowers());

            result.add(plOutput);
        }

        return result;
    }
}
