package fileio.output;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import fileio.CommandIO;

public final class CommandOutput extends CommandIO {
    // Standard argument fields

    private String user;
    @Getter @Setter
    private Integer timestamp;
    private String message;
    private ArrayList<String> results;
    private MusicPlayerStatusOutput stats;
    private Object result;


    public CommandOutput() {

    }

    /**
     *
     * @param user
     */
    public void setUser(final String user) {
        this.user = user;
    }

    /**
     *
     * @return
     */
    public String getUser() {
        return this.user;
    }

    /**
     *
     * @param message
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return this.message;
    }

    /**
     *
     * @param results
     */
    public void setResults(final ArrayList<String> results) {
        this.results = results;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getResults() {
        return this.results;
    }

    /**
     *
     * @param stats
     */
    public void setStats(final MusicPlayerStatusOutput stats) {
        this.stats = stats;
    }

    /**
     *
     * @return
     */
    public MusicPlayerStatusOutput getStats() {
        return this.stats;
    }

    /**
     *
     * @param result
     */
    public void setResult(final Object result) {
        this.result = result;
    }

    /**
     *
     * @return
     */
    public Object getResult() {
        return this.result;
    }
}
