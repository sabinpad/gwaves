package gwaves.collection;

import java.util.ArrayList;

import javax.xml.crypto.Data;

import gwaves.sample.Song;
import gwaves.storage.DataBase;

public class Album extends AudioCollection {
    private int releaseYear;
    private String description;

    private ArrayList<Song> songs;

    public Album(final String name, final String owner, final int releaseYear, final String description, ArrayList<Song> songs) {
        super(name, owner);

        DataBase database = DataBase.getInstance();

        this.songs = new ArrayList<>();

        for (var song : songs) {
            this.songs.add(song);
            // database.addSong(song);
        }
    }
}
