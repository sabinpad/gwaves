package gwaves.sample;

public abstract class AudioRec {
    protected String name;
    protected Integer duration;

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return
     */
    public Integer getDuration() {
        return this.duration;
    }
}
