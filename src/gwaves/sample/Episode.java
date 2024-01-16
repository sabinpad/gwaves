package gwaves.sample;

import lombok.Getter;

import fileio.input.EpisodeInput;

@Getter
public final class Episode extends AudioRec {
    private String description;
    private int listenings;

    public Episode(final EpisodeInput episodeInput) {
        super(episodeInput.getName(), episodeInput.getDuration());
        this.description = episodeInput.getDescription();
    }

    /**
     * Adds listening to the song
     */
    public void addListen() {
        this.listenings++;
    }
}
