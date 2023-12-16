package gwaves.tools;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import fileio.output.MusicPlayerStatusOutput;

import gwaves.sample.Song;
import gwaves.sample.Episode;
import gwaves.collection.Playlist;
import gwaves.collection.Album;
import gwaves.collection.Podcast;

public final class Musicplayer {
    private static final Random RAND = new Random(0);

    private final int skipSec = 90;

    private int remainingTime;

    private int currentSongIndex;
    private Song currentSong;
    private Playlist currentPlaylist;

    private int currentEpisodeIndex;
    private Episode currentEpisode;
    private Podcast currentPodcast;
    private Album currentAlbum;

    private boolean paused;
    private int repeat;
    private boolean shuffle;

    private ArrayList<Integer> shuffledIndices;

    private HashMap<Podcast, PodcastSavedInfo> watchedPodcasts;

    public Musicplayer() {
        this.paused = true;
        this.shuffledIndices = new ArrayList<>();
        this.watchedPodcasts = new HashMap<>();
    }

    /**
     * Unloads the current loaded song/playlist/podcast
     * In case of a podcast being unloaded, its last episode and minute are
     * stored in order to continue from there upon a later load
     *
     * @return
     */
    public void unloadCurrent() {
        PodcastSavedInfo podcastSavedInfo;

        if (this.currentPodcast != null) {
            podcastSavedInfo = watchedPodcasts.get(this.currentPodcast);

            if (this.currentEpisodeIndex == this.currentPodcast.getNrOfEpisodes()) {
                this.currentEpisode = this.currentPodcast.getEpisode(0);
                podcastSavedInfo.setLastEpisodePlayedIndex(0);
                podcastSavedInfo.setLastEpisodePlayedRemTime(this.currentEpisode.getDuration());
            } else {
                podcastSavedInfo.setLastEpisodePlayedIndex(this.currentEpisodeIndex);
                podcastSavedInfo.setLastEpisodePlayedRemTime(this.remainingTime);
            }
        }

        this.currentSong = null;
        this.currentPlaylist = null;
        this.currentAlbum = null;
        this.currentEpisode = null;
        this.currentPodcast = null;
        this.remainingTime = 0;
        this.paused = true;
        this.repeat = 0;
        this.shuffle = false;
    }

    /**
     * Loads the song into the musicplayer
     *
     * @param song
     */
    public void loadSong(final Song song) {
        this.unloadCurrent();
        this.currentSong = song;
        this.remainingTime = this.currentSong.getDuration();
        this.paused = false;
    }

    /**
     * Loads the playlist into the musicplayer
     *
     * @param playlist
     */
    public void loadPlaylist(final Playlist playlist) {
        this.unloadCurrent();
        this.currentPlaylist = playlist;
        this.currentSongIndex = 0;
        this.currentSong = playlist.getSong(this.currentSongIndex);
        this.remainingTime = this.currentSong.getDuration();
        this.paused = false;
    }

    /**
     * Loads the podcast into the musicplayer
     *
     * @param podcast
     */
    public void loadPodcast(final Podcast podcast) {
        PodcastSavedInfo podcastSavedInfo;

        this.unloadCurrent();

        if (!this.watchedPodcasts.containsKey(podcast)) {
            podcastSavedInfo = new PodcastSavedInfo(0, podcast.getEpisode(0).getDuration());
            this.watchedPodcasts.put(podcast, podcastSavedInfo);
        } else {
            podcastSavedInfo = watchedPodcasts.get(podcast);
        }

        this.currentPodcast = podcast;
        this.currentEpisodeIndex = podcastSavedInfo.getLastEpisodePlayedIndex();
        this.currentEpisode = podcast.getEpisode(this.currentEpisodeIndex);
        this.remainingTime = podcastSavedInfo.getLastEpisodePlayedRemTime();
        this.paused = false;
    }

    /**
     * Loads the album into the musicplayer
     *
     * @param album
     */
    public void loadAlbum(final Album album) {
        this.unloadCurrent();
        this.currentAlbum = album;
        this.currentSongIndex = 0;
        this.currentSong = album.getSong(this.currentSongIndex);
        this.remainingTime = this.currentSong.getDuration();
        this.paused = false;
    }

    /**
     * Plays itself for {@code timeInterval} seconds. Basically changes the
     * state of the Musicplayer object in time, thus the current loaded audio
     * recording will change in time.
     *
     * @return
     */
    public void playFor(final int timeInterval) {
        int time = timeInterval;

        if (this.paused || !this.isLoaded()) {
            return;
        }

        while (this.isLoaded() && time >= this.remainingTime) {
            time -= this.remainingTime;
            this.next();
        }

        if (this.remainingTime > 0) {
            this.remainingTime -= time;
        }
    }

    /**
     * Moves through the episode with 90 seconds if a podcast is loaded
     * If the remaining time is less than 90 seconds it skips to the next
     * episode
     */
    public void forward() {
        if (this.paused || !this.isLoaded()) {
            return;
        }

        if (this.remainingTime <= skipSec) {
            switch (this.repeat) {
                case 0:
                    this.currentEpisodeIndex++;

                    if (this.currentEpisodeIndex == this.currentPodcast.getNrOfEpisodes()) {
                        this.unloadCurrent();
                        break;
                    }
                case 1:
                    this.repeat = 0;
                case 2:
                    this.currentEpisode = this.currentPodcast.getEpisode(this.currentEpisodeIndex);
                    this.remainingTime = this.currentEpisode.getDuration();
                    break;
                default:
                    break;
            }
        } else {
            this.remainingTime -= skipSec;
        }
    }

    /**
     * Moves back through the episode with 90 seconds if a podcast is loaded
     * If the remaining time is equal to the episode time it skips to the
     * previous episode
     */
    public void backward() {
        if (this.paused || !this.isLoaded()) {
            return;
        }

        if (this.currentEpisode.getDuration() - this.remainingTime < skipSec) {
            this.remainingTime = this.currentEpisode.getDuration();
        } else {
            this.remainingTime += skipSec;
        }
    }

    /**
     * Skips to the next track
     */
    public void next() {
        int songIndex;

        if (!this.isLoaded()) {
            return;
        }

        this.paused = false;

        if (this.repeat == 2) {
            if (this.currentSong != null) {
                this.remainingTime = this.currentSong.getDuration();
            } else {
                this.remainingTime = this.currentEpisode.getDuration();
            }

            return;
        }

        if (this.currentPlaylist != null) {
            this.currentSongIndex++;

            switch (this.repeat) {
                case 0:
                    if (this.currentSongIndex == this.currentPlaylist.getNrOfSongs()) {
                        this.unloadCurrent();
                        return;
                    }
                    break;
                case 1:
                    if (this.currentSongIndex == this.currentPlaylist.getNrOfSongs()) {
                        this.currentSongIndex = 0;
                    }
                    break;
                default:
                    break;
            }

            if (this.shuffle) {
                songIndex = this.shuffledIndices.get(this.currentSongIndex);
            } else {
                songIndex = this.currentSongIndex;
            }

            this.currentSong = this.currentPlaylist.getSong(songIndex);
            this.remainingTime = this.currentSong.getDuration();
        } else if (this.currentAlbum != null) {
            this.currentSongIndex++;

            switch (this.repeat) {
                case 0:
                    if (this.currentSongIndex == this.currentAlbum.getNrOfSongs()) {
                        this.unloadCurrent();
                        return;
                    }
                    break;
                case 1:
                    if (this.currentSongIndex == this.currentAlbum.getNrOfSongs()) {
                        this.currentSongIndex = 0;
                    }
                    break;
                default:
                    break;
            }

            if (this.shuffle) {
                songIndex = this.shuffledIndices.get(this.currentSongIndex);
            } else {
                songIndex = this.currentSongIndex;
            }

            this.currentSong = this.currentAlbum.getSong(songIndex);
            this.remainingTime = this.currentSong.getDuration();
        } else if (this.currentSong != null) {
            switch (this.repeat) {
                case 0:
                    this.unloadCurrent();
                    return;
                case 1:
                    this.repeat = 0;
                    break;
                default:
                    break;
            }

            this.remainingTime = this.currentSong.getDuration();
        } else if (this.currentPodcast != null) {
            switch (this.repeat) {
                case 0:
                    this.currentEpisodeIndex++;

                    if (this.currentEpisodeIndex == this.currentPodcast.getNrOfEpisodes()) {
                        this.unloadCurrent();
                        return;
                    }
                    this.currentEpisode = this.currentPodcast.getEpisode(this.currentEpisodeIndex);
                    break;
                case 1:
                    this.repeat = 0;
                    break;
                default:
                    break;
            }

            this.remainingTime = this.currentEpisode.getDuration();
        }
    }

    /**
     * Skips to the previous track
     */
    public void prev() {
        int songIndex;

        if (!this.isLoaded()) {
            return;
        }

        if (this.currentPlaylist != null) {
            if (this.remainingTime == this.currentSong.getDuration()
                    && this.currentSongIndex > 0) {
                this.currentSongIndex--;

                if (this.shuffle) {
                    songIndex = this.shuffledIndices.get(this.currentSongIndex);
                } else {
                    songIndex = this.currentSongIndex;
                }

                this.currentSong = this.currentPlaylist.getSong(songIndex);
            }

            this.remainingTime = this.currentSong.getDuration();
        } else if (this.currentAlbum != null) {
            if (this.remainingTime == this.currentSong.getDuration()
                    && this.currentSongIndex > 0) {
                this.currentSongIndex--;

                if (this.shuffle) {
                    songIndex = this.shuffledIndices.get(this.currentSongIndex);
                } else {
                    songIndex = this.currentSongIndex;
                }

                this.currentSong = this.currentAlbum.getSong(songIndex);
            }

            this.remainingTime = this.currentSong.getDuration();
        } else if (this.currentSong != null) {
            this.remainingTime = this.currentSong.getDuration();
        } else if (this.currentPodcast != null) {
            if (this.remainingTime == this.currentEpisode.getDuration()
                    && this.currentEpisodeIndex > 0) {
                this.currentEpisodeIndex--;
                this.currentEpisode = this.currentPodcast.getEpisode(this.currentEpisodeIndex);
            }

            this.remainingTime = currentEpisode.getDuration();
        }

        this.paused = false;
    }

    /**
     * Toggles between pause and play
     */
    public void togglePlayPause() {
        this.paused = !this.paused;
    }

    /**
     * Switches between the repeat modes
     */
    public void switchRepeat() {
        this.repeat = (this.repeat + 1) % 3;
    }

    /**
     * Toggles the shuffle option
     * Available only for playlists
     */
    public void toggleShuffle(final long seed) {
        int prevIndex;
        boolean prevShuffle;

        prevShuffle = this.shuffle;
        this.shuffle = !this.shuffle;

        if (!this.shuffle) {
            if (prevShuffle) {
                this.currentSongIndex = this.shuffledIndices.get(this.currentSongIndex);
            }
        } else {
            if (prevShuffle) {
                prevIndex = this.shuffledIndices.get(this.currentSongIndex);
            } else {
                prevIndex = this.currentSongIndex;
            }

            this.shuffledIndices.clear();

            if (this.currentPlaylist != null) {
                for (int i = 0; i < this.currentPlaylist.getNrOfSongs(); ++i) {
                    this.shuffledIndices.add(i);
                }
            } else if (this.currentAlbum != null) {
                for (int i = 0; i < this.currentAlbum.getNrOfSongs(); ++i) {
                    this.shuffledIndices.add(i);
                }
            }

            Musicplayer.RAND.setSeed(seed);
            Collections.shuffle(this.shuffledIndices, Musicplayer.RAND);

            this.currentSongIndex = this.shuffledIndices.indexOf(prevIndex);
        }
    }

    /**
     * @return name of current audio recording
     */
    public String getCurrentRecName() {
        if (this.currentSong != null) {
            return this.currentSong.getName();
        } else if (this.currentPodcast != null) {
            return this.currentEpisode.getName();
        }

        return null;
    }

    /**
     *
     * @return
     */
    public String getListeningColl() {
        if (this.currentPlaylist != null) {
            return this.currentPlaylist.getName();
        } else if (this.currentAlbum != null) {
            return this.currentAlbum.getName();
        } else if (this.currentPodcast != null) {
            return this.currentPodcast.getName();
        }

        return null;
    }

    /**
     *
     * @return
     */
    public String getListeningSong() {
        if (this.currentSong != null) {
            return this.currentSong.getName();
        }

        return null;
    }

    /**
     * @return current loaded song
     */
    public Song getLoadedSong() {
        return this.currentSong;
    }

    /**
     * @return current loaded playlist
     */
    public Playlist getLoadedPlaylist() {
        return this.currentPlaylist;
    }

    /**
     * @return current loaded playlist
     */
    public Album getLoadedAlbum() {
        return this.currentAlbum;
    }

    /**
     * @return current loaded episode
     */
    public Episode getLoadedEpisode() {
        return this.currentEpisode;
    }

    /**
     * @return current loaded podcast
     */
    public Podcast getLoadedPodcast() {
        return this.currentPodcast;
    }

    /**
     * @return the remaining time
     */
    public int getRemainedTime() {
        return this.remainingTime;
    }

    /**
     * @return the name of the current repeat mode
     */
    public String getRepeatModeName() {
        String result = "no repeat";

        switch (this.repeat) {
            case 1:
                if (this.isPlaylistLoaded()) {
                    result = "repeat all";
                } else if (this.isSongLoaded() || this.isPodcastLoaded()) {
                    result = "repeat once";
                }
                break;
            case 2:
                if (this.isPlaylistLoaded()) {
                    result = "repeat current song";
                } else if (this.isSongLoaded() || this.isPodcastLoaded()) {
                    result = "repeat infinite";
                }
                break;
            default:
                break;
        }

        return result;
    }

    /**
     * @return an object which describes the current state of the musicplayer
     */
    public MusicPlayerStatusOutput getStatus() {
        MusicPlayerStatusOutput playerStatus = new MusicPlayerStatusOutput();

        if (this.currentSong != null) {
            playerStatus.setName(this.currentSong.getName());
        } else if (this.currentEpisode != null) {
            playerStatus.setName(this.currentEpisode.getName());
        }

        playerStatus.setRemainedTime(this.remainingTime);

        if (this.repeat == 0) {
            playerStatus.setRepeat("No Repeat");
        } else {
            playerStatus.setRepeat("Repeat");
        }

        playerStatus.setShuffle(this.shuffle);
        playerStatus.setPaused(this.paused);

        return playerStatus;
    }

    /**
     * @return true if a song is loaded
     */
    public boolean isSongLoaded() {
        return this.currentSong != null;
    }

    /**
     * @return true if a Playlist is loaded
     */
    public boolean isPlaylistLoaded() {
        return this.currentPlaylist != null;
    }

    /**
     * @return true if a Playlist is loaded
     */
    public boolean isAlbumLoaded() {
        return this.currentAlbum != null;
    }

    /**
     * @return true if a podcast is loaded
     */
    public boolean isPodcastLoaded() {
        return this.currentPodcast != null;
    }

    /**
     * @return if anything is loaded
     */
    public boolean isLoaded() {
        return this.isSongLoaded() || this.isPlaylistLoaded()
                || this.isPodcastLoaded() || this.isAlbumLoaded();
    }

    /**
     * @return if the player is pause
     */
    public boolean isPaused() {
        return this.paused;
    }

    /**
     * @return if the player is on repeat
     */
    public boolean isOnRepeat() {
        return this.repeat != 0;
    }

    /**
     * @return if the shuffle option is active
     */
    public boolean isShuffled() {
        return this.shuffle;
    }
}

class PodcastSavedInfo {
    private int lastEpisodePlayedIndex;
    private int lastEpisodePlayedRemainedTime;

    PodcastSavedInfo() {

    }

    /**
     * @param lastEpisodePlayedIndex
     * @param remainedTime
     */
    PodcastSavedInfo(final int lastEpisodePlayedIndex, final int remainedTime) {
        this.lastEpisodePlayedIndex = lastEpisodePlayedIndex;
        this.lastEpisodePlayedRemainedTime = remainedTime;
    }

    /**
     * @param index
     */
    public void setLastEpisodePlayedIndex(final int index) {
        this.lastEpisodePlayedIndex = index;
    }

    /**
     * @param remainedTime
     */
    public void setLastEpisodePlayedRemTime(final int remainedTime) {
        this.lastEpisodePlayedRemainedTime = remainedTime;
    }

    /**
     * @return
     */
    public int getLastEpisodePlayedIndex() {
        return this.lastEpisodePlayedIndex;
    }

    /**
     * @return
     */
    public int getLastEpisodePlayedRemTime() {
        return this.lastEpisodePlayedRemainedTime;
    }
}
