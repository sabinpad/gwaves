package fileio.output;

import fileio.input.CommandInput;

public abstract class CommandOutput {
    // Standard argument fields

    protected String command;
    protected String user;
    protected Integer timestamp;

    public CommandOutput(CommandInput commandInput)
    {
        this.command = commandInput.getCommand();
        this.user = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    public CommandOutput()
    {
        
    }

    public void setCommand(final String command)
    {
        this.command = command;
    }

    public String getCommand()
    {
        return this.command;
    }

    public void setUser(final String user)
    {
        this.user = user;
    }

    public String getUser()
    {
        return this.user;
    }

    public void setTimestamp(final Integer timestamp)
    {
        this.timestamp = timestamp;
    }

    public Integer getTimestamp()
    {
        return this.timestamp;
    }
}
