package gwaves.ui;

import gwaves.context.User;

public interface Page {
    public static enum Type {
        OFNUSER,
        OFARTIST,
        OFHOST
    }

    /**
     * Method that returns the type of the page
     * @return type of page
     */
    Type type();

    /**
     * Method that returns the user that the page belongs to
     * @return user that owns the page
     */
    User owner();

    /**
     * Method that strigifies the page
     * @return the page
     */
    String strigify();
}
