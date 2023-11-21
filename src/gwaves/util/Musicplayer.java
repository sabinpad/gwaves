package gwaves.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fileio.output.MusicPlayerStatusOutput;

import gwaves.sample.Song;
import gwaves.sample.Episode;
import gwaves.collection.Playlist;
import gwaves.collection.Podcast;

public class Musicplayer {
    private static final Random rand = new Random(0);

    private Song currentSong;

    private int currentSongIndex;
    private Playlist currentPlaylist;

    private int currentEpisodeIndex;
    private Podcast currentPodcast;

    private int remainingTime;

    private boolean paused;
    private int repeatMode;
    private boolean shuffle;

    private ArrayList<Integer> shuffledIndices;

    public Musicplayer()
    {
        this.paused = true;
        this.shuffledIndices = new ArrayList<>();
    }

    public void unloadCurrent()
    {
        this.currentSong = null;
        this.currentPlaylist = null;
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
        this.remainingTime = this.currentPlaylist.getSong(this.currentSongIndex).getDuration();
        this.paused = false;
    }

    public void loadPodcast(Podcast episode)
    {
        this.unloadCurrent();
    }

    public void playFor(int timeInterval)
    {
        if (this.paused)
            return;

        while (timeInterval >= this.remainingTime) {
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
        this.shuffle = !this.shuffle;

        if (!this.shuffle) {
            this.shuffledIndices.clear();
            return;
        }

        for (int i = 0; i < this.currentPlaylist.getNrOfSongs(); ++i)
            this.shuffledIndices.add(i);

        Musicplayer.rand.setSeed(seed);
        Collections.shuffle(this.shuffledIndices, Musicplayer.rand);
    }

    public void forward()
    {
        Episode currentEpisode;

        if (this.remainingTime < 90) {
            this.currentEpisodeIndex++;
            currentEpisode = this.currentPodcast.getEpisode(this.currentEpisodeIndex);
            this.remainingTime = currentEpisode.getDuration();
        } else {
            this.remainingTime -= 90;
        }
    }

    public void backward()
    {
        Episode currentEpisode = this.currentPodcast.getEpisode(this.currentEpisodeIndex);

        if (currentEpisode.getDuration() - this.remainingTime < 90)
            this.remainingTime = currentEpisode.getDuration();
        else
            this.remainingTime += 90;
    }

    public void next()
    {
        if (this.paused || !this.isLoaded())
            return;

        if (this.currentSong != null) {
            switch (this.repeatMode) {
            case 0:
                this.unloadCurrent();
                break;
            case 1:
                this.repeatMode = 0;
            case 2:
                this.remainingTime = this.currentSong.getDuration();
                break;
            }
        } else if (this.currentPlaylist != null) {
            switch (this.repeatMode) {
            case 0:
                if (this.currentSongIndex == this.currentPlaylist.getNrOfSongs() - 1) {
                    this.unloadCurrent();
                } else {
                    this.currentSongIndex++;

                    if (this.shuffle)
                        this.remainingTime = this.currentPlaylist.getSong(this.shuffledIndices.get(this.currentSongIndex)).getDuration();
                    else
                        this.remainingTime = this.currentPlaylist.getSong(this.currentSongIndex).getDuration();
                }
                break;
            case 1:
                if (this.currentSongIndex == this.currentPlaylist.getNrOfSongs() - 1) {
                    this.currentSongIndex = 0;
                    this.repeatMode = 0;
                } else {
                    this.currentSongIndex++;
                }

                if (this.shuffle)
                    this.remainingTime = this.currentPlaylist.getSong(this.shuffledIndices.get(this.currentSongIndex)).getDuration();
                else
                    this.remainingTime = this.currentPlaylist.getSong(this.currentSongIndex).getDuration();

                break;
            case 2:
                this.repeatMode = 0;

                if (this.shuffle)
                    this.remainingTime = this.currentPlaylist.getSong(this.shuffledIndices.get(this.currentSongIndex)).getDuration();
                else
                    this.remainingTime = this.currentPlaylist.getSong(this.currentSongIndex).getDuration();
                break;
            }
        } else if (this.currentPodcast != null) {
            switch (this.repeatMode) {
            case 0:
                if (this.currentEpisodeIndex == this.currentPodcast.getNrOfEpisodes() - 1) {
                    this.unloadCurrent();
                } else {
                    this.currentEpisodeIndex++;
                    this.remainingTime = this.currentPodcast.getEpisode(this.currentEpisodeIndex).getDuration();
                }
            case 1:
                this.repeatMode = 0;
            case 2:
                this.remainingTime = this.currentPodcast.getEpisode(this.currentEpisodeIndex).getDuration();
                break;
            }
        }
    }

    public void prev()
    {
        Song currentSong;
        Episode currentEpisode;

        if (this.currentSong != null) {
            this.remainingTime = this.currentSong.getDuration();
        } else if (this.currentPlaylist != null) {
            currentSong = this.currentPlaylist.getSong(this.currentSongIndex);

            if (this.remainingTime == currentSong.getDuration() && this.currentSongIndex > 0) {
                this.currentSongIndex--;
                currentSong = this.currentPlaylist.getSong(this.currentSongIndex);
            }

            this.remainingTime = currentSong.getDuration();
        } else if (this.currentPodcast != null) {
            currentEpisode = this.currentPodcast.getEpisode(this.currentEpisodeIndex);

            if (this.remainingTime == currentEpisode.getDuration() && this.currentEpisodeIndex > 0) {
                this.currentEpisodeIndex--;
                currentEpisode = this.currentPodcast.getEpisode(this.currentEpisodeIndex);
            }

            this.remainingTime = currentEpisode.getDuration();
        }

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

    public Podcast getLoadedPodcast()
    {
        return this.currentPodcast;
    }

    public String getCurrentRecName()
    {
        int shuffledIndex = 0;
        String name;

        if (!this.isLoaded())
            return null;

        if (this.currentSong != null) {
            name = this.currentSong.getName();
        } else if (this.currentPlaylist != null) {
            if (this.isShuffled()) {
                shuffledIndex = this.shuffledIndices.get(this.currentSongIndex);
                name = this.currentPlaylist.getSong(shuffledIndex).getName();
            } else {
                name = this.currentPlaylist.getSong(this.currentSongIndex).getName();
            }
        } else {
            name = this.currentPodcast.getEpisode(this.currentEpisodeIndex).getName();
        }

        return name;
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

        if (this.currentSong != null) {
            playerStatus.setName(this.currentSong.getName());
        } else if (this.currentPlaylist != null) {
            if (this.shuffle)
                playerStatus.setName(this.currentPlaylist.getSong(this.shuffledIndices.get(this.currentSongIndex)).getName());
            else
                playerStatus.setName(this.currentPlaylist.getSong(this.currentSongIndex).getName());
        } else if (this.currentPodcast != null) {
            playerStatus.setName(this.currentPodcast.getEpisode(this.currentEpisodeIndex).getName());
        }

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
