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
    private Playlist currentPlaylist;

    private Episode currentEpisode;
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
            this.currentSong = this.currentPlaylist.getSong(0);
            this.remainingTime = this.currentSong.getDuration();
        } else if (this.currentPodcast != null) {
            this.currentEpisode = this.currentPodcast.getEpisode(0);
            this.remainingTime = this.currentEpisode.getDuration();
        }
    }

    public void play(int timeInterval)
    {
        if (this.paused)
            return;

        while (timeInterval >= this.remainingTime) {
            this.next();
            // timeInterval -= this.remainingTime;

            // if (this.currentPlaylist != null) {
            //     // this.currentSong = nextSongFromPlaylist();
            //     if (this.shuffle) {

            //     } else {
            //         if (this.repeat == 0) {
            //             if (this.currentPlaylist.isLast(currentSong)) {
            //                 this.currentSong = null;
            //                 this.currentPlaylist = null;
            //                 this.remainingTime = -1;
            //                 return;
            //             }
            //             this.currentSong = this.currentPlaylist.getSongAfter(this.currentSong);
            //         } else if (this.repeat == 1) {
            //             this.repeat = 0;
            //         }
            //     }

            //     this.remainingTime = this.currentSong.getDuration();
            // } else if (this.currentPodcast != null) {
            //     if (this.repeat == 0) {
            //         if (this.currentPodcast.isLast(currentEpisode)) {
            //             this.currentEpisode = null;
            //             this.currentPodcast = null;
            //             this.remainingTime = -1;
            //             return;
            //         }
            //         this.currentEpisode = this.currentPodcast.getEpisodeAfter(this.currentEpisode);
            //     } else if (this.repeat == 1) {
            //         this.repeat = 0;
            //     } else {
            //         this.currentEpisode = this.currentPodcast.getEpisodeAfter(this.currentEpisode);
            //     }

            //     this.remainingTime = this.currentEpisode.getDuration();
            // } else {
            //     if (this.repeat == 0) {
            //         this.currentSong = null;
            //         this.remainingTime = -1;
            //         return;
            //     } else if (this.repeat == 1) {
            //         this.repeat = 0;
            //         this.remainingTime = this.currentSong.getDuration();
            //     }

            // }
        }

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

        if (!this.shuffle)
            return;

        this.shuffledIndices.clear();

        for (int i = 0; i < this.currentPlaylist.getNrOfSongs(); ++i)
            this.shuffledIndices.add(i);

        Musicplayer.rand.setSeed(seed);
        Collections.shuffle(this.shuffledIndices, Musicplayer.rand);
    }

    public void forward()
    {
        if (this.remainingTime < 90) {
            this.currentEpisode = this.currentPodcast.getEpisodeAfter(currentEpisode);
            this.remainingTime = this.currentEpisode.getDuration();
        } else {
            this.remainingTime += 90;
        }
    }

    public void backward()
    {
        if (this.currentEpisode.getDuration() - this.remainingTime < 90)
            this.remainingTime = this.currentEpisode.getDuration();
        else
            this.remainingTime -= 90;
    }

    public void next()
    {

    }

    public void prev()
    {
        if (this.currentPlaylist != null) {
            if (this.remainingTime < this.currentSong.getDuration())
                this.remainingTime = this.currentSong.getDuration();
            else if (this.currentPlaylist.isFirst(currentSong))
                this.remainingTime = this.currentSong.getDuration();
            else
                this.currentSong = this.currentPlaylist.getSongBefore(this.currentSong);
        } else if (this.currentPodcast != null) {
            if (this.remainingTime < this.currentEpisode.getDuration())
                this.remainingTime = this.currentEpisode.getDuration();
            else if (this.currentPodcast.isFirst(currentEpisode))
                this.remainingTime = this.currentEpisode.getDuration();
            else
                this.currentEpisode = this.currentPodcast.getEpisodeBefore(this.currentEpisode);
        } else {
            this.remainingTime = this.currentSong.getDuration();
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

        if (this.currentSong != null)
            playerStatus.setName(this.currentSong.getName());
        else if (this.currentEpisode != null)
            playerStatus.setName(this.currentEpisode.getName());

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
