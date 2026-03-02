package dev.sabin.gwaves.model.collection;

import java.util.ArrayList;

import dev.sabin.gwaves.model.io.input.EpisodeInput;
import dev.sabin.gwaves.model.io.input.PodcastInput;
import dev.sabin.gwaves.model.io.input.FilterInput;

import dev.sabin.gwaves.model.sample.Episode;
import dev.sabin.gwaves.service.util.Filterable;

public final class Podcast extends AudioCollection<Episode> implements Filterable {
    private boolean ghost;

    /**
     * Create new Podcast object
     *
     * @param name          of Podcast
     * @param owner         name of Podcast owner
     * @param episodesInput list of episodes
     */
    public Podcast(final String name, final String owner,
                   final ArrayList<EpisodeInput> episodesInput, final boolean ghosted) {
        super(name, owner);

        for (var episodeInput : episodesInput) {
            this.getAudRecs().add(new Episode(episodeInput));
        }

        this.ghost = ghosted;
    }

    /**
     * Create Podcast object based on PodcastInput object
     *
     * @param podcastInput object to create the instance from
     */
    public Podcast(final PodcastInput podcastInput, final boolean ghosted) {
        super(podcastInput.getName(), podcastInput.getOwner());

        for (var episodeInput : podcastInput.getEpisodes()) {
            this.getAudRecs().add(new Episode(episodeInput));
        }

        this.ghost = ghosted;
    }

    public boolean isGhosted() {
        return this.ghost;
    }

    /**
     * @param filter used to match
     * @return true if the song is matched by the filter
     */
    public boolean isMatchedByFilter(final FilterInput filter) {
        if (filter.getName() != null) {
            if (!this.getName().startsWith(filter.getName())) {
                return false;
            }
        }

        if (filter.getOwner() != null) {
            if (!this.getOwner().equals(filter.getOwner())) {
                return false;
            }
        }

        return true;
    }
}
