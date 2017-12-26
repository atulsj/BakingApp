package youtubeapidemo.examples.com.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class IngredientActivity extends AppCompatActivity implements IngredientActivityFragment.OnIngredientClickListener{

    public static final String ARG ="Set Arguments" ;
    public static final String POSITION ="position" ;
    // public static ProgressBar mProgressBar;
int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        String activityName = "Baking Activity";



        if (intent != null) {
            if (intent.hasExtra(MainActivity.BUNDLE)) {
                Bundle bundle = intent.getBundleExtra(MainActivity.BUNDLE);
                activityName = bundle.getString(MainActivity.INGREDIENT_ACTIVITY_NAME);
                pos = bundle.getInt(MainActivity.LIST_KEY);
            }
        }

        if (actionBar != null) {
            actionBar.setTitle(activityName);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
      /*  Configuration configuration = getResources().getConfiguration();
        int smallestScreenWidthDp = configuration.smallestScreenWidthDp;*/
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE &&
                getResources().getBoolean(R.bool.is_tablet)) {
            DescriptionActivityFragment descriptionActivityFragment = new DescriptionActivityFragment();
            Bundle args = new Bundle();
            args.putInt(ARG,pos);
            descriptionActivityFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.description_frame_container,
                    descriptionActivityFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onIngredientSelected() {
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra(POSITION,pos);
        startActivity(intent);
    }
}
