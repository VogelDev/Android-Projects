package me.vogeldev.memopad.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by Vogel on 6/5/2016.
 */
public class NotepadDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "notepad.db";

    public NotepadDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NOTE_TABLE = "CREATE TABLE " + NotepadContract.NoteEntry.TABLE_NAME_NOTE + "("
                + NotepadContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NotepadContract.NoteEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + NotepadContract.NoteEntry.COLUMN_DESCRIPTION + " TEXT"
                + ");";
        db.execSQL(SQL_CREATE_NOTE_TABLE);

        db.execSQL("INSERT INTO " + NotepadContract.NoteEntry.TABLE_NAME_NOTE
        + "(" + NotepadContract.NoteEntry.COLUMN_TITLE + ", " + NotepadContract.NoteEntry.COLUMN_DESCRIPTION + ") "
        + "VALUES('Test', 'Test');");

        final String SQL_CREATE_TASKS_TABLE = "CREATE TABLE " + NotepadContract.NoteEntry.TABLE_NAME_TASKS + "("
                + NotepadContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NotepadContract.NoteEntry.COLUMN_NOTE_ID + " INTEGER NOT NULL, "
                + NotepadContract.NoteEntry.COLUMN_TASK + " TEXT"
                + ");";
        db.execSQL(SQL_CREATE_TASKS_TABLE);

        db.execSQL("INSERT INTO " + NotepadContract.NoteEntry.TABLE_NAME_TASKS
                + "(" + NotepadContract.NoteEntry.COLUMN_NOTE_ID + ", " + NotepadContract.NoteEntry.COLUMN_TASK + ") "
                + "VALUES('1', 'Test');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Current upgrade pattern is to remove all existing data and upgrade
        // This will need to change in the future to allow user data to persist
        db.execSQL("DROP TABLE IF EXISTS " + NotepadContract.NoteEntry.TABLE_NAME_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + NotepadContract.NoteEntry.TABLE_NAME_TASKS);

        onCreate(db);
    }
}
