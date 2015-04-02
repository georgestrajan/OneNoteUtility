package com.georgestrajan.onenoteutility;

import java.io.Serializable;

/**
 * Created by georgestrajan on 3/30/15.
 */
public class OneNoteSection implements Serializable {
    public String id;
    public String name;
    public String pagesUrl;

    public @Override String toString() {
        return this.name;
    }
}
