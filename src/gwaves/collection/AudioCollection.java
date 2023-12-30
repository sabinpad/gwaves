package gwaves.collection;

import java.util.ArrayList;

import lombok.Getter;

import gwaves.sample.AudioRec;

@Getter
public abstract class AudioCollection<T extends AudioRec> {
    private String name;
    private String owner;
    private int entireDuration;
    private ArrayList<T> collection;

    public AudioCollection(final String name, final String owner) {
        this.name = name;
        this.owner = owner;
        this.entireDuration = 0;
        this.collection = new ArrayList<>();
    }

    public void addAudRec(T audRec) {
        this.collection.add(audRec);
    }

    public void removeAudRec(T audRec) {
        if (this.collection.contains(audRec)) {
            this.collection.remove(audRec);
        }
    }

    public T getAudRec(final int index) {
        if (index < 0 || index >= this.collection.size()) {
            return null;
        }

        return this.collection.get(index);
    }

    public int getNrOfAudRecs() {
        return this.collection.size();
    }

    public ArrayList<String> getAudRecsName() {
        ArrayList<String> nameList = new ArrayList<>();

        for (var rec : this.collection) {
            nameList.add(rec.getName());
        }

        return nameList;
    }

    public boolean hasAudRec(T audRec) {
        return this.collection.contains(audRec);
    }
}
