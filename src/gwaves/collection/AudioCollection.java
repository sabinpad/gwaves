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

    public void addAudRec(T audRec) {
        this.audRecs.add(audRec);
    }

    public void removeAudRec(T audRec) {
        if (this.audRecs.contains(audRec)) {
            this.audRecs.remove(audRec);
        }
    }

    public T getAudRec(final int index) {
        if (index < 0 || index >= this.audRecs.size()) {
            return null;
        }

        return this.audRecs.get(index);
    }

    public int getNrOfAudRecs() {
        return this.audRecs.size();
    }

    public ArrayList<String> getAudRecsName() {
        ArrayList<String> nameList = new ArrayList<>();

        for (var rec : this.audRecs) {
            nameList.add(rec.getName());
        }

        return nameList;
    }

    public boolean hasAudRec(AudioRec audRec) {
        return this.audRecs.contains(audRec);
    }
}
