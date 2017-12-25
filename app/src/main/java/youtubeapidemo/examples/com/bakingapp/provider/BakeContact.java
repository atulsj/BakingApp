package youtubeapidemo.examples.com.bakingapp.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The class is defining some fields use for creating database table.
 */

public final class BakeContact {
    public static final String AUTHORITY = "youtubeapidemo.examples.com.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_RECIPES = "food_to_cook";

    public static final class BakeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();
        public static final String TABLE_NAME = "food_to_cook";
        public static final String COLUMN_NAME = "recipe_name";
    }
}
