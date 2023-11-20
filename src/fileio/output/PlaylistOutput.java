package fileio.output;

import java.util.ArrayList;

public class PlaylistOutput {
    private String name;
    private ArrayList<String> songs;
    private String visibility;
    private Integer followers;

    public PlaylistOutput()
    {

    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setSongs(final ArrayList<String> songs)
    {
        this.songs = songs;
    }

    public ArrayList<String> getSongs()
    {
        return this.songs;
    }

    public void setVisibility(final String visibility)
    {
        this.visibility = visibility;
    }

    public String getVisibility()
    {
        return this.visibility;
    }

    public void setFollowers(final Integer followers)
    {
        this.followers = followers;
    }

    public Integer getFollowers()
    {
        return this.followers;
    }
}
