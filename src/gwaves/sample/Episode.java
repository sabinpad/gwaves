package gwaves.sample;

import fileio.input.EpisodeInput;

public final class Episode extends AudioRec {
    private String description;

    public Episode(final EpisodeInput episodeInput) {
        this.name = episodeInput.getName();
        this.duration = episodeInput.getDuration();
        this.description = episodeInput.getDescription();
    }

    public String getDescription() {
        return this.description;
    }
}
