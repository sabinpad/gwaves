package gwaves.collection;

import java.util.ArrayList;

import lombok.Getter;

import gwaves.sample.AudioRec;

@Getter
public abstract class AudioCollection<T extends AudioRec> {
    private String name;
    private String owner;
    private int entireDuration;
    private ArrayList<T> audRecs;

    public AudioCollection(final String name, final String owner) {
        this.name = name;
        this.owner = owner;
        this.entireDuration = 0;
        this.audRecs = new ArrayList<>();
    }

    /**
     * Adds given recording to the playlist
     * @param audRec recording
     */
    public void addAudRec(final T audRec) {
        this.audRecs.add(audRec);
    }

    /**
     * Removes given recording from the playlist
     * @param audRec recording
     */
    public void removeAudRec(final T audRec) {
        if (this.audRecs.contains(audRec)) {
            this.audRecs.remove(audRec);
        }
    }

    /**
     * Returns recording at given index
     * @param index
     * @return recording ar given index
     */
    public T getAudRec(final int index) {
        if (index < 0 || index >= this.audRecs.size()) {
            return null;
        }

        return this.audRecs.get(index);
    }

    /**
     * @return number of stored recordings
     */
    public int getNrOfAudRecs() {
        return this.audRecs.size();
    }

    /**
     * @return names list of stored recordings
     */
    public ArrayList<String> getAudRecsName() {
        ArrayList<String> nameList = new ArrayList<>();

        for (var rec : this.audRecs) {
            nameList.add(rec.getName());
        }

        return nameList;
    }

    /**
     * checks if given recording is stored in playlist
     * @param audRec
     * @return true if playlist has the recording false otherwise
     */
    public boolean hasAudRec(final AudioRec audRec) {
        return this.audRecs.contains(audRec);
    }
}
