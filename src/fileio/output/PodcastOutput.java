package fileio.output;

import java.util.ArrayList;

public class PodcastOutput {
    private String name;

    private ArrayList<String> episodes;

    public PodcastOutput() {

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
     * @param episodes
     */
    public void setEpisodes(final ArrayList<String> episodes) {
        this.episodes = episodes;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getEpisodes() {
        return this.episodes;
    }
}
