package com.georgestrajan.onenoteutility;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SelectPageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_page);

        Bundle bundle = getIntent().getExtras();
        final String accessToken = bundle.getString("token");
        ArrayList<OneNotePage> pagesArrayList = (ArrayList<OneNotePage>)bundle.get("pages");

        final ListAdapter listAdapter = new ArrayAdapter<OneNotePage>(SelectPageActivity.this, android.R.layout.simple_list_item_1, pagesArrayList);
        final ListView listView = (ListView)findViewById(R.id.list_pages);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                OneNotePage selectedPage = (OneNotePage)listView.getItemAtPosition(position);

                // get the pages list of this section

                Intent pageContentIntent = new Intent(SelectPageActivity.this, PageContentActivity.class);
                pageContentIntent.putExtra("page", selectedPage);
                pageContentIntent.putExtra("token", accessToken);
                startActivity(pageContentIntent);

                /*
                String url = selectedPage.contentUrl;
                OneNoteRequest oneNoteRequest = new OneNoteRequest(accessToken, SelectPageActivity.this);

                oneNoteRequest.SendGetRequest(url, new OneNoteResponse.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray sections = response.getJSONArray("value");

                            ArrayList<OneNotePage> pagesArrayList = new ArrayList<OneNotePage>();

                            for (int index = 0; index < sections.length(); index++) {
                                JSONObject json_data = sections.getJSONObject(index);

                                OneNotePage page = new OneNotePage();
                                page.id = json_data.getString("id");
                                page.title = json_data.getString("title");
                                page.contentUrl = json_data.getString("contentUrl");

                                pagesArrayList.add(page);
                            }

                            Intent pageSelectionIntent = new Intent(SelectSectionActivity.this, SelectPageActivity.class);
                            pageSelectionIntent.putExtra("pages", pagesArrayList);
                            pageSelectionIntent.putExtra("token", accessToken);
                            startActivity(pageSelectionIntent);

                        } catch (JSONException jsEx) {
                            // TODO Handle exceptions!!
                        }
                    }
                });

                */
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_page, menu);
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
}
