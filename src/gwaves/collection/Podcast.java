package gwaves.collection;

import java.util.ArrayList;

import fileio.input.PodcastInput;

import gwaves.sample.Episode;
import gwaves.util.Filter;

public class Podcast extends AudioCollection {
    private ArrayList<Episode> episodes;
    private int lastEpisodePlayedIndex;
    private int lastEpisodePlayedRemainedTime;

    public Podcast(PodcastInput podcastInput)
    {
        super(podcastInput.getName(), podcastInput.getOwner());

        this.episodes = new ArrayList<>();

        for (var episodeInput : podcastInput.getEpisodes()) {
            this.episodes.add(new Episode(episodeInput));
            this.entireDuration += episodeInput.getDuration();
        }

        this.lastEpisodePlayedRemainedTime = episodes.get(0).getDuration();
    }

    public void setLastEpisodePlayedIndex(int index)
    {
        if (index < this.episodes.size())
            this.lastEpisodePlayedIndex = index;
    }

    public void setLastEpisodePlayedRemainedTime(int remainedTime)
    {
        if (remainedTime <= this.episodes.get(this.lastEpisodePlayedIndex).getDuration())
            this.lastEpisodePlayedRemainedTime = remainedTime;
    }

    public String getName()
    {
        return this.name;
    }
    
    public int getLastEpisodePlayedIndex()
    {
        return this.lastEpisodePlayedIndex;
    }

    public int getLastEpisodePlayedRemainedTime()
    {
        return this.lastEpisodePlayedRemainedTime;
    }

    public int getNrOfEpisodes()
    {
        return this.episodes.size();
    }

    public int getEpisodeNr(Episode episode)
    {
        return this.episodes.indexOf(episode);
    }

    public Episode getEpisode(int number)
    {
        if (number >= this.episodes.size())
            return null;

        return this.episodes.get(number);
    }

    public Episode getEpisodeAfter(Episode episode)
    {
        int i;

        i = (this.episodes.indexOf(episode) + 1) % this.episodes.size();

        return this.episodes.get(i);
    }

    public Episode getEpisodeBefore(Episode episode)
    {
        int i;

        i = (this.episodes.indexOf(episode) - 1) % this.episodes.size();

        if (i == -1)
            i = this.episodes.size() - 1;

        return this.episodes.get(i);
    }

    public boolean isFirst(Episode episode)
    {
        return (this.episodes.indexOf(episode) == 0);
    }

    public boolean isLast(Episode episode)
    {
        return (this.episodes.indexOf(episode) == (this.episodes.size() - 1));
    }

    public boolean isMatchedByFilter(Filter filter)
    {
        if (filter.getName() != null) {
            if (this.name.startsWith(filter.getName()) == false)
                return false;
        }

        if (filter.getOwner() != null) {
            if (this.owner.equals(filter.getOwner()) == false)
                return false;
        }

        return true;
    }
}
