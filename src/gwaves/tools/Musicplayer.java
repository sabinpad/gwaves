package gwaves.tools;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import fileio.output.MusicPlayerStatusOutput;

import gwaves.sample.Song;
import gwaves.sample.AudioRec;
import gwaves.sample.Episode;
import gwaves.collection.Playlist;
import gwaves.collection.Album;
import gwaves.collection.AudioCollection;
import gwaves.collection.Podcast;
import gwaves.context.NormalUser;

public final class Musicplayer {
    private static final Random RAND = new Random(0);
    private static final int SKIPSEC = 90;

    private enum Type {
        NA,
        SONG,
        PLAYLIST,
        ALBUM,
        PODCAST
    }

    private NormalUser ownerUser;

    private Type loadedType;

    private int currentIndex;
    @Getter
    private AudioRec currentRec;
    @Getter
    private AudioCollection<?> currentCollec;

    private int remainingTime;

    private boolean paused;
    private int repeat;
    private boolean shuffle;

    private ArrayList<Integer> shuffledIndices;

    private HashMap<Podcast, PodcastSavedInfo> watchedPodcasts;

    public Musicplayer(NormalUser ownerUser) {
        this.ownerUser = ownerUser;
        this.loadedType = Musicplayer.Type.NA;
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

        if (this.loadedType == Musicplayer.Type.PODCAST) {
            podcastSavedInfo = watchedPodcasts.get((Podcast)this.currentCollec);

            if (this.currentIndex == this.currentCollec.getNrOfAudRecs()) {
                this.currentRec = this.currentCollec.getAudRec(0);
                podcastSavedInfo.setLastEpisodePlayedIndex(0);
                podcastSavedInfo.setLastEpisodePlayedRemainingTime(this.currentRec.getDuration());
            } else {
                podcastSavedInfo.setLastEpisodePlayedIndex(this.currentIndex);
                podcastSavedInfo.setLastEpisodePlayedRemainingTime(this.remainingTime);
            }
        }

        this.currentRec = null;
        this.currentCollec = null;
        this.loadedType = Musicplayer.Type.NA;
        this.currentIndex = 0;
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
        this.currentRec = song;
        this.remainingTime = this.currentRec.getDuration();
        this.loadedType = Musicplayer.Type.SONG;
        this.paused = false;

        this.updateStatistics();
    }

    /**
     * Loads the playlist into the musicplayer
     *
     * @param playlist
     */
    public void loadPlaylist(final Playlist playlist) {
        this.unloadCurrent();
        this.currentCollec = playlist;
        this.currentIndex = 0;
        this.currentRec = playlist.getAudRec(this.currentIndex);
        this.remainingTime = this.currentRec.getDuration();
        this.loadedType = Musicplayer.Type.PLAYLIST;
        this.paused = false;

        this.updateStatistics();
    }

    /**
     * Loads the album into the musicplayer
     *
     * @param album
     */
    public void loadAlbum(final Album album) {
        this.unloadCurrent();
        this.currentCollec = album;
        this.currentIndex = 0;
        this.currentRec = album.getAudRec(this.currentIndex);
        this.remainingTime = this.currentRec.getDuration();
        this.loadedType = Musicplayer.Type.ALBUM;
        this.paused = false;

        this.updateStatistics();
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
            podcastSavedInfo = new PodcastSavedInfo(0, podcast.getAudRec(0).getDuration());
            this.watchedPodcasts.put(podcast, podcastSavedInfo);
        } else {
            podcastSavedInfo = watchedPodcasts.get(podcast);
        }

        this.currentCollec = podcast;
        this.currentIndex = podcastSavedInfo.getLastEpisodePlayedIndex();
        this.currentRec = podcast.getAudRec(this.currentIndex);
        this.remainingTime = podcastSavedInfo.getLastEpisodePlayedRemainingTime();
        this.loadedType = Musicplayer.Type.PODCAST;
        this.paused = false;

        this.updateStatistics();
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

        if (this.remainingTime <= Musicplayer.SKIPSEC) {
            switch (this.repeat) {
                case 0:
                    this.currentIndex++;

                    if (this.currentIndex == this.currentCollec.getNrOfAudRecs()) {
                        this.unloadCurrent();
                        break;
                    }
                case 1:
                    this.repeat = 0;
                case 2:
                    this.currentRec = this.currentCollec.getAudRec(this.currentIndex);
                    this.remainingTime = this.currentRec.getDuration();
                    break;
                default:
                    break;
            }
        } else {
            this.remainingTime -= Musicplayer.SKIPSEC;
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

        if (this.currentRec.getDuration() - this.remainingTime < Musicplayer.SKIPSEC) {
            this.remainingTime = this.currentRec.getDuration();
        } else {
            this.remainingTime += Musicplayer.SKIPSEC;
        }
    }

    public void next() {
        int index;

        if (!this.isLoaded()) {
            return;
        }

        this.paused = false;

        switch (this.repeat) {
            case 0:
                switch (this.loadedType) {
                    case SONG:
                        this.unloadCurrent();
                        return;
                    case PLAYLIST:
                    case ALBUM:
                    case PODCAST:
                        this.currentIndex++;
                        if (this.currentIndex == this.currentCollec.getNrOfAudRecs()) {
                            this.unloadCurrent();
                            return;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                switch (this.loadedType) {
                    case SONG:
                    case PODCAST:
                        this.repeat = 0;
                        this.remainingTime = this.currentRec.getDuration();
                        return;
                    case PLAYLIST:
                    case ALBUM:
                        this.currentIndex++;
                        if (this.currentIndex == this.currentCollec.getNrOfAudRecs()) {
                            this.currentIndex = 0;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                this.remainingTime = this.currentRec.getDuration();
                return;
            default:
                break;
        }

        if (this.loadedType != Musicplayer.Type.PODCAST && this.shuffle) {
            index = this.shuffledIndices.get(this.currentIndex);
        } else {
            index = this.currentIndex;
        }

        this.currentRec = this.currentCollec.getAudRec(index);
        this.remainingTime = this.currentRec.getDuration();

        this.updateStatistics();
    }

    public void prev() {
        int index;

        switch (this.loadedType) {
            case SONG:
                break;
            case PLAYLIST:
            case ALBUM:
                if (this.remainingTime == this.currentRec.getDuration() && this.currentIndex > 0) {
                    this.currentIndex--;

                    if (this.shuffle) {
                        index = this.shuffledIndices.get(this.currentIndex);
                    } else {
                        index = this.currentIndex;
                    }

                    this.currentRec = this.currentCollec.getAudRec(index);
                }
                break;
            case PODCAST:
                if (this.remainingTime == this.currentRec.getDuration() && this.currentIndex > 0) {
                    this.currentIndex--;
                    this.currentRec = this.currentCollec.getAudRec(this.currentIndex);
                }
                break;
            default:
                break;
        }

        this.remainingTime = this.currentRec.getDuration();

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
                this.currentIndex = this.shuffledIndices.get(this.currentIndex);
            }
        } else {
            if (prevShuffle) {
                prevIndex = this.shuffledIndices.get(this.currentIndex);
            } else {
                prevIndex = this.currentIndex;
            }

            this.shuffledIndices.clear();

            for (int i = 0; i < this.currentCollec.getNrOfAudRecs(); ++i) {
                this.shuffledIndices.add(i);
            }

            Musicplayer.RAND.setSeed(seed);
            Collections.shuffle(this.shuffledIndices, Musicplayer.RAND);

            this.currentIndex = this.shuffledIndices.indexOf(prevIndex);
        }
    }

    private void updateStatistics() {
        if (this.currentRec == null) {
            return;
        }

        if (this.loadedType == Musicplayer.Type.PODCAST) {
            Episode currentEpisode = (Episode)this.currentRec;

            currentEpisode.addListen();
            // TODO sa adaug listen la host
        } else {
            Song currentSong = (Song)this.currentRec;

            currentSong.addListen();
            currentSong.getArtist().addListen(this.ownerUser);

            this.ownerUser.updateStatistics(currentSong);

            if (this.loadedType == Musicplayer.Type.ALBUM) {
                if (this.currentIndex == 0) {
                    ((Album)this.currentCollec).addListen();
                }

                this.ownerUser.updateStatistics((Album)currentCollec);
            }
        }
    }

    /**
     * @return current loaded song
     */
    public Song getLoadedSong() {
        return (Song)this.currentRec;
    }

    /**
     * @return current loaded playlist
     */
    public Playlist getLoadedPlaylist() {
        return (Playlist)this.currentCollec;
    }

    /**
     * @return current loaded playlist
     */
    public Album getLoadedAlbum() {
        return (Album)this.currentCollec;
    }

    /**
     * @return current loaded episode
     */
    public Episode getLoadedEpisode() {
        return (Episode)this.currentRec;
    }

    /**
     * @return current loaded podcast
     */
    public Podcast getLoadedPodcast() {
        return (Podcast)this.currentCollec;
    }

    /**
     * @return the remaining time
     */
    public int getRemainingTime() {
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

        if (this.currentRec != null) {
            playerStatus.setName(this.currentRec.getName());
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
        return this.loadedType == Musicplayer.Type.SONG
               || this.loadedType == Musicplayer.Type.PLAYLIST
               || this.loadedType == Musicplayer.Type.ALBUM;
    }

    /**
     * @return true if a Playlist is loaded
     */
    public boolean isPlaylistLoaded() {
        return this.loadedType == Musicplayer.Type.PLAYLIST;
    }

    /**
     * @return true if a Playlist is loaded
     */
    public boolean isAlbumLoaded() {
        return this.loadedType == Musicplayer.Type.ALBUM;
    }

    /**
     * @return true if a podcast is loaded
     */
    public boolean isPodcastLoaded() {
        return this.loadedType == Musicplayer.Type.PODCAST;
    }

    /**
     * @return if anything is loaded
     */
    public boolean isLoaded() {
        return this.currentRec != null || this.currentCollec != null;
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

@Getter @Setter
class PodcastSavedInfo {
    private int lastEpisodePlayedIndex;
    private int lastEpisodePlayedRemainingTime;

    PodcastSavedInfo() {

    }

    /**
     * @param lastEpisodePlayedIndex
     * @param remainedTime
     */
    PodcastSavedInfo(final int lastEpisodePlayedIndex, final int remainedTime) {
        this.lastEpisodePlayedIndex = lastEpisodePlayedIndex;
        this.lastEpisodePlayedRemainingTime = remainedTime;
    }
}
