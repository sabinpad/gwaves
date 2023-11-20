package fileio.output;

import java.util.ArrayList;

import fileio.input.CommandInput;

public class SysCommandOutput extends CommandOutput {
    private ArrayList<String> result;

    public SysCommandOutput(CommandInput commandInput)
    {
        super(commandInput);
    }

    public void setResult(ArrayList<String> result)
    {
        this.result = result;
    }

    public ArrayList<String> getResult()
    {
        return this.result;
    }
}
