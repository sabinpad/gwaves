package gwaves.context;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import fileio.input.FilterInput;
import fileio.input.SongInput;
import fileio.output.AlbumOutput;

import gwaves.collection.Album;
import gwaves.storage.DataBase;
import gwaves.tools.UserManager;
import gwaves.misc.ArtistEvent;
import gwaves.misc.ArtistMerch;
import gwaves.util.Filterable;
import gwaves.ui.Page;
import gwaves.ui.ArtistPage;

public final class Artist extends User implements Filterable {
    private LinkedHashMap<String, ArtistEvent> events;
    private LinkedHashMap<String, ArtistMerch> merches;

    private LinkedHashMap<String, Album> albums;

    private ArtistPage page;

    private LinkedHashMap<NormalUser, Integer> listeners;

    private ArrayList<NormalUser> subscribers;

    /**
     * Create new Artist object
     *
     * @param username
     * @param age
     * @param city
     */
    public Artist(final String username, final int age, final String city) {
        super(username, age, city);

        this.events = new LinkedHashMap<>();
        this.merches = new LinkedHashMap<>();
        this.albums = new LinkedHashMap<>();
        this.page = new ArtistPage(this, events, merches, albums);
        this.listeners = new LinkedHashMap<>();
        this.subscribers = new ArrayList<>();
    }

    /**
     *
     * @param name
     * @param owner
     * @param releaseYear
     * @param description
     * @param songsInput
     */
    public void doAddAlbum(final String name, final String owner, final Integer releaseYear, final String description,
            final ArrayList<SongInput> songsInput) {
        if (this.albums.containsKey(name)) {
            this.commandMessage = this.getUsername() + " has another album with the same name.";
            return;
        }

        for (var sg1 : songsInput) {
            for (var sg2 : songsInput) {
                if (sg1.getName().equals(sg2.getName()) && (sg1 != sg2)) {
                    this.commandMessage = this.getUsername()
                                          + " has the same song at least twice in this album.";
                    return;
                }
            }
        }

        Album newAlbum = new Album(name, owner, releaseYear, description, songsInput);
        DataBase database = DataBase.getInstance();

        this.albums.put(name, newAlbum);

        for (var song : newAlbum.getAudRecs()) {
            database.addSong(song);
        }

        database.addAlbum(newAlbum);

        this.notifySubs("New Album", "New Album from " + this.getUsername());

        this.commandMessage = this.getUsername() + " has added new album successfully.";
    }

    /**
     *
     * @param name
     */
    public void doRmvAlbum(final String name) {
        if (!this.albums.containsKey(name)) {
            this.commandMessage = this.getUsername()
                                  + " doesn't have an album with the given name.";
            return;
        }

        if (!UserManager.getInstance().isSafeToRemove(this.albums.get(name))) {
            this.commandMessage = this.getUsername() + " can't delete this album.";
            return;
        }

        UserManager.getInstance().toUnlink(this.albums.get(name));

        DataBase database = DataBase.getInstance();

        for (var song : this.albums.get(name).getAudRecs()) {
            database.removeSong(song);
        }

        database.removeAlbum(this.albums.get(name));

        this.albums.remove(name);

        this.commandMessage = this.getUsername() + " deleted the album successfully.";
    }

    /**
     *
     * @param name
     * @param description
     * @param date
     */
    public void doAddEvent(final String name, final String description, final String date) {
        if (this.events.containsKey(name)) {
            this.commandMessage = this.getUsername() + " has another event with the same name.";
            return;
        }

        // sanity check to see if date is valid
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            this.commandMessage = "Event for " + this.getUsername()
                                  + " does not have a valid date.";
            return;
        }

        this.events.put(name, new ArtistEvent(name, description, date));

        this.notifySubs("New Event", "New Event from " + this.getUsername());

        this.commandMessage = this.getUsername() + " has added new event successfully.";
    }

    /**
     *
     * @param name
     */
    public void doRmvEvent(final String name) {
        if (!this.events.containsKey(name)) {
            this.commandMessage = this.getUsername()
                                  + " doesn't have an event with the given name.";
            return;
        }

        this.events.remove(name);

        this.commandMessage = this.getUsername() + " deleted the event successfully.";
    }

    /**
     *
     * @param name
     * @param description
     * @param price
     */
    public void doAddMerch(final String name, final String description, final int price) {
        if (this.merches.containsKey(name)) {
            this.commandMessage = this.getUsername() + " has merchandise with the same name.";
            return;
        }

        if (price < 0) {
            this.commandMessage = "Price for merchandise can not be negative.";
            return;
        }

        this.merches.put(name, new ArtistMerch(name, description, price));

        this.notifySubs("New Merch", "New Merch from " + this.getUsername());

        this.commandMessage = this.getUsername() + " has added new merchandise successfully.";
    }

    /**
     *
     * @return
     */
    public ArrayList<AlbumOutput> doShowAlbums() {
        AlbumOutput albOutput;
        ArrayList<AlbumOutput> result = new ArrayList<>();

        for (var entry : this.albums.entrySet()) {
            albOutput = new AlbumOutput();

            albOutput.setName(entry.getValue().getName());
            albOutput.setSongs(entry.getValue().getAudRecsName());

            result.add(albOutput);
        }

        this.commandMessage = null;

        return result;
    }

    /**
     *
     * @return
     */
    public String doGetPage() {
        return this.page.strigify();
    }

    public void addListen(NormalUser normalUser) {
        if (this.listeners.containsKey(normalUser)) {
            Integer val = this.listeners.get(normalUser);
            val++;
        } else {
            this.listeners.put(normalUser, 1);
        }
    }

    private void notifySubs(String name, String description) {
        for (var sub : this.subscribers) {
            sub.addNotification(name, description);
        }
    }

    /**
     *
     */
    public void clearAll() {
        DataBase database = DataBase.getInstance();

        for (var entry : this.albums.entrySet()) {
            for (var song : entry.getValue().getAudRecs()) {
                database.removeSong(song);
            }

            database.removeAlbum(entry.getValue());
        }

        this.albums.clear();
    }

    /**
     *
     * @return
     */
    public int getNrOfLikes() {
        int sum = 0;

        for (var entry : this.albums.entrySet()) {
            sum += entry.getValue().getNrOfLikes();
        }

        return sum;
    }

    /**
     *
     * @return
     */
    public ArrayList<Album> getAlbums() {
        return new ArrayList<>(this.albums.values());
    }

    /**
     *
     * @return
     */
    public Page getPage() {
        return this.page;
    }

    /**
     *
     */
    public boolean isMatchedByFilter(final FilterInput filter) {
        if (filter.getName() != null) {
            if (!this.getUsername().startsWith(filter.getName())) {
                return false;
            }
        }

        return true;
    }
}
