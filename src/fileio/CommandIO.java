package fileio;

public abstract class CommandIO {
    // Standard argument fields
    protected String command;
    protected Integer timestamp;

    /**
     * @param command
     */
    public void setCommand(final String command) {
        this.command = command;
    }

    /**
     * @return
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * @param timestamp
     */
    public void setTimestamp(final Integer timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return
     */
    public Integer getTimestamp() {
        return this.timestamp;
    }
}
