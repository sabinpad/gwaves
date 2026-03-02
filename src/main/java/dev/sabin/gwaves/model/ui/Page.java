package dev.sabin.gwaves.model.ui;

import dev.sabin.gwaves.model.context.User;

public interface Page {
    enum Type {
        OFNUSER,
        OFARTIST,
        OFHOST
    }

    /**
     * Method that returns the type of the page
     * @return type of page (useful for casting owner)
     */
    Type type();

    /**
     * Method that returns the user that the page belongs to
     * @return owner user of the page
     */
    User owner();

    /**
     * Method that strigifies the page
     * @return the contens of the page in String format
     */
    String strigify();
}
