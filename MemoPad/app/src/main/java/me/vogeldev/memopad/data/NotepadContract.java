package me.vogeldev.memopad.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Vogel on 6/5/2016.
 */
public final class NotepadContract {
    private NotepadContract(){}

    public static final String AUTHORITY = "me.vogeldev.memopad.data.NotepadProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_NOTE = "notes";

    public static final class NoteEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTE).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + AUTHORITY + "/" + PATH_NOTE;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + AUTHORITY + "/" + PATH_NOTE;

        public static final String TABLE_NAME = "note";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}
