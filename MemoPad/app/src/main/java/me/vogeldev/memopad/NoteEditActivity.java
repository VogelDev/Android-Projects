package me.vogeldev.memopad;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

import me.vogeldev.memopad.data.NotepadContract;
import me.vogeldev.memopad.data.NotepadDbHelper;

/**
 * Created by Vogel on 6/9/2016.
 */
public class NoteEditActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ACTION_NEW = "action_new";
    public static final String ACTION_EDIT = "action_edit";
    private static final int TASKS_LOADER = 1;
    private static final String[] PROJECTION = {NotepadContract.NoteEntry._ID, NotepadContract.NoteEntry.COLUMN_TASK};

    private Spinner spinnerType;

    ArrayAdapter<String> adapter;
    ArrayList<String> tasks;
    private SimpleCursorAdapter cursorAdapter;

    private EditText etTitle;
    private EditText etDesc;
    private EditText etTask;

    private Uri note;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        final Intent intent = getIntent();

        // Check intent action for new note or edit note
        if(intent.getAction().equals(ACTION_NEW)){
            ContentValues initialValues = new ContentValues();
            initialValues.put(NotepadContract.NoteEntry.COLUMN_TITLE, "");
            initialValues.put(NotepadContract.NoteEntry.COLUMN_DESCRIPTION, "");
            note = getContentResolver().insert(NotepadContract.NoteEntry.NOTE_CONTENT_URI, initialValues);
        }else if(intent.getAction().equals(ACTION_EDIT)){
            note = intent.getData();
        }

        id = Long.valueOf(note.getLastPathSegment());

        Log.i("table", note.toString());

        etTitle = (EditText)findViewById(R.id.et_title);
        etDesc = (EditText)findViewById(R.id.et_description);

        Cursor cursor = getContentResolver().query(note, null, null, null, null);
        // This has the potential to fail, but since we're either pulling a note or creating
        // a new one, we always have an entry from the db
        cursor.moveToFirst();

        Log.i("table", Arrays.toString(cursor.getColumnNames()));

        // get information saved in db for this note and set form to appropriate values
        etTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(NotepadContract.NoteEntry.COLUMN_TITLE)));
        etDesc.setText(cursor.getString(cursor.getColumnIndexOrThrow(NotepadContract.NoteEntry.COLUMN_DESCRIPTION)));

        // Close the cursor, we're done with it
        cursor.close();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.edit_memo);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        // This has the potential to fail, but we are explicitly setting the actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tasks = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, tasks);

        getSupportLoaderManager().initLoader(TASKS_LOADER, null, this);
        
        final ListView listView = (ListView)findViewById(R.id.listView_task);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        SQLiteDatabase db = new NotepadDbHelper(this).getReadableDatabase();
        cursor = db.query("task", null, "note_id = " + id, null, null, null, "_ID");
        Log.i("DB_CHECK", Arrays.toString(cursor.getColumnNames()));
        Log.i("DB_CHECK", String.valueOf(cursor.getCount()));
        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            do{
                tasks.add(cursor.getString(2));
                
            }while(cursor.moveToNext());

            adapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
        etTask = (EditText)findViewById(R.id.et_addTask);

        final FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.add(etTask.getText().toString());

                ContentValues values = new ContentValues();
                values.put(NotepadContract.NoteEntry.COLUMN_NOTE_ID, id);
                values.put(NotepadContract.NoteEntry.COLUMN_TASK, etTask.getText().toString());
                getContentResolver().insert(NotepadContract.NoteEntry.TASK_CONTENT_URI, values);

                adapter.notifyDataSetChanged();
            }
        });


        // Create array adapter for unit spinners based on string array in the strings.xml file
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.edit_types));
        spinAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerType = (Spinner)findViewById(R.id.spinnerType);
        spinnerType.setAdapter(spinAdapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                switch(position){
                    case 0:
                        fab.setVisibility(View.INVISIBLE);
                        etTask.setHint("");
                        etTask.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        break;
                    case 1:
                        fab.setVisibility(View.VISIBLE);
                        etTask.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            // Our spinner has no option for no selection
            public void onNothingSelected(AdapterView<?> parentView) {}

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            // This option is the "back" button in the actionbar
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

    // Takes the information from the form and updates the entry in the db
    private void save(){
        ContentValues values = new ContentValues();
        values.put(NotepadContract.NoteEntry.COLUMN_TITLE, etTitle.getText().toString());
        values.put(NotepadContract.NoteEntry.COLUMN_DESCRIPTION, etDesc.getText().toString());
        getContentResolver().update(note, values, null, null);
    }

    @Override
    // The onPause method is always called when the activity stops running,
    // so we save here to ensure data isn't lost
    protected void onPause() {
        super.onPause();
        save();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tasks.remove(position);

        Log.i("Task ID", String.valueOf(id));

        // Deletes the given note from the db
        getContentResolver().delete(
                ContentUris.withAppendedId(NotepadContract.NoteEntry.TASK_CONTENT_URI, id),
                null,
                null
        );

        adapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case TASKS_LOADER:
                return new CursorLoader(this, NotepadContract.NoteEntry.TASK_CONTENT_URI, PROJECTION, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.changeCursor(data);

        //  This will swap two cursors with one another, but does not close cursor
        //  adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.changeCursor(null);
    }
}
