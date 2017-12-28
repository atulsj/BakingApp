package youtubeapidemo.examples.com.bakingapp;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import youtubeapidemo.examples.com.bakingapp.IdlingResource.SimpleIdlingResource;

/*Main Activity contains fragment MainActivityFragment*/

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    //    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private static final int VERTICAL_ITEM_SPACE = 29;
    private ArrayList<Recipes> arrayList;
    public static final int NO_OF_COLUMNS = 2;

    public static final String RECIPE_LIST = "Recipe list";

    public static final String LIST_ITEM_CLICKED = "clicked item position";
    public static final String BUNDLE = "bundle";
    public static final String INGREDIENT_ACTIVITY_NAME = "name";
    @Nullable
    public SimpleIdlingResource mIdlingResource;
    public static final String PARCELABLE_LIST = "parcelable_list";

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
        //for Testing Idling resources
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
            mIdlingResource.setIdleState(false);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        updateNetwork();

        if (savedInstanceState == null) {
            makeJsonArrayRequest();
        } else if (savedInstanceState.containsKey(RECIPE_LIST)) {
            arrayList = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            // Log.e("8989898",arrayList.get(0).getIngredients().size()+"");
            recipeAdapter.changeData(arrayList);
        }
    }

    public void updateNetwork() {
        if (getResources().getBoolean(R.bool.is_mobile)) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NO_OF_COLUMNS);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        arrayList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, arrayList);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new youtubeapidemo.examples.com.bakingapp.VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
    }


    private void makeJsonArrayRequest() {
        String mUrlBaking = "https://go.udacity.com/android-baking-app-json";
        JsonArrayRequest req = new JsonArrayRequest(mUrlBaking,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = (JSONObject) response.get(i);
                                String dish_Name = person.getString("name");
                                int id = person.getInt("id");
                                int servings = person.getInt("servings");
                                String image_link = person.getString("image");

                                JSONObject recipe = (JSONObject) response.get(i);
                                JSONArray ingredients = recipe.getJSONArray(
                                        BakingUtility.INGREDIENTS);
                                ArrayList<String> ingredientArrayList = new ArrayList<>();
                                for (int j = 0; j < ingredients.length(); j++) {
                                    JSONObject ingr = (JSONObject) ingredients.get(j);
                                    String ingredientsRequired = ingr.getString(BakingUtility.INGREDIENT)
                                            + " - " + ingr.getDouble(BakingUtility.QUALITY) + " " +
                                            ingr.getString(BakingUtility.MEASURE);
                                    ingredientArrayList.add(ingredientsRequired);
                                }
                                arrayList.add(new Recipes(id, dish_Name, servings, image_link,
                                        ingredientArrayList));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "Error: " + e.getMessage());
                            updateNetwork();
                        } finally {
                            recyclerView.setVisibility(View.VISIBLE);
                            recipeAdapter.changeData(arrayList);
                            SimpleIdlingResource simpleIdlingResource = getIdlingResource();
                            simpleIdlingResource.setIdleState(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(req);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST, arrayList);
    }


    @Override
    public void onListItemClick(int clickedItemIndex, String name) {
        Intent intent = new Intent(MainActivity.this, IngredientActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(INGREDIENT_ACTIVITY_NAME, name);
        bundle.putInt(LIST_ITEM_CLICKED, clickedItemIndex);
        intent.putExtra(BUNDLE, bundle);
        intent.putStringArrayListExtra(PARCELABLE_LIST, arrayList.get(clickedItemIndex).getIngredients());
        startActivity(intent);
    }

}

class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int verticalSpaceHeight;

    VerticalSpaceItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
