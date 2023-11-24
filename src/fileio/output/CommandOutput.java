package fileio.output;

import fileio.input.CommandInput;

public abstract class CommandOutput {
    // Standard argument fields

    protected String command;
    protected String user;
    protected Integer timestamp;

    /**
     * @param commandInput
     */
    public CommandOutput(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.user = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    public CommandOutput() {

    }

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
     * @param user
     */
    public void setUser(final String user) {
        this.user = user;
    }

    /**
     * @return
     */
    public String getUser() {
        return this.user;
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
