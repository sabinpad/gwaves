package fileio.output;

import java.util.ArrayList;

import fileio.CommandIO;

public class CommandOutput extends CommandIO {
    // Standard argument fields
    
    private String user;
    private String message;
    private ArrayList<String> results;
    private MusicPlayerStatusOutput stats;
    private Object result;


    public CommandOutput() {

    }

    public void setUser(final String user) {
        this.user = user;
    }

    public String getUser() {
        return this.user;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setResults(final ArrayList<String> results) {
        this.results = results;
    }

    public ArrayList<String> getResults() {
        return this.results;
    }

    public void setStats(final MusicPlayerStatusOutput stats) {
        this.stats = stats;
    }

    public MusicPlayerStatusOutput getStats() {
        return this.stats;
    }

    public void setResult(final Object result) {
        this.result = result;
    }

    public Object getResult() {
        return this.result;
    }
}
