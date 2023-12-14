package gwaves.context;

import java.util.ArrayList;

import gwaves.collection.Podcast;

public class Host extends User {
    private ArrayList<Podcast> podcasts;

    public Host(String username, int age, String city) {
        super(username, age, city);

        this.podcasts = new ArrayList<>();
    }
}
