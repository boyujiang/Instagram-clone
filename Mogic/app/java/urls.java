package com.cas.jiamin.mogic.Utility;

import java.util.ArrayList;

/**
 * urls class
 *
 * The class used to represent a global ArrayList of the uploads objects.
 */
public class urls {

    //The global ArrayList of the uploads objects.
    public static ArrayList<uploads> upArray = new ArrayList<>();

    /**
     * A simple method takes no arguments and empty the global ArrayList of the uploads objects.
     */
    public static void clean(){
        upArray = new ArrayList<>();
    }

}
