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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SelectNotebookActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_notebook);

        Bundle bundle = getIntent().getExtras();
        ArrayList<OneNoteNotebook> noteBooksArrayList = (ArrayList<OneNoteNotebook>)bundle.get("notebooks");
        final String accessToken = bundle.getString("token");

        final ListAdapter notebookAdapter = new ArrayAdapter<OneNoteNotebook>(SelectNotebookActivity.this, android.R.layout.simple_list_item_1, noteBooksArrayList);
        final ListView noteBooksListView = (ListView)findViewById(R.id.list_notebooks);

        noteBooksListView.setAdapter(notebookAdapter);

        noteBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 OneNoteNotebook selectedNotebook = (OneNoteNotebook)noteBooksListView.getItemAtPosition(position);
                 // get the sections of this notebook

                 String url = selectedNotebook.sectionsUrl;
                 OneNoteRequest oneNoteRequest = new OneNoteRequest(accessToken, SelectNotebookActivity.this);
                 oneNoteRequest.SendGetRequest(url, new OneNoteResponse.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {

                         try {

                             JSONArray sections = response.getJSONArray("value");

                             ArrayList<OneNoteSection> sectionsArrayList = new ArrayList<OneNoteSection>();

                             for (int index = 0; index < sections.length(); index++) {
                                 JSONObject json_data = sections.getJSONObject(index);

                                 OneNoteSection section = new OneNoteSection();
                                 section.id = json_data.getString("id");
                                 section.name = json_data.getString("name");
                                 section.pagesUrl = json_data.getString("pagesUrl");

                                 sectionsArrayList.add(section);
                             }

                             Intent sectionSelectionIntent = new Intent(SelectNotebookActivity.this, SelectSectionActivity.class);
                             sectionSelectionIntent.putExtra("sections", sectionsArrayList);
                             sectionSelectionIntent.putExtra("token", accessToken);
                             startActivity(sectionSelectionIntent);

                         } catch (JSONException jsEx) {
                             // TODO Handle exceptions!!
                         }
                     }
                 });
             }
         });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_notebook, menu);
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
