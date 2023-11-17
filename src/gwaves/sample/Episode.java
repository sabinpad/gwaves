package gwaves.sample;

import fileio.input.EpisodeInput;

public class Episode extends AudioRec {
    String description;

    public Episode() {}

    public Episode(EpisodeInput input)
    {
        this.name = input.getName();
        this.duration = input.getDuration();
        this.description = input.getDescription();
    }
}
