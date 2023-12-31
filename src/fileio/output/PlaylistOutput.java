package fileio.output;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class PlaylistOutput {
    private String name;
    private ArrayList<String> songs;
    private String visibility;
    private Integer followers;
}
