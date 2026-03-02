package dev.sabin.gwaves.repository;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.core.io.ClassPathResource;

import dev.sabin.gwaves.model.io.input.FilterInput;
import dev.sabin.gwaves.model.io.input.LibraryInput;

import dev.sabin.gwaves.model.context.User;
import dev.sabin.gwaves.model.misc.TopAlbumsStats;
import dev.sabin.gwaves.model.misc.TopArtistsStats;
import dev.sabin.gwaves.model.misc.TopPlaylistsStat;
import dev.sabin.gwaves.model.misc.TopSongsStats;
import dev.sabin.gwaves.model.context.NormalUser;
import dev.sabin.gwaves.model.context.Artist;
import dev.sabin.gwaves.model.context.Host;

import dev.sabin.gwaves.model.sample.Song;
import dev.sabin.gwaves.service.util.Statistics;
import dev.sabin.gwaves.model.collection.Playlist;
import dev.sabin.gwaves.model.collection.Album;
import dev.sabin.gwaves.model.collection.Podcast;

public final class DataBase {
    private static DataBase instance = new DataBase();
    private static Random randGen = new Random();

    private LinkedHashMap<String, NormalUser> normalusers;
    private LinkedHashMap<String, Artist> artists;
    private LinkedHashMap<String, Host> hosts;

    private Song add;
    private ArrayList<Song> library;
    private ArrayList<Playlist> playlists;
    private ArrayList<Album> albums;
    private ArrayList<Podcast> podcasts;

    private HashMap<String, Integer> listeningsRecord;

    private static final int COUNT = 5;

    static {
        try {
            instance.loadDataBase((new ObjectMapper()).readValue(
                new ClassPathResource("data/library.json").getInputStream(),
                LibraryInput.class
            ));
        } catch (Exception e) {}
    }

    private DataBase() {
        this.normalusers = new LinkedHashMap<>();
        this.artists = new LinkedHashMap<>();
        this.hosts = new LinkedHashMap<>();

        this.library = new ArrayList<>();
        this.playlists = new ArrayList<>();
        this.albums = new ArrayList<Album>();
        this.podcasts = new ArrayList<>();

        this.listeningsRecord = new HashMap<>();
    }

    /**
     * @return unique database instance
     */
    public static DataBase getInstance() {
        return DataBase.instance;
    }

    /**
     * @param libInput class to load from
     */
    public void loadDataBase(final LibraryInput libInput) {
        for (var userInput : libInput.getUsers()) {
            this.normalusers.put(userInput.getUsername(), new NormalUser(userInput));
        }

        for (var songInput : libInput.getSongs()) {
            this.add = new Song(songInput);
        }

        for (var podcastInput : libInput.getPodcasts()) {
            this.podcasts.add(new Podcast(podcastInput, true));
        }
    }

    /**
     * @param playlist to add
     */
    public void addNormalUser(final NormalUser user) {
        this.normalusers.put(user.getUsername(), user);
    }

    /**
     * @param playlist to remove
     */
    public void removeNormalUser(final NormalUser user) {
        normalusers.remove(user.getUsername());
    }

    /**
     * @param playlist to add
     */
    public void addArtist(final Artist artist) {
        this.artists.put(artist.getUsername(), artist);
    }

    /**
     * @param playlist to remove
     */
    public void removeArtist(final Artist artist) {
        artists.remove(artist.getUsername());
    }

    /**
     * @param playlist to add
     */
    public void addHost(final Host host) {
        this.hosts.put(host.getUsername(), host);

        for (var podcast : this.podcasts) {
            if (podcast.getOwner().equals(host.getUsername())) {
                host.addGhostedPodcast(podcast);
            }
        }
    }

    /**
     * @param playlist to remove
     */
    public void removeHost(final Host host) {
        hosts.remove(host.getUsername());
    }

    /**
     * @param playlist to add
     */
    public void addSong(final Song song) {
        this.library.add(song);

        if (this.listeningsRecord.containsKey(song.getName())) {
            song.setListenings(this.listeningsRecord.get(song.getName()));
            this.listeningsRecord.remove(song.getName());
        }
    }

    /**
     * @param playlist to remove
     */
    public void removeSong(final Song song) {
        this.library.remove(song);

        Integer val;

        if (this.listeningsRecord.containsKey(song.getName())) {
            val = this.listeningsRecord.get(song.getName());
        } else {
            val = 0;
        }

        this.listeningsRecord.put(song.getName(), val + song.getListenings());
    }

    /**
     * @param playlist to add
     */
    public void addPlaylist(final Playlist playlist) {
        this.playlists.add(playlist);
    }

    /**
     * @param playlist to remove
     */
    public void removePlaylist(final Playlist playlist) {
        this.playlists.remove(playlist);
    }

    /**
     * @param playlist to add
     */
    public void addAlbum(final Album album) {
        this.albums.add(album);

        if (this.listeningsRecord.containsKey(album.getName())) {
            album.setListenings(this.listeningsRecord.get(album.getName()));
            this.listeningsRecord.remove(album.getName());
        }
    }

    /**
     * @param playlist to remove
     */
    public void removeAlbum(final Album album) {
        this.albums.remove(album);

        Integer val;

        if (this.listeningsRecord.containsKey(album.getName())) {
            val = this.listeningsRecord.get(album.getName());
        } else {
            val = 0;
        }

        this.listeningsRecord.put(album.getName(), val + album.getListenings());
    }

    /**
     * @param playlist to add
     */
    public void addPodcast(final Podcast podcast) {
        this.podcasts.add(podcast);
    }

    /**
     * @param playlist to remove
     */
    public void removePodcast(final Podcast podcast) {
        this.podcasts.remove(podcast);
    }

    /**
     * @param username name of user
     * @return user queried by name
     */
    public User queryUser(final String username) {
        if (this.normalusers.containsKey(username)) {
            return this.normalusers.get(username);
        }

        if (this.artists.containsKey(username)) {
            return this.artists.get(username);
        }

        if (this.hosts.containsKey(username)) {
            return this.hosts.get(username);
        }

        return null;
    }

    /**
     * @param username name of normal user
     * @return queried normal user
     */
    public NormalUser queryNormalUser(final String username) {
        return this.normalusers.get(username);
    }

    /**
     * @param username name of artist
     * @return artist queried by name
     */
    public Artist queryArtist(final String username) {
        return this.artists.get(username);
    }

    /**
     * @param username name of artist
     * @return artist queried by name
     */
    public ArrayList<Artist> queryArtistsWithFilter(final FilterInput filter) {
        ArrayList<Artist> result = new ArrayList<>();

        for (var entry : this.artists.entrySet()) {
            if (entry.getValue().isMatchedByFilter(filter)) {
                result.add(entry.getValue());
            }
        }

        return result;
    }

    /**
     * @param username name of host
     * @return host queried by name
     */
    public Host queryHost(final String username) {
        return this.hosts.get(username);
    }

    /**
     * @param username name of host
     * @return host queried by name
     */
    public ArrayList<Host> queryHostsWithFilter(final FilterInput filter) {
        ArrayList<Host> result = new ArrayList<>();

        for (var entry : this.hosts.entrySet()) {
            if (entry.getValue().isMatchedByFilter(filter)) {
                result.add(entry.getValue());
            }
        }

        return result;
    }

    /**
     * @return list of all normal users
     */
    public ArrayList<NormalUser> queryAllNormalUsers() {
        ArrayList<NormalUser> list = new ArrayList<>();

        for (var entry : this.normalusers.entrySet()) {
            list.add(entry.getValue());
        }

        return list;
    }

    /**
     * @param filter used to match
     * @return ArrayList of matched songs
     */
    public ArrayList<Song> querySongs(final FilterInput filter) {
        ArrayList<Song> result = new ArrayList<>();

        for (var song : this.library) {
            if (song.isMatchedByFilter(filter)) {
                result.add(song);
            }
        }

        return result;
    }

    /**
     * Returns random song from given genre from the database using given seed
     * @param genre
     * @param seed
     * @return random song
     */
    public Song queryRandomSong(final String genre, final int seed) {
        ArrayList<Song> genreSongs = new ArrayList<>();

        for (var song : this.library) {
            if (song.getGenre().equals(genre)) {
                genreSongs.add(song);
            }
        }

        randGen.setSeed(seed);

        return genreSongs.get(randGen.nextInt(genreSongs.size()));
    }

    /**
     * Returns an ArrayList of playlists that are either public(visible) or
     * are owned by the user with name {@code owner}
     *
     * @param filter used to match
     * @param owner  name of playlist owner
     * @return ArrayList of matched playlists
     */
    public ArrayList<Playlist> queryVisiblePlaylistsAndOwnedBy(final FilterInput filter,
                                                               final String owner) {
        ArrayList<Playlist> result = new ArrayList<>();

        for (var playlist : this.playlists) {
            if (playlist.isMatchedByFilter(filter)) {
                if (playlist.isVisible()
                        || (!playlist.isVisible() && owner.equals(playlist.getOwner()))) {
                    result.add(playlist);
                }
            }
        }

        return result;
    }

    /**
     * @param filter used to match
     * @return ArrayList of matched podcasts
     */
    public ArrayList<Album> queryAlbums(final FilterInput filter) {
        ArrayList<Album> result = new ArrayList<>();
        ArrayList<String> helper = new ArrayList<>(this.artists.keySet());

        this.albums.sort(new Comparator<Album>() {
            public int compare(final Album album1, final Album album2) {
                return helper.indexOf(album1.getOwner()) - helper.indexOf(album2.getOwner());
            }
        });

        for (var album : this.albums) {
            if (album.isMatchedByFilter(filter)) {
                result.add(album);
            }
        }

        return result;
    }

    /**
     * @param name of album
     * @return queried album of null otherwise
     */
    public Album queryAlbum(final String name) {
        for (var album : this.albums) {
            if (album.getName().equals(name)) {
                return album;
            }
        }

        return null;
    }

    /**
     * @param filter used to match
     * @return ArrayList of matched podcasts
     */
    public ArrayList<Podcast> queryPodcasts(final FilterInput filter) {
        ArrayList<Podcast> result = new ArrayList<>();

        for (var podcast : this.podcasts) {
            if (podcast.isMatchedByFilter(filter)) {
                result.add(podcast);
            }
        }

        return result;
    }

    /**
     * @return ArrayList containing the names of the top 5 most liked songs
     */
    public Statistics top5SongsStatistics() {
        return new TopSongsStats(this.library);
    }

    /**
     * @return ArrayList containing the names of the top 5 most followed
     *         playlists
     */
    public Statistics top5PlaylistsStatistics() {
        return new TopPlaylistsStat(this.playlists);
    }

    /**
     * Computes top 5 albums by nr of likes
     * @return list of names of artists
     */
    public Statistics top5AlbumsStatistics() {
        return new TopAlbumsStats(this.albums);
    }

    /**
     * Computes top 5 artists by nr of likes
     * @return list of names of artists
     */
    public Statistics top5ArtistsStatistics() {
        return new TopArtistsStats(this.artists.values());
    }

    /**
     * @return list of all users name
     */
    public ArrayList<String> getAllUsersName() {
        ArrayList<String> result = new ArrayList<>();

        for (var entry : this.normalusers.entrySet()) {
            result.add(entry.getValue().getUsername());
        }

        for (var entry : this.artists.entrySet()) {
            result.add(entry.getValue().getUsername());
        }

        for (var entry : this.hosts.entrySet()) {
            result.add(entry.getValue().getUsername());
        }

        return result;
    }

    /**
     * @return list of names of online users
     */
    public ArrayList<String> getOnlineUsersName() {
        ArrayList<String> result = new ArrayList<>();

        for (var entry : this.normalusers.entrySet()) {
            if (entry.getValue().isActive()) {
                result.add(entry.getValue().getUsername());
            }
        }

        return result;
    }

    /**
     * Computes ranking of existing artists
     * @return ObjectNode containing the ranking
     */
    public ObjectNode getArtistRanking() {
        int rank = 0;
        ObjectMapper objMapper = new ObjectMapper();
        ObjectNode rankOutput, objNode = objMapper.createObjectNode();

        List<Artist> artistRanking = this.artists.values().stream()
                                    .sorted(Comparator.comparing(Artist::getTotalRevenue).reversed()
                                    .thenComparing(Artist::getUsername))
                                    .collect(Collectors.toList());

        for (var artist : artistRanking) {
            if (artist.hasBeenStreamed() || artist.hasRevenue()) {
                rank++;

                rankOutput = objMapper.createObjectNode();
                rankOutput.put("merchRevenue", (double) artist.getMerchRevenue());
                rankOutput.put("songRevenue", (double) Math.round(
                                                        artist.getSongRevenue() * 100) / 100);
                rankOutput.put("ranking", rank);
                rankOutput.put("mostProfitableSong", artist.getMostProfitableSongName());

                objNode.set(artist.getUsername(), rankOutput);
            }
        }

        return objNode;
    }
}
