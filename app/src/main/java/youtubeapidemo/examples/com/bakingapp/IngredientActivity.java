package youtubeapidemo.examples.com.bakingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import youtubeapidemo.examples.com.bakingapp.provider.BakeContract;

import static youtubeapidemo.examples.com.bakingapp.provider.BakeContract.BakeEntry.CONTENT_URI;

public class IngredientActivity extends AppCompatActivity implements IngredientActivityFragment.OnIngredientClickListener {

    public static final String ARG = "Set Arguments";
    public static final String POSITION = "position";
    private int pos;
    private ArrayList<String> ingredients;
    public static final String ITEM_INGREDIENT="item_ingredient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        String activityName = "Baking Activity";

        if (intent != null) {
            if (intent.hasExtra(MainActivity.BUNDLE)) {
               ingredients=intent.getStringArrayListExtra(MainActivity.PARCELABLE_LIST);
                Bundle bundle = intent.getBundleExtra(MainActivity.BUNDLE);
                activityName = bundle.getString(MainActivity.INGREDIENT_ACTIVITY_NAME);
                pos = bundle.getInt(MainActivity.LIST_ITEM_CLICKED);
            }
        }

        if (actionBar != null) {
            actionBar.setTitle(activityName);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                getResources().getBoolean(R.bool.is_tablet)) {
            DescriptionActivityFragment descriptionActivityFragment = new DescriptionActivityFragment();
            Bundle args = new Bundle();
            args.putInt(ARG, pos);
            descriptionActivityFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.description_frame_container,
                    descriptionActivityFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_settings:
                Intent intent=new Intent(this,BakingItemsService.class);
                intent.setAction(BakingItemsService.WIDGET_UPDATE_ACTION);
                Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
                if (cursor!=null ) {
                    if (cursor.getCount() > 0) {
                        getContentResolver().delete(CONTENT_URI, null, null);
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                for(int i=0;i<ingredients.size();i++){
                    ContentValues contentValue = new ContentValues();

                    contentValue.put(BakeContract.BakeEntry.RECIPE_INGREDIENTS, ingredients.get(i));
                    getContentResolver().insert(CONTENT_URI, contentValue);
                }
               getBaseContext().startService(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onIngredientSelected() {
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra(POSITION, pos);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wid, menu);
        return true;
    }

}
