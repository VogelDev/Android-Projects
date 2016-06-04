package me.vogeldev.worldgeography;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements AbsListView.MultiChoiceModeListener {

    private Toolbar toolbar;
    private ListView listView;

    private ArrayList<String> countries;
    private ArrayList<String> selected;

    private ArrayAdapter<String> adapter;

    private boolean gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selected = new ArrayList<>();       // list of currently selected objects
        countries = new ArrayList<>();      // list of all objects
        initCountryList();                  // initializes countries list with arrays saved in strings.xml

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, countries);

        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }

    private void initCountryList(){
        // adds countries in arrays saved in strings.xml
        countries.addAll(Arrays.asList(getResources().getStringArray(R.array.ficticious_countries)));
        countries.addAll(Arrays.asList(getResources().getStringArray(R.array.countries_array)));

        Collections.sort(countries);            // sort list so fictitious countries are "shuffled" in

        // flag if the user has removed all fictitious countries
        // this should be reset whenever the user refreshes the list
        gameOver = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                // this application doesn't have settings at the moment
                return true;
            case R.id.action_refresh:
                // return list to its original state and notify adapter to update the ListView
                countries.clear();
                initCountryList();
                adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

        // handle whether the user selected or deselected the given item
        if(checked)
            selected.add(adapter.getItem(position));
        else
            selected.remove(adapter.getItem(position));

        // display the number of items currently selected
        mode.setTitle(String.valueOf(selected.size()));
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cab, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_delete:
                // user deleted the items
                // cycle through list of selected items and remove from master list
                // notifying the adapter to update the ListView
                for(String country : selected){
                    countries.remove(country);
                }
                adapter.notifyDataSetChanged();

                // end the ActionMode, we're done with it
                mode.finish();
                return true;
            default:
                // this shouldn't happen
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

        // check if fictitious countries are still in the master list
        for(String fake : Arrays.asList(getResources().getStringArray(R.array.ficticious_countries))){
            // if a single item is found we can break out of the list,
            // no need to keep looking
            if(countries.contains(fake)){
                selected.clear();
                return;
            }
        }
        // made it out of the loop, therefore fictitious countries were not found
        gameOver = true;
        // alert the user they have completed the "game"
        Toast.makeText(this, "Congratulations, you have destroyed the impostor countries!", Toast.LENGTH_LONG).show();
        selected.clear();
    }
}
