package com.slawski.quicklist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

public class TaskContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.slawski.quicklist.provider.TaskContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + DatabaseHandler.TABLE_TASKS);
    private DatabaseHandler dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHandler(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DatabaseHandler.TABLE_TASKS);
        String orderBy = DatabaseHandler.KEY_ID + " asc";
        Cursor cursor = qb.query(dbHelper.getReadableDatabase(),
                new String[] { DatabaseHandler.KEY_ID, DatabaseHandler.KEY_TASK_DESCRIPTION, DatabaseHandler.KEY_VOTES // more fields can go here
                }, null,
                null, null, null, orderBy);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null; //TODO something here?
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
