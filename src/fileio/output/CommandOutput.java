package fileio.output;

import fileio.CommandIO;

public abstract class CommandOutput  extends CommandIO {
    // Standard argument fields
    protected String user;

    public CommandOutput() {

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
}
