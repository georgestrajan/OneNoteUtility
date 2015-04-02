package com.georgestrajan.onenoteutility;

import java.io.Serializable;

/**
 * Created by georgestrajan on 3/30/15.
 */
public class OneNotePage implements Serializable {
    public String id;
    public String title;
    public String contentUrl;

    public @Override String toString() {
        return this.title;
    }
}
