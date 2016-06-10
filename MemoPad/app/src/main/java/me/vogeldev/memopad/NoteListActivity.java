package me.vogeldev.memopad;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

import me.vogeldev.memopad.data.NotepadContract;

public class NoteListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.MultiChoiceModeListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private static final int NOTES_LOADER = 1;
    private static final String[] PROJECTION = {NotepadContract.NoteEntry._ID, NotepadContract.NoteEntry.COLUMN_TITLE};
    private SimpleCursorAdapter adapter;
    ArrayList<Long> selectedIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        ListView listView = (ListView)findViewById(R.id.listView);
        String[] from = new String[] {NotepadContract.NoteEntry.COLUMN_TITLE};
        int[] to = new int[] {android.R.id.text1};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_activated_1, null, from, to, 0);
        listView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(NOTES_LOADER, null, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);

        FloatingActionButton  fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case NOTES_LOADER:
                return new CursorLoader(this, NotepadContract.NoteEntry.CONTENT_URI, PROJECTION, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);

        //  This will swap two cursors with one another, but does not close cursor
        //  adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

    @Override
    // This is called when an item in the ListView is clicked while the CAB is active
    // this will either add or remove the note from the list to be deleted
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if(checked) selectedIds.add(id);
        else selectedIds.remove(id);
        mode.setTitle(String.valueOf(selectedIds.size()));
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
    // Handles what happens when the user clicks an action button
    // The only action button we have is the delete button
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch(item.getItemId()){
            case R.id.actoin_delete:
                for(long id : selectedIds){
                    // Deletes the given note from the db
                    getContentResolver().delete(
                            ContentUris.withAppendedId(NotepadContract.NoteEntry.CONTENT_URI, id),
                            null,
                            null
                    );
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    // Clears the temporary list when the user exits the delete screen
    public void onDestroyActionMode(ActionMode mode) {
        selectedIds.clear();
    }

    @Override
    // This handles when a user clicks the floating action button to add a new note
    public void onClick(View v) {
        Intent intent = new Intent(this, NoteEditActivity.class);
        intent.setAction(NoteEditActivity.ACTION_NEW);
        startActivity(intent);
        adapter.notifyDataSetChanged();
    }

    @Override
    // This handles when the user clicks an item on the ListView to edit
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, NoteEditActivity.class);

        // These do the same thing, but since we have a URI we can use the setData option
        // intent.putExtra(NotepadContract.NoteEntry._ID, id);

        intent.setData(ContentUris.withAppendedId(NotepadContract.NoteEntry.CONTENT_URI, id));

        intent.setAction(NoteEditActivity.ACTION_EDIT);
        startActivity(intent);
        adapter.notifyDataSetChanged();
    }
}
