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
                CommandExecuter.execAddUser(commandInput, commandOutput);
                break;
            case "deleteUser":
                CommandExecuter.execDeleteUser(commandInput, commandOutput);
                break;
            case "addAlbum":
                CommandExecuter.execAddAlbum(commandInput, commandOutput);
                break;
            case "removeAlbum":
                CommandExecuter.execRemoveAlbum(commandInput, commandOutput);
                break;
            case "addEvent":
                CommandExecuter.execAddEvent(commandInput, commandOutput);
                break;
            case "removeEvent":
                CommandExecuter.execRemoveEvent(commandInput, commandOutput);
                break;
            case "addMerch":
                CommandExecuter.execAddMerch(commandInput, commandOutput);
                break;
            case "addPodcast":
                CommandExecuter.execAddPodcast(commandInput, commandOutput);
                break;
            case "removePodcast":
                CommandExecuter.execRemovePodcast(commandInput, commandOutput);
                break;
            case "addAnnouncement":
                CommandExecuter.execAddAnnouncement(commandInput, commandOutput);
                break;
            case "removeAnnouncement":
                CommandExecuter.execRemoveAnnouncement(commandInput, commandOutput);
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
                CommandExecuter.execShowAlbums(commandInput, commandOutput);
                break;
            case "showPodcasts":
                CommandExecuter.execShowPodcasts(commandInput, commandOutput);
                    break;
            case "switchConnectionStatus":
                CommandExecuter.execSwitchConnectionStatus(commandInput, commandOutput);
                break;
            case "getTop5Songs":
                CommandExecuter.execGetTop5Songs(commandInput, commandOutput);
                break;
            case "getTop5Playlists":
                CommandExecuter.execGetTop5Playlists(commandInput, commandOutput);
                break;
            case "getTop5Albums":
                CommandExecuter.execGetTop5AlbumsName(commandInput, commandOutput);
                break;
            case "getTop5Artists":
                CommandExecuter.execGetTop5ArtistsName(commandInput, commandOutput);
                break;
            case "getAllUsers":
                CommandExecuter.execGetAllUsersName(commandInput, commandOutput);
                break;
            case "getOnlineUsers":
                CommandExecuter.execGetOnlineUsersName(commandInput, commandOutput);
                break;
            default:
                break;
        }

        return commandOutput;
    }

    private static void execSearch(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());
        
        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        commandOutput.setResults(normaluser.doSearch(commandInput.getType(), commandInput.getFilters()));
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execSelect(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doSelect(commandInput.getItemNumber());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execLoad(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doLoad();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execPlayPause(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doPlayPause();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execRepeat(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doRepeat();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execShuffle(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        if (commandInput.getSeed() == null) {
            normaluser.doShuffle(0);
        } else {
            normaluser.doShuffle(commandInput.getSeed());
        }

        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execForward(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doForward();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execBackward(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doBackward();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execLike(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doLike();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execNext(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doNext();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execPrev(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doPrev();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execAddRemoveInPlaylist(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doAddRemoveInPlaylist(commandInput.getPlaylistId());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }
    
    private static void execStatus(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        commandOutput.setStats(normaluser.doMPlayerStatus());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execCreatePlaylist(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doCreatePlaylist(commandInput.getPlaylistName());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execSwitchVisibility(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doSwitchVisibility(commandInput.getPlaylistId());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execFollow(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doFollowPlaylist();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execShowPlaylists(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());
        
        commandOutput.setResult(normaluser.doShowPlaylists());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    private static void execShowPreferredSongs(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        commandOutput.setResult(normaluser.getPreferredSongsName());
    }

    private static void execGetTop5Songs(CommandInput commandInput, CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getTop5SongsName());
    }

    private static void execGetTop5Playlists(CommandInput commandInput, CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getTop5PlaylistsName());
    }

    //

    private static void execSwitchConnectionStatus(CommandInput commandInput, CommandOutput commandOutput) {
        NormalUser user = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return;
        }

        user = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (user == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not a normal user.");
            return;
        }

        user.doSwitchStatus();
  
        commandOutput.setMessage(user.getLastCommandMessage());
    }

    private static void execAddUser(CommandInput commandInput, CommandOutput commandOutput) {
        DataBase database = DataBase.getInstance();

        if (database.queryUser(commandInput.getUsername()) != null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " is already taken.");
            return;
        }

        switch (commandInput.getType()) {
            case "user":
                database.addNormalUser(new NormalUser(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity()));
                break;
            case "artist":
                database.addArtist(new Artist(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity()));
                break;
            case "host":
                database.addHost(new Host(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity()));
                break;
            default:
                return;
        }

        commandOutput.setMessage("The username " + commandInput.getUsername() + " has been added successfully.");
    }

    private static void execDeleteUser(CommandInput commandInput, CommandOutput commandOutput) {
        User user = null;
        DataBase database = DataBase.getInstance();
        
        user = database.queryUser(commandInput.getUsername());

        if (user == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return;
        }

        // if (!UserManager.getInstance().isSafeToRemove(user)) {
        //     commandOutput.setMessage(commandInput.getUsername() + " can't be deleted.");
        //     return;
        // }
        
        // TODO
        // database.removeUser(user);

        commandOutput.setMessage(commandInput.getUsername() + " was successfully deleted.");
    }

    private static void execAddAlbum(CommandInput commandInput, CommandOutput commandOutput) {
        Artist artist = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return;
        }

        artist = DataBase.getInstance().queryArtist(commandInput.getUsername());

        if (artist == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not an artist.");
            return;
        }

        artist.doAddAlbum(commandInput.getName(),
                          commandInput.getUsername(),
                          commandInput.getReleaseYear(),
                          commandInput.getDescription(),
                          commandInput.getSongs());
  
        commandOutput.setMessage(artist.getLastCommandMessage());
    }

    private static void execRemoveAlbum(CommandInput commandInput, CommandOutput commandOutput) {
        Artist artist = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return;
        }

        artist = DataBase.getInstance().queryArtist(commandInput.getUsername());

        if (artist == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not an artist.");
            return;
        }

        artist.doRmvAlbum(commandInput.getName());
  
        commandOutput.setMessage(artist.getLastCommandMessage());
    }

    private static void execAddEvent(CommandInput commandInput, CommandOutput commandOutput) {
        Artist artist = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return;
        }

        artist = DataBase.getInstance().queryArtist(commandInput.getUsername());

        if (artist == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not an artist.");
            return;
        }

        artist.doAddEvent(commandInput.getName(),
                          commandInput.getDescription(),
                          commandInput.getDate());
  
        commandOutput.setMessage(artist.getLastCommandMessage());
    }

    private static void execRemoveEvent(CommandInput commandInput, CommandOutput commandOutput) {
        Artist artist = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return;
        }

        artist = DataBase.getInstance().queryArtist(commandInput.getUsername());

        if (artist == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not an artist.");
            return;
        }

        artist.doRmvEvent(commandInput.getName());
  
        commandOutput.setMessage(artist.getLastCommandMessage());
    }

    private static void execAddMerch(CommandInput commandInput, CommandOutput commandOutput) {
        Artist artist = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return;
        }

        artist = DataBase.getInstance().queryArtist(commandInput.getUsername());

        if (artist == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not an artist.");
            return;
        }

        artist.doAddMerch(commandInput.getName(),
                          commandInput.getDescription(),
                          commandInput.getPrice());
  
        commandOutput.setMessage(artist.getLastCommandMessage());
    }

    private static void execShowAlbums(CommandInput commandInput, CommandOutput commandOutput) {
        Artist artist = DataBase.getInstance().queryArtist(commandInput.getUsername());
        commandOutput.setResult(artist.doShowAlbums());
    }

    private static void execAddPodcast(CommandInput commandInput, CommandOutput commandOutput) {
        Host host = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return;
        }

        host = DataBase.getInstance().queryHost(commandInput.getUsername());

        if (host == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not a host.");
            return;
        }

        host.doAddPodcast(commandInput.getName(),
                          commandInput.getUsername(),
                          commandInput.getEpisodes());
  
        commandOutput.setMessage(host.getLastCommandMessage());
    }

    private static void execRemovePodcast(CommandInput commandInput, CommandOutput commandOutput) {
        Host host = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return;
        }

        host = DataBase.getInstance().queryHost(commandInput.getUsername());

        if (host == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not a host.");
            return;
        }

        host.doRmvPodcast(commandInput.getName());
  
        commandOutput.setMessage(host.getLastCommandMessage());
    }

    private static void execAddAnnouncement(CommandInput commandInput, CommandOutput commandOutput) {
        Host host = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return;
        }

        host = DataBase.getInstance().queryHost(commandInput.getUsername());

        if (host == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not a host.");
            return;
        }

        host.doAddAnnouncement(commandInput.getName(), commandInput.getDescription());
  
        commandOutput.setMessage(host.getLastCommandMessage());
    }

    private static void execRemoveAnnouncement(CommandInput commandInput, CommandOutput commandOutput) {
        Host host = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername() + " doesn't exist.");
            return;
        }

        host = DataBase.getInstance().queryHost(commandInput.getUsername());

        if (host == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not a host.");
            return;
        }

        host.doRmvAnnouncement(commandInput.getName());
  
        commandOutput.setMessage(host.getLastCommandMessage());
    }

    private static void execShowPodcasts(CommandInput commandInput, CommandOutput commandOutput) {
        Host host = DataBase.getInstance().queryHost(commandInput.getUsername());
        commandOutput.setResult(host.doShowPodcasts());
    }

    private static void execGetTop5AlbumsName(CommandInput commandInput, CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getTop5AlbumsName());
    }

    private static void execGetTop5ArtistsName(CommandInput commandInput, CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getTop5ArtistsName());
    }

    private static void execGetAllUsersName(CommandInput commandInput, CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getAllUsersName());
    }

    private static void execGetOnlineUsersName(CommandInput commandInput, CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getOnlineUsersName());
    }
}
