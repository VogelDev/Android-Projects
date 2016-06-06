package me.vogeldev.memopad.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Vogel on 6/5/2016.
 */
public class NotepadProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static  final int NOTE = 100;
    private static  final int NOTE_ID = 101;

    private NotepadDbHelper openHelper;

    private static final SQLiteQueryBuilder queryBuilder;

    static {
        queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(NotepadContract.NoteEntry.TABLE_NAME);
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NotepadContract.AUTHORITY;

        matcher.addURI(authority, NotepadContract.PATH_NOTE, NOTE);
        matcher.addURI(authority, NotepadContract.PATH_NOTE + "/#", NOTE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
