package youtubeapidemo.examples.com.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class DescriptionActivity extends AppCompatActivity {

    public static final String ARG_DES ="description_argument" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
       /* Configuration configuration = getResources().getConfiguration();
        int smallestScreenWidthDp = configuration.smallestScreenWidthDp;*/
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                getResources().getBoolean(R.bool.is_tablet)
                ) {
            finish();
        }
        int pos = 0;
        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(IngredientActivity.POSITION))
                pos = intent.getIntExtra(IngredientActivity.POSITION, 0);
        }
        if(savedInstanceState==null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ||
                    getResources().getBoolean(R.bool.is_mobile)) {
                DescriptionActivityFragment descriptionActivityFragment = new DescriptionActivityFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_DES, pos);
                descriptionActivityFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().add(R.id.description_frame_container,
                        descriptionActivityFragment).commit();
            }
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
}
