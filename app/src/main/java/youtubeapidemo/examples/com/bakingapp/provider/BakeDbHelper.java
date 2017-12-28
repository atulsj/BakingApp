package youtubeapidemo.examples.com.bakingapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import youtubeapidemo.examples.com.bakingapp.provider.BakeContract.BakeEntry;

/**
 * class for creating SQLiteOpenHelper.
 */

class BakeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipeDb.db";
    private static final int VERSION = 6;

    BakeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BakeEntry.TABLE_NAME + " (" +
                BakeEntry.RECIPE_INGREDIENTS + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > 1) {
            db.execSQL("DROP TABLE IF EXISTS " + BakeEntry.TABLE_NAME);
            onCreate(db);
        }
    }
}
