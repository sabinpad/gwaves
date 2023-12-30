package gwaves.sample;

import lombok.Getter;

@Getter
public abstract class AudioRec {
    private String name;
    private Integer duration;

    public AudioRec(final String name, final Integer duration) {
        this.name = name;
        this.duration = duration;
    } 
}
