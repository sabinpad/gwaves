package fileio.output;

public final class MusicPlayerStatusOutput {
    private String name;
    private Integer remainedTime;
    private String repeat;
    private Boolean shuffle;
    private Boolean paused;

    public MusicPlayerStatusOutput() {

    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setRemainedTime(final Integer remainedTime) {
        this.remainedTime = remainedTime;
    }

    public Integer getRemainedTime() {
        return this.remainedTime;
    }

    public void setRepeat(final String repeat) {
        this.repeat = repeat;
    }

    public String getRepeat() {
        return this.repeat;
    }

    public void setShuffle(final Boolean shuffle) {
        this.shuffle = shuffle;
    }

    public Boolean getShuffle() {
        return this.shuffle;
    }

    public void setPaused(final Boolean paused) {
        this.paused = paused;
    }

    public Boolean getPaused() {
        return this.paused;
    }
}
