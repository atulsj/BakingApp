package youtubeapidemo.examples.com.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;


public class BakingItemsService extends IntentService {

public static final String WIDGET_UPDATE_ACTION= "widget_update_action";
    public BakingItemsService() {
        super("BakingItemsService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (WIDGET_UPDATE_ACTION.equals(action)) {
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
        intent.setAction(WIDGET_UPDATE_ACTION);
        context.startService(intent);
    }
}
