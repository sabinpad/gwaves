package fileio.output;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class MusicPlayerStatusOutput {
    private String name;
    private Integer remainedTime;
    private String repeat;
    private Boolean shuffle;
    private Boolean paused;
}
