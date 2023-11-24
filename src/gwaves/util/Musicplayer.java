package gwaves.util;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import fileio.output.MusicPlayerStatusOutput;

import gwaves.sample.Song;
import gwaves.sample.Episode;
import gwaves.collection.Playlist;
import gwaves.collection.Podcast;

public class Musicplayer {
    private static final Random rand = new Random(0);

    private int currentSongIndex;
    private Song currentSong;
    private Playlist currentPlaylist;

    private int currentEpisodeIndex;
    private Episode currentEpisode;
    private Podcast currentPodcast;

    private int remainingTime;

    private boolean paused;
    private int repeatMode;
    private boolean shuffle;

    private ArrayList<Integer> shuffledIndices;

    private HashMap<Podcast, PodcastSavedInfo> watchedPodcasts;

    public Musicplayer()
    {
        this.paused = true;
        this.shuffledIndices = new ArrayList<>();
        this.watchedPodcasts = new HashMap<>();
    }

    public void playFor(int timeInterval)
    {
        if (this.paused || !this.isLoaded())
            return;

        while (this.isLoaded() && timeInterval >= this.remainingTime) {
            timeInterval -= this.remainingTime;
            this.next();
        }


        if (this.remainingTime > 0)
            this.remainingTime -= timeInterval;
    }

    public void playPause()
    {
        this.paused = !this.paused;
    }

    public void changeRepeatMode()
    {
        this.repeatMode = (this.repeatMode + 1 ) % 3;
    }

    public void shufflePlaylist(long seed)
    {
        int prevIndex;
        boolean prevShuffle;

        prevShuffle = this.shuffle;
        this.shuffle = !this.shuffle;

        if (!this.shuffle) {
            if (prevShuffle)
                this.currentSongIndex = this.shuffledIndices.get(this.currentSongIndex);
        } else {
            if (prevShuffle)
                prevIndex = this.shuffledIndices.get(this.currentSongIndex);
            else
                prevIndex = this.currentSongIndex;

            this.shuffledIndices.clear();

            for (int i = 0; i < this.currentPlaylist.getNrOfSongs(); ++i)
                this.shuffledIndices.add(i);

            Musicplayer.rand.setSeed(seed);
            Collections.shuffle(this.shuffledIndices, Musicplayer.rand);

            this.currentSongIndex = this.shuffledIndices.indexOf(prevIndex);
        }
    }

    public void forward()
    {
        if (this.paused || !this.isLoaded())
            return;

        if (this.remainingTime <= 90) {
            switch (this.repeatMode) {
            case 0:
                this.currentEpisodeIndex++;

                if (this.currentEpisodeIndex == this.currentPodcast.getNrOfEpisodes()) {
                    this.unloadCurrent();
                    break;
                }
            case 1:
                this.repeatMode = 0;
            case 2:
                this.currentEpisode = this.currentPodcast.getEpisode(this.currentEpisodeIndex);
                this.remainingTime = this.currentEpisode.getDuration();
                break;
            }
        } else {
            this.remainingTime -= 90;
        }
    }

    public void backward()
    {
        if (this.paused || !this.isLoaded())
            return;

        if (this.currentEpisode.getDuration() - this.remainingTime < 90)
            this.remainingTime = this.currentEpisode.getDuration();
        else
            this.remainingTime += 90;
    }

    public void next()
    {
        int songIndex;

        if (!this.isLoaded())
            return;

        this.paused = false;

        if (this.repeatMode == 2) {
            if (this.currentSong != null)
                this.remainingTime = this.currentSong.getDuration();
            else
                this.remainingTime = this.currentEpisode.getDuration();

            return;
        }

        if (this.currentPlaylist != null) {
            this.currentSongIndex++;

            switch (this.repeatMode) {
            case 0:
                if (this.currentSongIndex == this.currentPlaylist.getNrOfSongs()) {
                    this.unloadCurrent();
                    return;
                }

                break;
            case 1:
                if (this.currentSongIndex == this.currentPlaylist.getNrOfSongs())
                    this.currentSongIndex = 0;
                
                break;
            }

            if (this.shuffle)
                songIndex = this.shuffledIndices.get(this.currentSongIndex);
            else
                songIndex = this.currentSongIndex;

            this.currentSong = this.currentPlaylist.getSong(songIndex);
            this.remainingTime = this.currentSong.getDuration();
        } else if (this.currentSong != null) {
            switch (this.repeatMode) {
            case 0:
                this.unloadCurrent();
                return;
            case 1:
                this.repeatMode = 0;
                break;
            }

            this.remainingTime = this.currentSong.getDuration();
        } else if (this.currentPodcast != null) {
            switch (this.repeatMode) {
            case 0:
                this.currentEpisodeIndex++;

                if (this.currentEpisodeIndex == this.currentPodcast.getNrOfEpisodes()) {
                    this.unloadCurrent();
                    return;
                }

                this.currentEpisode = this.currentPodcast.getEpisode(this.currentEpisodeIndex);
                break;
            case 1:
                this.repeatMode = 0;
                break;
            }

            this.remainingTime = this.currentEpisode.getDuration();
        }
    }

    public void prev()
    {
        int songIndex;

        if (!this.isLoaded())
            return;

        if (this.currentPlaylist != null) {
            if (this.remainingTime == this.currentSong.getDuration() 
                && this.currentSongIndex > 0) {
                this.currentSongIndex--;

                if (this.shuffle)
                    songIndex = this.shuffledIndices.get(this.currentSongIndex);
                else
                    songIndex = this.currentSongIndex;

                this.currentSong = this.currentPlaylist.getSong(songIndex);
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

    public void unloadCurrent()
    {
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
        this.currentEpisode = null;
        this.currentPodcast = null;
        this.remainingTime = 0;
        this.paused = true;
        this.repeatMode = 0;
        this.shuffle = false;
    }

    public void loadSong(Song song)
    {
        this.unloadCurrent();
        this.currentSong = song;
        this.remainingTime = this.currentSong.getDuration();
        this.paused = false;
    }

    public void loadPlaylist(Playlist playlist)
    {
        this.unloadCurrent();
        this.currentPlaylist = playlist;
        this.currentSongIndex = 0;
        this.currentSong = playlist.getSong(this.currentSongIndex);
        this.remainingTime = this.currentSong.getDuration();
        this.paused = false;
    }

    public void loadPodcast(Podcast podcast)
    {
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

    public Song getLoadedSong()
    {   
        return this.currentSong;
    }

    public Playlist getLoadedPlaylist()
    {
        return this.currentPlaylist;
    }

    public Episode getLoadedEpisode()
    {
        return this.currentEpisode;
    }

    public Podcast getLoadedPodcast()
    {
        return this.currentPodcast;
    }

    public String getCurrentRecName()
    {
        if (this.currentSong != null)
            return this.currentSong.getName();
        else if (this.currentPodcast != null)
            return this.currentEpisode.getName();

        return null;
    }

    public int getRemainedTime()
    {
        return this.remainingTime;
    }

    public String getRepeatModeName()
    {
        String result = "no repeat";

        switch (this.repeatMode) {
        case 1:
            if (this.isPlaylistLoaded())
                result = "repeat all";
            else if (this.isSongLoaded() || this.isPodcastLoaded())
                result = "repeat once";
            break;
        case 2:
            if (this.isPlaylistLoaded())
                result = "repeat current song";
            else if (this.isSongLoaded() || this.isPodcastLoaded())
                result = "repeat infinite";
            break;
        }

        return result;
    }

    public boolean isSongLoaded()
    {
        return this.currentSong != null;
    }

    public boolean isPlaylistLoaded()
    {
        return this.currentPlaylist != null;
    }

    public boolean isPodcastLoaded()
    {
        return this.currentPodcast != null;
    }

    public boolean isLoaded()
    {
        return this.isSongLoaded() || this.isPlaylistLoaded() || this.isPodcastLoaded();
    }

    public boolean isPaused()
    {
        return this.paused;
    }

    public boolean isOnRepeat()
    {
        return this.repeatMode != 0;
    }

    public boolean isShuffled()
    {
        return this.shuffle;
    }

    public MusicPlayerStatusOutput getStatus()
    {
        MusicPlayerStatusOutput playerStatus = new MusicPlayerStatusOutput();

        if (this.currentSong != null)
            playerStatus.setName(this.currentSong.getName());
        else if (this.currentEpisode != null)
            playerStatus.setName(this.currentEpisode.getName());

        playerStatus.setRemainedTime(this.remainingTime);

        if (this.repeatMode == 0)
            playerStatus.setRepeat("No Repeat");
        else
            playerStatus.setRepeat("Repeat");

        playerStatus.setShuffle(this.shuffle);
        playerStatus.setPaused(this.paused);

        return playerStatus;
    }
}

class PodcastSavedInfo {
    private int lastEpisodePlayedIndex;
    private int lastEpisodePlayedRemainedTime;

    public PodcastSavedInfo()
    {

    }

    public PodcastSavedInfo(int lastEpisodePlayedIndex, int remainedTime)
    {
        this.lastEpisodePlayedIndex = lastEpisodePlayedIndex;
        this.lastEpisodePlayedRemainedTime = remainedTime;
    }

    public void setLastEpisodePlayedIndex(int index)
    {
        this.lastEpisodePlayedIndex = index;
    }

    public void setLastEpisodePlayedRemTime(int remainedTime)
    {
        this.lastEpisodePlayedRemainedTime = remainedTime;
    }

    public int getLastEpisodePlayedIndex()
    {
        return this.lastEpisodePlayedIndex;
    }

    public int getLastEpisodePlayedRemTime()
    {
        return this.lastEpisodePlayedRemainedTime;
    }
}
