package youtubeapidemo.examples.com.bakingapp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import youtubeapidemo.examples.com.bakingapp.provider.BakeContact.BakeEntry;

/**
 * Class to be Used for inserting ,querying and deleting database content.
 */

public class BakeListContentProvider extends ContentProvider {

    public static final int RECIPES = 100;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BakeContact.AUTHORITY, BakeContact.PATH_RECIPES, RECIPES);
        return uriMatcher;
    }

    private BakeDbHelper mBakeDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mBakeDbHelper = new BakeDbHelper(context);
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mBakeDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case RECIPES:
                long id = db.insert(BakeEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(BakeEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mBakeDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECIPES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(BakeEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mBakeDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case RECIPES:
                cursor = db.query(BakeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}