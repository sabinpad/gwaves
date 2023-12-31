package fileio.input;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class LibraryInput {
    private ArrayList<SongInput> songs;
    private ArrayList<PodcastInput> podcasts;
    private ArrayList<UserInput> users;
}
