package gwaves.context;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import fileio.input.FilterInput;
import fileio.input.SongInput;
import fileio.output.AlbumOutput;
import fileio.output.WrappedOutput;

import gwaves.sample.Song;
import gwaves.collection.Album;
import gwaves.storage.DataBase;
import gwaves.tools.UserManager;
import gwaves.misc.ArtistEvent;
import gwaves.misc.ArtistMerch;
import gwaves.util.Filterable;
import lombok.Getter;
import gwaves.ui.Page;
import gwaves.ui.ArtistPage;

public final class Artist extends User implements Filterable {
    private LinkedHashMap<String, ArtistEvent> events;
    private LinkedHashMap<String, ArtistMerch> merches;

    private LinkedHashMap<String, Album> albums;

    private ArtistPage page;

    private LinkedHashMap<NormalUser, Integer> listeners;
    @Getter
    private double songRevenue;
    @Getter
    private int merchRevenue;

    private HashMap<String, Double> songsRevenue;

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
        this.songsRevenue = new HashMap<>();
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

        database.addAlbum(newAlbum);

        for (var song : newAlbum.getAudRecs()) {
            database.addSong(song);
        }

        this.notifySubs("New Album", "New Album from " + this.getUsername() + ".");

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

        this.notifySubs("New Event", "New Event from " + this.getUsername() + ".");

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

        this.notifySubs("New Merchandise", "New Merchandise from " + this.getUsername() + ".");

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

    public WrappedOutput doWrapped() {
        WrappedOutput wrappedOutput = new WrappedOutput();
        HashMap<String, Integer> streamedSongs = new HashMap<>();
        HashMap<String, Integer> streamedAlbums = new HashMap<>();
        Integer val;
        ObjectNode objNode;

        if (this.listeners.isEmpty()) {
            this.commandMessage = "No data to show for artist " + this.getUsername() + ".";
            return null;
        }

        for (var entry : this.albums.entrySet()) {
            Album album = entry.getValue();

            for (var song : album.getAudRecs()) {
                if (song.getListenings() > 0) {
                    if (streamedSongs.containsKey(song.getName())) {
                        val = streamedSongs.get(song.getName());
                    } else {
                        val = 0;
                    }

                    streamedSongs.put(song.getName(), val + song.getListenings());
                }
            }

            if (album.getListenings() > 0) {
                if (streamedAlbums.containsKey(album.getName())) {
                    val = streamedAlbums.get(album.getName());
                } else {
                    val = 0;
                }

                streamedAlbums.put(album.getName(), val + album.getListenings());
            }
        }

        objNode = NormalUser.objMapper.createObjectNode();
        List<String> topSongsName = streamedSongs.keySet().stream()
                                            .sorted(new Comparator<String>() {
                                                public int compare(String songName1, String songName2) {
                                                    if (streamedSongs.get(songName2) == streamedSongs.get(songName1)) {
                                                        return songName1.compareTo(songName2);
                                                    }

                                                    return streamedSongs.get(songName2) - streamedSongs.get(songName1);
                                                }
                                            })
                                            .limit(5)
                                            .collect(Collectors.toList());

        for (var songName : topSongsName) {
            objNode.put(songName, streamedSongs.get(songName));
        }
        wrappedOutput.setTopSongs(objNode);

        objNode = NormalUser.objMapper.createObjectNode();
        List<String> topAlbumsName = streamedAlbums.keySet().stream()
                                            .sorted(new Comparator<String>() {
                                                public int compare(String albumName1, String albumName2) {
                                                    if (streamedAlbums.get(albumName2) == streamedAlbums.get(albumName1)) {
                                                        return albumName1.compareTo(albumName2);
                                                    }

                                                    return streamedAlbums.get(albumName2) - streamedAlbums.get(albumName1);
                                                }
                                            })
                                            .limit(5)
                                            .collect(Collectors.toList());

        for (var albumName : topAlbumsName) {
            objNode.put(albumName, streamedAlbums.get(albumName));
        }
        wrappedOutput.setTopAlbums(objNode);

        ArrayList<String> fansName = new ArrayList<>();
        List<NormalUser> topFans = this.listeners.keySet().stream()
                                                        .sorted(new Comparator<NormalUser>() {
                                                            public int compare(NormalUser normalUser1, NormalUser normalUser2) {
                                                                if (listeners.get(normalUser2) == listeners.get(normalUser1)) {
                                                                    return normalUser1.getUsername().compareTo(normalUser2.getUsername());
                                                                }

                                                                return listeners.get(normalUser2) - listeners.get(normalUser1);
                                                            }
                                                        })
                                                        .limit(5)
                                                        .collect(Collectors.toList());

        for (var fan : topFans) {
            fansName.add(fan.getUsername());
        }
        wrappedOutput.setTopFans(fansName);

        wrappedOutput.setListeners(this.listeners.size());

        return wrappedOutput;
    }

    public void addListen(NormalUser normalUser) {
        Integer val;

        if (this.listeners.containsKey(normalUser)) {
            val = this.listeners.get(normalUser);
        } else {
            val = 0;
        }

        this.listeners.put(normalUser, val + 1);
    }

    public void pay(double amount) {
        this.songRevenue += amount;
    }

    public void paySong(double amount, Song song) {
        Double val;

        if (this.songsRevenue.containsKey(song.getName())) {
            val = this.songsRevenue.get(song.getName());
        } else {
            val = 0.0;
        }

        this.songsRevenue.put(song.getName(), val + amount);
    }

    public ArtistMerch buyMerch(String merchName) {
        if (!this.merches.containsKey(merchName)) {
            return null;
        }

        this.merchRevenue += this.merches.get(merchName).getPrice();

        return this.merches.get(merchName);
    }

    public void addSubscriber(NormalUser normalUser) {
        this.subscribers.add(normalUser);
    }

    public void removeSubscriber(NormalUser normalUser) {
        this.subscribers.remove(normalUser);
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
    public ArrayList<Album> getAlbums() {
        return new ArrayList<>(this.albums.values());
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

    public double getTotalRevenue() {
        return this.songRevenue + this.merchRevenue;
    }

    public String getMostProfitableSongName() {
        if (this.songRevenue == 0) {
            return "N/A";
        }

        List<String> profitableSongsName = this.songsRevenue.keySet().stream()
                                                               .sorted(new Comparator<String>() {
                                                                    public int compare(String songName1, String songName2) {
                                                                        if (songsRevenue.get(songName2).equals(songsRevenue.get(songName1))) {
                                                                            return songName1.compareTo(songName2);
                                                                        }
                                                                        
                                                                        if (songsRevenue.get(songName1) < songsRevenue.get(songName2)) {
                                                                            return 1;
                                                                        } else if (songsRevenue.get(songName1) > songsRevenue.get(songName2)) {
                                                                            return -1;
                                                                        }
                                                                        return 0;
                                                                    }
                                                                })
                                                               .collect(Collectors.toList());

        return profitableSongsName.get(0);
    }

    public List<NormalUser> gettop5Fans() {
        return this.listeners.keySet().stream()
                                      .sorted(Comparator.comparing(normalUser -> listeners.get(normalUser)))
                                      .limit(5)
                                      .collect(Collectors.toList());
    }

    /**
     *
     * @return
     */
    public Page getPage() {
        return this.page;
    }

    public boolean hasBeenStreamed() {
        return !this.listeners.isEmpty();
    }

    public boolean hasRevenue() {
        return this.songRevenue > 0 || this.merchRevenue > 0;
    }

    public boolean hasSubscriber(NormalUser normalUser) {
        return this.subscribers.contains(normalUser);
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
