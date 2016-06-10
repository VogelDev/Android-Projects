package me.vogeldev.memopad;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import me.vogeldev.memopad.data.NotepadContract;

/**
 * Created by Vogel on 6/9/2016.
 */
public class NoteEditActivity extends AppCompatActivity {

    public static final String ACTION_NEW = "action_new";
    public static final String ACTION_EDIT = "action_edit";

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
}
