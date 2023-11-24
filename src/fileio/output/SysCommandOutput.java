package fileio.output;

import java.util.ArrayList;

import fileio.input.CommandInput;

public final class SysCommandOutput extends CommandOutput {
    private ArrayList<String> result;

    public SysCommandOutput(final CommandInput commandInput) {
        super(commandInput);
    }

    public SysCommandOutput() {

    }

    public void setResult(final ArrayList<String> result) {
        this.result = result;
    }

    public ArrayList<String> getResult() {
        return this.result;
    }
}
