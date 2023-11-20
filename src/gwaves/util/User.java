package gwaves.util;

import java.util.ArrayList;
import java.util.HashMap;

import fileio.input.CommandInput;
import fileio.output.UserCommandOutput;

import fileio.input.UserInput;

import gwaves.sample.Song;
import gwaves.collection.Playlist;

public class User {
    private String username;
    private Integer age;
    private String city;

    private HashMap<String, Playlist> playlists;
    private ArrayList<Song> likedSongs;

    private Searchbar searchbar;
    private Musicplayer musicplayer;

    public User(UserInput userInput)
    {
        this.username = userInput.getUsername();
        this.age = userInput.getAge();
        this.city = userInput.getCity();
        this.playlists = new HashMap<>();
        this.likedSongs = new ArrayList<>();
        this.searchbar = new Searchbar();
        this.musicplayer = new Musicplayer();
    }

    public UserCommandOutput executeCommand(CommandInput commandInput)
    {
        UserCommandOutput output;

        output = new UserCommandOutput();
        output.setCommand(commandInput.getCommand());
        output.setUser(commandInput.getUsername());
        output.setTimestamp(commandInput.getTimestamp());

        switch (commandInput.getCommand()) {
        case "search":
            switch (commandInput.getType()) {
            case "song":
                output.setResults(this.searchbar.searchSongs(commandInput.getFilters()));
                break;
            case "playlist":
                output.setResults(this.searchbar.searchPlaylists(commandInput.getFilters()));
                break;
            case "podcast":
                output.setResults(this.searchbar.searchPodcasts(commandInput.getFilters()));
                break;
            }

            output.setMessage("Search returned " + output.getResults().size() + " results");
            break;
        case "select":

            break;
        case "load":

            break;
        case "playPause":

            break;
        case "repeat":

            break;
        case "shuffle":

            break;
        case "forward":

            break;
        case "backward":

            break;
        case "like":

            break;
        case "next":

            break;
        case "prev":

            break;
        case "addRemoveInPlaylist":

            break;
        case "status":

            break;
        }

        return output;
    }
}
