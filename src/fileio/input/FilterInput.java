package fileio.input;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class FilterInput {
    private String owner;
    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear;
    private String artist;
    private String description;
}
