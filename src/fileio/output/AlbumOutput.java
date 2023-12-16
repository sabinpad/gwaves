package fileio.output;

import java.util.ArrayList;

public class AlbumOutput {
    private String name;

    private ArrayList<String> songs;

    /**
     *
     */
    public AlbumOutput() {

    }

    /**
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param songs
     */
    public void setSongs(final ArrayList<String> songs) {
        this.songs = songs;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getSongs() {
        return this.songs;
    }
}
