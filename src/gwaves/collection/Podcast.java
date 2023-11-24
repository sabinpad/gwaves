package gwaves.collection;

import java.util.ArrayList;

import fileio.input.FilterInput;
import fileio.input.PodcastInput;

import gwaves.sample.Episode;

public final class Podcast extends AudioCollection {
    private ArrayList<Episode> episodes;
    private int lastEpisodePlayedIndex;
    private int lastEpisodePlayedRemainedTime;

    public Podcast(final PodcastInput podcastInput) {
        super(podcastInput.getName(), podcastInput.getOwner());

        this.episodes = new ArrayList<>();

        for (var episodeInput : podcastInput.getEpisodes()) {
            this.episodes.add(new Episode(episodeInput));
            this.entireDuration += episodeInput.getDuration();
        }

        this.lastEpisodePlayedRemainedTime = episodes.get(0).getDuration();
    }

    /**
     * @param index
     */
    public void setLastEpisodePlayedIndex(final int index) {
        if (index < this.episodes.size()) {
            this.lastEpisodePlayedIndex = index;
        }
    }

    /**
     * @param remainedTime
     */
    public void setLastEpisodePlayedRemainedTime(final int remainedTime) {
        if (remainedTime <= this.episodes.get(this.lastEpisodePlayedIndex).getDuration()) {
            this.lastEpisodePlayedRemainedTime = remainedTime;
        }
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
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
    public int getLastEpisodePlayedRemainedTime() {
        return this.lastEpisodePlayedRemainedTime;
    }

    /**
     * @return
     */
    public int getNrOfEpisodes() {
        return this.episodes.size();
    }

    /**
     * @param episode
     * @return
     */
    public int getEpisodeNr(final Episode episode) {
        return this.episodes.indexOf(episode);
    }

    /**
     * @param number
     * @return
     */
    public Episode getEpisode(final int number) {
        if (number >= this.episodes.size()) {
            return null;
        }

        return this.episodes.get(number);
    }

    /**
     * @param episode
     * @return
     */
    public Episode getEpisodeAfter(final Episode episode) {
        int i;

        i = (this.episodes.indexOf(episode) + 1) % this.episodes.size();

        return this.episodes.get(i);
    }

    /**
     * @param episode
     * @return
     */
    public Episode getEpisodeBefore(final Episode episode) {
        int i;

        i = (this.episodes.indexOf(episode) - 1) % this.episodes.size();

        if (i == -1) {
            i = this.episodes.size() - 1;
        }

        return this.episodes.get(i);
    }

    /**
     * @param episode
     * @return
     */
    public boolean isFirst(final Episode episode) {
        return (this.episodes.indexOf(episode) == 0);
    }

    /**
     * @param episode
     * @return
     */
    public boolean isLast(final Episode episode) {
        return (this.episodes.indexOf(episode) == (this.episodes.size() - 1));
    }

    /**
     * @param filter
     * @return
     */
    public boolean isMatchedByFilter(final FilterInput filter) {
        if (filter.getName() != null) {
            if (!this.name.startsWith(filter.getName())) {
                return false;
            }
        }

        if (filter.getOwner() != null) {
            if (!this.owner.equals(filter.getOwner())) {
                return false;
            }
        }

        return true;
    }
}
