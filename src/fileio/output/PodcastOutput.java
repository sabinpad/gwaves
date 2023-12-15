package fileio.output;

import java.util.ArrayList;

public class PodcastOutput {
    private String name;

    private ArrayList<String> episodes;

    public PodcastOutput() {
        
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEpisodes(ArrayList<String> episodes) {
        this.episodes = episodes;
    }

    public ArrayList<String> getEpisodes() {
        return this.episodes;
    }
}
