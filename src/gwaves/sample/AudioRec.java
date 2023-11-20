package gwaves.sample;

public abstract class AudioRec {
    protected String name;
    protected Integer duration;

    public String getName()
    {
        return this.name;
    }

    public Integer getDuration()
    {
        return this.duration;
    }
}
