package fileio.output;

import java.util.ArrayList;

public class UserCommandOutput extends CommandOutput {
    private ArrayList<String> results;
    private MusicPlayerStatusOutput stats;
    private ArrayList<PlaylistOutput> result;

    public UserCommandOutput()
    {

    }

    public void setResults(final ArrayList<String> results)
    {
        this.results = results;
    }

    public ArrayList<String> getResults()
    {
        return this.results;
    }

    public void setStats(final MusicPlayerStatusOutput stats)
    {
        this.stats = stats;
    }

    public MusicPlayerStatusOutput getStats()
    {
        return this.stats;
    }

    public void setResult(final ArrayList<PlaylistOutput> result)
    {
        this.result = result;
    }

    public ArrayList<PlaylistOutput> getResult()
    {
        return this.result;
    }
}
