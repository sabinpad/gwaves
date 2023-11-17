package gwaves.sample;

import java.util.ArrayList;

import fileio.input.SongInput;

public class Song extends AudioRec {
    String album;
    ArrayList<String> tags;
    String lyrics;
    String genre;
    int releaseYear;
    String artist;

    public Song() {}

    public Song(SongInput input)
    {
        this.name = input.getName();
        this.duration = input.getDuration();
    }
}
