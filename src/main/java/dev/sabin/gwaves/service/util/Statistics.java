package dev.sabin.gwaves.service.util;

import java.util.List;

public interface Statistics {
    static final int MAXNUMBER = 5;

    /**
     * Computes the statistics dependong on the implementation on arguments
     * passed to the constructor
     * @return list containg the names of the objects that were used
     */
    List<String> results();
}
