package gwaves.context;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import fileio.input.SongInput;
import fileio.output.AlbumOutput;

import gwaves.collection.Album;
import gwaves.storage.DataBase;

public class Artist extends User {
    private LinkedHashMap<String, ArtistEvent> events;
    private LinkedHashMap<String, ArtistMerch> merches;

    private LinkedHashMap<String, Album> albums;

    /**
     * Create new Artist object
     *
     * @param username
     * @param age
     * @param city
     */
    public Artist(String username, int age, String city) {
        super(username, age, city);

        this.events = new LinkedHashMap<>();
        this.merches = new LinkedHashMap<>();
        this.albums = new LinkedHashMap<>();
    }

    public void doAddAlbum(final String name, final String owner, final Integer releaseYear, final String description, final ArrayList<SongInput> songsInput) {
        if (this.albums.containsKey(name)) {
            this.commandMessage = this.getUserName() + " has another album with the same name.";
            return;
        }

        // TODO sa verific daca sunt 2 cu acelasi nume
        // this.commandMessage = this.getUserName() + " has the same song at least twice in this album.";

        Album newAlbum = new Album(name, owner, releaseYear, description, songsInput);
        DataBase database = DataBase.getInstance();

        this.albums.put(name, newAlbum);

        for (var song : newAlbum.getSongs())
            database.addSong(song);

        database.addAlbum(newAlbum);

        this.commandMessage = this.getUserName() + " has added new album successfully.";
    }

    public void doRmvAlbum(final String name) {
        if (!this.albums.containsKey(name)) {
            this.commandMessage = this.getUserName() + " doesn't have an album with the given name.";
            return;
        }

        // TODO sa verific daca cineva are incarcat albumul
        // this.commandMessage = this.getUserName() + " can't delete this album.";

        this.albums.remove(name);

        this.commandMessage = this.getUserName() + " deleted the album successfully.";
    }

    public void doAddEvent(String name, String description, String date) {
        if (this.events.containsKey(name)) {
            this.commandMessage = this.getUserName() + " has another event with the same name.";
            return;
        }

        // TODO sa verific daca data e valida
        // this.commandMessage = "Event for " + this.getUserName() + " does not have a valid date.";

        this.events.put(name, new ArtistEvent(name, description, date));

        this.commandMessage = this.getUserName() + " has added new event successfully.";
    }

    public void doRmvEvent(String name) {
        if (!this.events.containsKey(name)) {
            this.commandMessage = this.getUserName() + " doesn't have an event with the given name.";
            return;
        }

        this.events.remove(name);

        this.commandMessage = this.getUserName() + " deleted the event successfully.";
    }

    public void doAddMerch(String name, String description, int price) {
        if (!this.merches.containsKey(name)) {
            this.commandMessage = this.getUserName() + " has merchandise with the same name.";
            return;
        }

        if (price < 0) {
            this.commandMessage = "Price for merchandise can not be negative.";
            return;
        }

        this.merches.put(name, new ArtistMerch(name, description, price));

        this.commandMessage = this.getUserName() + " has added new merchandise successfully.";
    }

    public ArrayList<AlbumOutput> doShowAlbums() {
        AlbumOutput albOutput;
        ArrayList<AlbumOutput> result = new ArrayList<>();

        for (var entry : this.albums.entrySet()) {
            albOutput = new AlbumOutput();

            albOutput.setName(entry.getValue().getName());
            albOutput.setSongs(entry.getValue().getSongsNameList());

            result.add(albOutput);
        }

        this.commandMessage = null;

        return result;
    }

    public int getNrOfLikes() {
        int sum = 0;

        for (var entry : this.albums.entrySet())
            sum += entry.getValue().getNrOfLikes();

        return sum;
    }
}

class ArtistEvent {
    private String name;
    private String description;
    private String date;

    public ArtistEvent(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDate() {
        return this.date;
    }
}

class ArtistMerch {
    private String name;
    private String description;
    private int price;

    public ArtistMerch(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPrice() {
        return this.price;
    }
}
