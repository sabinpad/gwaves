package fileio.input;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class PodcastInput {
    private String name;
    private String owner;
    private ArrayList<EpisodeInput> episodes;
}
