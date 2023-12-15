package gwaves.tools;

import java.util.List;
import java.util.LinkedList;

import gwaves.context.NormalUser;

public class UserManager {
    private static UserManager instance;

    private int lastTimestamp;
    private LinkedList<NormalUser> users;

    static {
        instance = new UserManager();
    }

    private UserManager() {
        this.lastTimestamp = 0;
        this.users = new LinkedList<>();
    }

    public static void changeInstance() {
        instance = new UserManager();
    }

    public static UserManager getInstance() {
        return instance;
    }

    /**
     * Load users into the manager
     * @param users list of users to be loaded
     */
    public void loadUsers(List<NormalUser> users) {
        this.users.addAll(users);
    }

    /**
     * Unload all users from the user
     */
    public void reset() {
        this.users.clear();
    }

    /**
     * Adds the specified user to the list to be managed
     *
     * @param user to be added
     */
    public void addUser(NormalUser user) {
        this.users.add(user);
    }

    /**
     * Removes the specified user from the list to be managed
     *
     * @param user to be removed
     */
    public void rmvUser(NormalUser user) {
        this.users.remove(user);
    }

    /**
     * Updates all users on the platform
     */
    public void updateAll(int currentTimeStamp) {
        int timeInterval = currentTimeStamp - this.lastTimestamp;

        for (var user : this.users)
            if (user.isActive())
                user.runMusicPlayer(timeInterval);

        this.lastTimestamp = currentTimeStamp;
    }

    /**
     * 
     * @param user to be checked
     * @return true if no user is interacting with the specified user
     */
    public boolean isSafeToRemove(NormalUser user) {
        // for (var otherUser : this.users) {
        //     // TODO verific pe rand daca fiecare user are cumva incarcat
        //     // vreun playlist al altui user, album al altui artist sau
        //     // podcast al altui host
        // }

        return true;
    }
}
