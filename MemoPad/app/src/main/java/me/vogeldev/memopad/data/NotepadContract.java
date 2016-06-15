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
    public static final String PATH_TASK = "tasks";

    public static final class NoteEntry implements BaseColumns {
        public static final Uri NOTE_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTE).build();
        public static final Uri TASK_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();

        public static final String NOTE_CONTENT_TYPE = "vnd.android.cursor.dir/" + AUTHORITY + "/" + PATH_NOTE;
        public static final String NOTE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + AUTHORITY + "/#" + PATH_NOTE;

        public static final String TABLE_NAME_NOTE = "note";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";

        public static final String TASK_CONTENT_TYPE = "vnd.android.cursor.dir/" + AUTHORITY + "/" + PATH_TASK;
        public static final String TASK_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + AUTHORITY + "/#" + PATH_TASK;

        public static final String TABLE_NAME_TASKS = "task";

        public static final String COLUMN_NOTE_ID = "note_id";
        public static final String COLUMN_TASK = "task";
    }
}
