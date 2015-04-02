package com.georgestrajan.onenoteutility;

import java.io.Serializable;

/**
 * Created by georgestrajan on 3/30/15.
 */
public class OneNoteNotebook implements Serializable {
    public String id;
    public String name;
    public String sectionsUrl;

    public @Override String toString() {
        return  this.name;
    }
}
