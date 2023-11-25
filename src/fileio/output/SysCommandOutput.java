package fileio.output;

import java.util.ArrayList;

public final class SysCommandOutput extends CommandOutput {
    private ArrayList<String> result;

    public SysCommandOutput() {

    }

    public void setResult(final ArrayList<String> result) {
        this.result = result;
    }

    public ArrayList<String> getResult() {
        return this.result;
    }
}
