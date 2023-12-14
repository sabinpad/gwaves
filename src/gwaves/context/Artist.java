package gwaves.context;

import java.util.ArrayList;

import gwaves.collection.Album;

public class Artist extends User {
    private ArrayList<Album> albums;

    public Artist(String username, int age, String city) {
        super(username, age, city);
    }
}
