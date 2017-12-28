package youtubeapidemo.examples.com.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import youtubeapidemo.examples.com.bakingapp.IdlingResource.SimpleIdlingResource;

/*Main Activity contains fragment MainActivityFragment*/

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {


    //public static ProgressBar mProgressBar;
    private int previosPosition=0;
    public static final String LIST_KEY = "clicked item position";
    public static final String BUNDLE = "bundle";
    public static final String INGREDIENT_ACTIVITY_NAME = "name";
    @Nullable
    public SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   mProgressBar= (ProgressBar) findViewById(R.id.prog_main);

        if(previosPosition==0) {
         //   mProgressBar.setVisibility(View.VISIBLE);
            previosPosition++;
        }
       /* if (savedInstanceState != null && !savedInstanceState.isEmpty() && savedInstanceState.containsKey("CLICKED_POSITION")) {
            previosPosition = savedInstanceState.getInt("CLICKED_POSITION", 0);
        }*/
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
            mIdlingResource.setIdleState(false);
        }

    }

    @Override
    public void onListItemClick(int clickedItemIndex,String name) {
            previosPosition = clickedItemIndex;
            Intent intent = new Intent(MainActivity.this, IngredientActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString(INGREDIENT_ACTIVITY_NAME,name);
            bundle.putInt(LIST_KEY,clickedItemIndex);
            intent.putExtra(BUNDLE, bundle);
            intent.setAction("DETAILS");
            startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("CLICKED_POSITION", previosPosition);
        super.onSaveInstanceState(outState);
    }


}
