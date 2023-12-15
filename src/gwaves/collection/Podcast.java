package gwaves.collection;

import java.util.ArrayList;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.FilterInput;

import gwaves.sample.Episode;
import gwaves.util.Filterable;

public final class Podcast extends AudioCollection implements Filterable {
    private ArrayList<Episode> episodes;

    /**
     * Create new Podcast object
     *
     * @param name of Podcast
     * @param owner name of Podcast owner
     * @param episodesInput list of episodes
     */
    public Podcast(final String name, final String owner, final ArrayList<EpisodeInput> episodesInput) {
        super(name, owner);

        this.episodes = new ArrayList<>();

        for (var episodeInput : episodesInput)
            this.episodes.add(new Episode(episodeInput));
    }

    /**
     * Create Podcast object based on PodcastInput object
     *
     * @param podcastInput object to create the instance from
     */
    public Podcast(final PodcastInput podcastInput) {
        super(podcastInput.getName(), podcastInput.getOwner());

        this.episodes = new ArrayList<>();

        for (var episodeInput : podcastInput.getEpisodes()) {
            this.episodes.add(new Episode(episodeInput));
            this.entireDuration += episodeInput.getDuration();
        }
    }

    /**
     * @return nr of episodes the podcast has
     */
    public int getNrOfEpisodes() {
        return this.episodes.size();
    }

    /**
     * @param number
     * @return episode that coresponds with the number
     */
    public Episode getEpisode(final int number) {
        if (number >= this.episodes.size()) {
            return null;
        }

        return this.episodes.get(number);
    }

    public ArrayList<String> getEpisodesNameList() {
        ArrayList<String> nameList = new ArrayList<>();

        for (var episode : this.episodes) {
            nameList.add(episode.getName());
        }

        return nameList;
    }

    /**
     * @param filter used to match
     * @return true if the song is matched by the filter
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
