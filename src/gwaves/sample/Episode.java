package gwaves.sample;

import lombok.Getter;

import fileio.input.EpisodeInput;

@Getter
public final class Episode extends AudioRec {
    private String description;

    public Episode(final EpisodeInput episodeInput) {
        super(episodeInput.getName(), episodeInput.getDuration());
        this.description = episodeInput.getDescription();
    }

    // public String getDescription() {
    //     return this.description;
    // }
}
