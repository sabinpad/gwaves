package dev.sabin.gwaves.service.util;

import dev.sabin.gwaves.model.io.input.FilterInput;

/**
 * This interface ensures that an object that implements it has the necessary
 * fields to be matched by an instance of the FilterInput class
 */

public interface Filterable {
    /**
     * Method that checks if an objected is matched by a FilterInput object
     * @param filter specified filter
     * @return true if the object matches the provided filter
     */
    boolean isMatchedByFilter(FilterInput filter);
}
