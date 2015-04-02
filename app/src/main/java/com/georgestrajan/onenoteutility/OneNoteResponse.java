package com.georgestrajan.onenoteutility;

/**
 * Created by georgestrajan on 3/24/15.
 * Encapsulates a OneNote API response.
 *
 * @param <T> Parsed type of this response
 */
public class OneNoteResponse {

    /** Callback interface for delivering parsed responses. */
    public interface Listener<T> {
        /** Called when a response is received. */
        public void onResponse(T response);
    }

}
