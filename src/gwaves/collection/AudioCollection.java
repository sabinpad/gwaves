package gwaves.collection;

public abstract class AudioCollection {
    protected String name;
    protected String owner;
    protected int entireDuration;

    public AudioCollection(String name, String owner)
    {
        this.name = name;
        this.owner = owner;
        this.entireDuration = 0;
    }
}
