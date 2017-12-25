package youtubeapidemo.examples.com.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by 1515012 on 24-07-2017.
 */

public class BakingItemsService extends IntentService {

    public static final String ACTION_UPDATE_RECIPE_LIST_WIDGETS =
            "youtubeapidemo.examples.com.bakingapp.action.update_recipe_list_widgets";

    public BakingItemsService() {
        super("BakingItemsService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_LIST_WIDGETS.equals(action)) {
                handleActionUpdateRecipeListWidgets();
            }
        }
    }

    public void handleActionUpdateRecipeListWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.
                getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this,BakingWidgetProvider.class));
        BakingWidgetProvider.updateRecipeWidgets(this,
                appWidgetManager,appWidgetIds);
    }


    public static void startActionUpdateRecipeWidgets(Context context) {
        Intent intent = new Intent(context, BakingItemsService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_LIST_WIDGETS);
        context.startService(intent);
    }

}
