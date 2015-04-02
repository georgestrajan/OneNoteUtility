package com.georgestrajan.onenoteutility;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.microsoft.live.LiveAuthClient;
import com.microsoft.live.LiveAuthException;
import com.microsoft.live.LiveAuthListener;
import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveConnectSession;
import com.microsoft.live.LiveStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ActionBarActivity implements LiveAuthListener {

    public final static String LOGIN_MESSAGE = "com.georgestrajan.onenoteutility.loginmessage";

    private LiveAuthClient mAuthClient;
    private LiveConnectClient mLiveConnectClient;
    private TextView resultTextView;
    private String mAccessToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTextView = (TextView) findViewById(R.id.txtView_Auth);
        mAuthClient = new LiveAuthClient(this, Constants.CLIENTID);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAuthComplete(LiveStatus status, LiveConnectSession session, Object userState)
    {

        if(status == LiveStatus.CONNECTED) {
            resultTextView.setText(R.string.auth_yes);
            mAccessToken = session.getAccessToken();
            mLiveConnectClient = new LiveConnectClient(session);
        }
        else {
            resultTextView.setText(R.string.auth_no);
            mLiveConnectClient = null;
        }
    }

    public void onAuthError(LiveAuthException exception, Object userState)
    {
        resultTextView.setText(getResources().getString(R.string.auth_err) + exception.getMessage());
        mLiveConnectClient = null;
    }

    /** Called when the user clicks the Authenticate Button */
    public void callOAuth(View view) {

        super.onStart();
        Iterable<String> scopes = Arrays.asList("office.onenote_create");
        mAuthClient.login(this, scopes, this);

    }

    private void StartRequest() {
        TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
        tvStatus.setText("Status: Sending request...");

    }

    public void btn_fetchnotebooks_click(View view)
    {
        StartRequest();

        String url = "https://www.onenote.com/api/v1.0/notebooks";
        OneNoteRequest oneNoteRequest = new OneNoteRequest(this.mAccessToken, this);

        oneNoteRequest.SendGetRequest(url, new OneNoteResponse.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                TextView tvStatus = (TextView) findViewById(R.id.tvStatus);

                try {

                    JSONArray noteBooks = response.getJSONArray("value");

                    ArrayList<OneNoteNotebook> noteBooksArrayList = new ArrayList<OneNoteNotebook>();
                    for (int notebook = 0; notebook < noteBooks.length(); notebook++) {

                        JSONObject json_data = noteBooks.getJSONObject(notebook);

                        OneNoteNotebook noteBook = new OneNoteNotebook();
                        noteBook.id = json_data.getString("id");
                        noteBook.name = json_data.getString("name");
                        noteBook.sectionsUrl = json_data.getString("sectionsUrl");
                        noteBooksArrayList.add(noteBook);
                    }

                    Intent noteBookSelectionIntent = new Intent(MainActivity.this, SelectNotebookActivity.class);
                    noteBookSelectionIntent.putExtra("notebooks", noteBooksArrayList);
                    noteBookSelectionIntent.putExtra("token", mAccessToken);
                    startActivity(noteBookSelectionIntent);

                } catch (JSONException jsEx) {
                    tvStatus.setText("Error occurred: " + jsEx.toString());
                }
            }
        });

     }

}
