package youtubeapidemo.examples.com.bakingapp;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivityFragment extends Fragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private static final String TAG = MainActivityFragment.class.getSimpleName();

    private RecipeAdapter recipeAdapter;
    private static final int VERTICAL_ITEM_SPACE = 29;
    private ArrayList<Recipes> arrayList;
    public static final int NO_OF_COLUMNS = 3;

    public static final String RECIPE_LIST = "Recipe list";

    public MainActivityFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
        ButterKnife.bind(this, rootView);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        updateNetwork();
        if (savedInstanceState == null) {
            makeJsonArrayRequest();

        } else if (savedInstanceState.containsKey(RECIPE_LIST)) {
            arrayList = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            recipeAdapter.changeData(arrayList);
            //MainActivity.mProgressBar.setVisibility(View.INVISIBLE);
        }


        //   recyclerView.scrollToPosition(DetailIngredientFragment.pos);
        return rootView;
    }

    public void updateNetwork() {
        arrayList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(recipeAdapter);
        Configuration configuration = getActivity().getResources().getConfiguration();
        /*int screenWidthDp = configuration.screenWidthDp; //The current width of the available screen space, in dp units, corresponding to screen width resource qualifier.
        */

        int smallestScreenWidthDp = configuration.smallestScreenWidthDp; //The smallest screen size an application will see in normal operation, corresponding to smallest screen width resource qualifier
        if (getResources().getBoolean(R.bool.is_mobile)) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);

        }
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
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
                                String image_link = null;
                                image_link = person.getString("image");
                                arrayList.add(new Recipes(id, dish_Name, servings, image_link));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "Error: " + e.getMessage());
                            updateNetwork();
                        } finally {
                            recyclerView.setVisibility(View.VISIBLE);
                            // MainActivity.mProgressBar.setVisibility(View.INVISIBLE);
                            recipeAdapter.changeData(arrayList);

                            //TODO: uncomment the below comment for testing please
                          /*   SimpleIdlingResource simpleIdlingResource =
                                    ((MainActivity) getActivity()).getIdlingResource();
                            if (simpleIdlingResource != null) {
                                simpleIdlingResource.setIdleState(true);
                            }*/
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(req);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST, arrayList);
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