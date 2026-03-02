package dev.sabin.gwaves.model.io.input;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import dev.sabin.gwaves.model.io.CommandIO;

@Getter @Setter
public final class CommandInput extends CommandIO {
    // Argument fields based on command

    private String username;

    private Integer timestamp;

    private String type;
    private FilterInput filters;
    private Integer itemNumber;
    private Integer seed;
    private String playlistName;
    private Integer playlistId;
    private String nextPage;
    private Integer age;
    private String city;
    private String name;
    private Integer releaseYear;
    private String description;
    private ArrayList<SongInput> songs;
    private ArrayList<EpisodeInput> episodes;
    private String date;
    private Integer price;

    private String recommendationType;
}
