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

    private int repeat;
    private boolean shuffle;

    private ArrayList<Integer> shuffledIndices;

    public Musicplayer()
    {
        this.remainingTime = -1;
        this.paused = true;
        this.shuffledIndices = new ArrayList<>();
    }

    public void removeSelected()
    {
        this.currentSong = null;
        this.currentPlaylist = null;
        this.currentPodcast = null;
        this.remainingTime = -1;
        this.paused = true;
        this.repeat = 0;
        this.shuffle = false;
    }

    public void setSelectedSong(Song song)
    {
        this.currentSong = song;
        this.currentPlaylist = null;
        this.currentPodcast = null;
    }

    public void setSelectedPlaylist(Playlist playlist)
    {
        this.currentPlaylist = playlist;
        this.currentSong = null;
        this.currentPodcast = null;
    }

    public void setSelectedPodcast(Podcast podcast)
    {
        this.currentPodcast = podcast;
        this.currentSong = null;
        this.currentPlaylist = null;
    }

    public void load()
    {
        if (this.currentSong != null) {
            this.remainingTime = this.currentSong.getDuration();
        } else if (this.currentPlaylist != null) {
            this.currentSongIndex = 0;
            this.remainingTime = this.currentPlaylist.getSong(this.currentSongIndex).getDuration();
        } else if (this.currentPodcast != null) {
            this.currentEpisodeIndex = 0;
            this.remainingTime = this.currentPodcast.getEpisode(this.currentEpisodeIndex).getDuration();
        }
    }

    public void play(int timeInterval)
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

    public void repeat()
    {
        this.repeat = (this.repeat + 1 ) % 3;
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
        if (this.paused || this.remainingTime == -1)
            return;

        if (this.currentSong != null) {
            switch (this.repeat) {
            case 0:
                this.removeSelected();
                break;
            case 1:
                this.repeat = 0;
            case 2:
                this.remainingTime = this.currentSong.getDuration();
                break;
            }
        } else if (this.currentPlaylist != null) {
            switch (this.repeat) {
            case 0:
                if (this.currentSongIndex == this.currentPlaylist.getNrOfSongs() - 1) {
                    this.removeSelected();
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
                    this.repeat = 0;
                } else {
                    this.currentSongIndex++;
                }

                if (this.shuffle)
                    this.remainingTime = this.currentPlaylist.getSong(this.shuffledIndices.get(this.currentSongIndex)).getDuration();
                else
                    this.remainingTime = this.currentPlaylist.getSong(this.currentSongIndex).getDuration();

                break;
            case 2:
                this.repeat = 0;

                if (this.shuffle)
                    this.remainingTime = this.currentPlaylist.getSong(this.shuffledIndices.get(this.currentSongIndex)).getDuration();
                else
                    this.remainingTime = this.currentPlaylist.getSong(this.currentSongIndex).getDuration();
                break;
            }
        } else if (this.currentPodcast != null) {
            switch (this.repeat) {
            case 0:
                if (this.currentEpisodeIndex == this.currentPodcast.getNrOfEpisodes() - 1) {
                    this.removeSelected();
                } else {
                    this.currentEpisodeIndex++;
                    this.remainingTime = this.currentPodcast.getEpisode(this.currentEpisodeIndex).getDuration();
                }
            case 1:
                this.repeat = 0;
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
    }

    public void likeCurrentSong()
    {
        if (this.currentSong != null)
            this.currentSong.addLike();
    }

    public void followCurrentPlaylist()
    {
        if (this.currentPlaylist != null)
            this.currentPlaylist.addFollower();
    }

    public MusicPlayerStatusOutput status()
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

        if (this.repeat == 0)
            playerStatus.setRepeat("No Repeat");
        else
            playerStatus.setRepeat("Repeat");

        playerStatus.setShuffle(this.shuffle);
        playerStatus.setPaused(this.paused);

        return playerStatus;
    }
}
