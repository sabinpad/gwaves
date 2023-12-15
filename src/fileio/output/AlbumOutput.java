package fileio.output;

import java.util.ArrayList;

public class AlbumOutput {
    private String name;

    private ArrayList<String> songs;

    public AlbumOutput() {
        
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setSongs(ArrayList<String> songs) {
        this.songs = songs;
    }

    public ArrayList<String> getSongs() {
        return this.songs;
    }
}
