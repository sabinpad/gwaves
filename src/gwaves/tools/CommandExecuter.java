package gwaves.tools;

import fileio.input.CommandInput;
import fileio.output.CommandOutput;

import gwaves.context.*;
import gwaves.storage.DataBase;

public class CommandExecuter {
    public static CommandOutput run(CommandInput commandInput) {
        CommandOutput commandOutput = new CommandOutput();

        commandOutput.setCommand(commandInput.getCommand());
        commandOutput.setUser(commandInput.getUsername());
        commandOutput.setTimestamp(commandInput.getTimestamp());

        switch (commandInput.getCommand()) {
            case "addUser":
                break;
            case "deleteUser":
                break;
            case "addAlbum":
                break;
            case "removeAlbum":
                break;
            case "addEvent":
                break;
            case "removeEvent":
                break;
            case "addMerch":
                break;
            case "addPodcast":
                break;
            case "removePodcast":
                break;
            case "addAnnouncement":
                break;
            case "removeAnnouncement":
                break;
            case "search":
                CommandExecuter.execSearch(commandInput, commandOutput);
                break;
            case "select":
                CommandExecuter.execSelect(commandInput, commandOutput);
                break;
            case "load":
                CommandExecuter.execLoad(commandInput, commandOutput);
                break;
            case "playPause":
                CommandExecuter.execPlayPause(commandInput, commandOutput);
                break;
            case "repeat":
                CommandExecuter.execRepeat(commandInput, commandOutput);
                break;
            case "shuffle":
                CommandExecuter.execShuffle(commandInput, commandOutput);
                break;
            case "forward":
                CommandExecuter.execForward(commandInput, commandOutput);
                break;
            case "backward":
                CommandExecuter.execBackward(commandInput, commandOutput);
                break;
            case "like":
                CommandExecuter.execLike(commandInput, commandOutput);
                break;
            case "next":
                CommandExecuter.execNext(commandInput, commandOutput);
                break;
            case "prev":
                CommandExecuter.execPrev(commandInput, commandOutput);
                break;
            case "createPlaylist":
                CommandExecuter.execCreatePlaylist(commandInput, commandOutput);
                break;
            case "addRemoveInPlaylist":
                CommandExecuter.execAddRemoveInPlaylist(commandInput, commandOutput);
                break;
            case "switchVisibility":
                CommandExecuter.execSwitchVisibility(commandInput, commandOutput);
                break;
            case "showPlaylists":
                CommandExecuter.execShowPlaylists(commandInput, commandOutput);
                break;
            case "follow":
                CommandExecuter.execFollow(commandInput, commandOutput);
                break;
            case "status":
                CommandExecuter.execStatus(commandInput, commandOutput);
                break;
            case "showPreferredSongs":
                CommandExecuter.execShowPreferredSongs(commandInput, commandOutput);
                break;
            case "showAlbums":
                break;
            case "showPodcasts":
                break;
            case "switchConnectionStatus":
                break;
            case "getPreferredGenre":
                break;
            case "getTop5Songs":
                CommandExecuter.execGetTop5Songs(commandInput, commandOutput);
                break;
            case "getTop5Playlists":
                CommandExecuter.execGetTop5Playlists(commandInput, commandOutput);
                break;
            case "getTop5Albums":
                break;
            case "getTop5Artists":
                break;
            case "getAllUsers":
                break;
            case "getOnlineUsers":
                break;
            default:
                break;
        }

        return commandOutput;
    }

    private static void execSearch(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());
        
        commandOutput.setResults(user.doSearch(commandInput.getType(), commandInput.getFilters()));
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execSelect(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doSelect(commandInput.getItemNumber());
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execLoad(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doLoad();
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execPlayPause(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doPlayPause();
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execRepeat(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doRepeat();
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execShuffle(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        if (commandInput.getSeed() == null) {
            user.doShuffle(0);
        } else {
            user.doShuffle(commandInput.getSeed());
        }

        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execForward(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doForward();
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execBackward(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doBackward();
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execLike(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doLike();
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execNext(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doNext();
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execPrev(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doPrev();
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execAddRemoveInPlaylist(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doAddRemoveInPlaylist(commandInput.getPlaylistId());
        commandOutput.setMessage(user.getLastCommandMessage());
    }
    
    private static void execStatus(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        commandOutput.setStats(user.doStatus());
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execCreatePlaylist(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doCreatePlaylist(commandInput.getPlaylistName());
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execSwitchVisibility(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doSwitchVisibility(commandInput.getPlaylistId());
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execFollow(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        user.doFollowPlaylist();
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execShowPlaylists(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());
        
        commandOutput.setResult(user.doShowPlaylists());
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execShowPreferredSongs(CommandInput commandInput, CommandOutput commandOutput) {
        User user = DataBase.getInstance().queryUser(commandInput.getUsername());

        commandOutput.setResult(user.getPreferredSongsName());
    }

    private static void execGetTop5Songs(CommandInput commandInput, CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getTop5SongsName());
    }

    private static void execGetTop5Playlists(CommandInput commandInput, CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getTop5PlaylistsName());
    }

    //

    private static void execAddUser(CommandInput commandInput, CommandOutput commandOutput) {
        User newUser;

        switch (commandInput.getType()) {
            case "user":
                newUser = new User(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity());
                break;
            case "artist":
                newUser = new Artist(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity());
                break;
            case "host":
                newUser = new Host(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity());
                break;
        }
        
        DataBase.getInstance();
    }
}
