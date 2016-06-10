package me.vogeldev.memopad;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import me.vogeldev.memopad.data.NotepadContract;

/**
 * Created by Vogel on 6/9/2016.
 */
public class NoteEditActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String ACTION_NEW = "action_new";
    public static final String ACTION_EDIT = "action_edit";

    private Spinner spinnerType;

    ArrayAdapter<String> adapter;
    ArrayList<String> tasks;

    private EditText etTitle;
    private EditText etDesc;

    private Uri note;

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
            note = getContentResolver().insert(NotepadContract.NoteEntry.CONTENT_URI, initialValues);
        }else if(intent.getAction().equals(ACTION_EDIT)){
            note = intent.getData();
        }

        etTitle = (EditText)findViewById(R.id.et_title);
        etDesc = (EditText)findViewById(R.id.et_description);

        Cursor cursor = getContentResolver().query(note, null, null, null, null);
        // This has the potential to fail, but since we're either pulling a note or creating
        // a new one, we always have an entry from the db
        cursor.moveToFirst();

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

        ListView listView = (ListView)findViewById(R.id.listView_task);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.add(etDesc.getText().toString());
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
        adapter.notifyDataSetChanged();
    }
}
