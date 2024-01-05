package fileio.output;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import fileio.CommandIO;

@Getter @Setter
public final class CommandOutput extends CommandIO {
    // Standard argument fields

    private String user;

    private Integer timestamp;

    private String message;
    private ArrayList<String> results;
    private MusicPlayerStatusOutput stats;
    private Object result;
    private ArrayList<?> notifications;
}
