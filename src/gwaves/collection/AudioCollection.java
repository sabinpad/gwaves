package gwaves.collection;

public abstract class AudioCollection {
    protected String name;
    protected String owner;
    protected int entireDuration;

    /**
     * @param name
     * @param owner
     */
    public AudioCollection(final String name, final String owner) {
        this.name = name;
        this.owner = owner;
        this.entireDuration = 0;
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return
     */
    public String getOwner() {
        return this.owner;
    }
}
