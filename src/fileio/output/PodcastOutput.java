package fileio.output;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PodcastOutput {
    private String name;
    private ArrayList<String> episodes;
}
