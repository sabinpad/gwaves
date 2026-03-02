package dev.sabin.gwaves.model.io.input;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class SongInput {
    private String name;
    private Integer duration;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private Integer releaseYear;
    private String artist;
}
