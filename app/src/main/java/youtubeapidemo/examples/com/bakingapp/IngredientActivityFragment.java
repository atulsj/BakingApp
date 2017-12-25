package youtubeapidemo.examples.com.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class IngredientActivityFragment extends Fragment {

    private static final String RESTORE_INGREDIENT_LIST = "restore_ingredient_list";
    private static final String ITEM_POSITION ="item_postion" ;
    public static final String WIDGIT_POSITION = "WIDGIT POSITION";
    public static final String TAG = IngredientActivityFragment.class.getSimpleName();
    public static final String DESCRIPTION_ARRAY_LIST = "list";
    private TextView ingredientHead;
    private View cookingButton;
    private CardView cardViewIngredients;
    private IngredientAdapter ingredientAdapter;
    private ArrayList<String> ingredientArrayList;
    public static int pos;
    public static ArrayList<Description> descriptionArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ingredient_activity, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        ingredientHead = (TextView) root.findViewById(R.id.ingredient_head);
        Configuration configuration = getActivity().getResources().getConfiguration();
        boolean orient=configuration.orientation ==
                Configuration.ORIENTATION_PORTRAIT;
        if ( orient || getResources().getBoolean(R.bool.is_mobile)) {
            cookingButton = root.findViewById(R.id.cook_custom_button);
            if(orient)
            cardViewIngredients = (CardView) root.findViewById(R.id.card_view_ingredients);
        }
        ingredientArrayList=new ArrayList<>();
        descriptionArrayList = new ArrayList<>();
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if ((intent.getAction()).equals(WIDGIT_POSITION)) {
                pos = intent.getIntExtra("WIDGIT_POSITION_CLICKED", 0);
            } else if (intent.hasExtra(MainActivity.BUNDLE)) {
                Bundle bundle = intent.getBundleExtra(MainActivity.BUNDLE);
                pos = bundle.getInt(MainActivity.LIST_KEY);
            }
        }
        if(savedInstanceState!=null){
            pos=savedInstanceState.getInt(ITEM_POSITION);
        }
        makeJsonArrayRequest();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientAdapter = new IngredientAdapter(getActivity(), ingredientArrayList);
        recyclerView.setAdapter(ingredientAdapter);
        return root;
    }


    private void makeJsonArrayRequest() {
        String mUrlBaking = BakingUtility.BAKING_URL;
        JsonArrayRequest req = new JsonArrayRequest(mUrlBaking,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String ingredientsRequirent;
                        try {
                            JSONObject recipe = (JSONObject) response.get(pos);
                            JSONArray ingredients = recipe.getJSONArray(BakingUtility.INGREDIENTS);
                            for (int j = 0; j < ingredients.length(); j++) {
                                JSONObject ingr = (JSONObject) ingredients.get(j);
                                ingredientsRequirent = ingr.getString(BakingUtility.INGREDIENT) + " - " +
                                        ingr.getDouble(BakingUtility.QUALITY) + " " + ingr.getString(BakingUtility.MEASURE);
                                ingredientArrayList.add(ingredientsRequirent);
                            }
                            /*Cooking button,card View  is not available in Landscape layout*/
                            JSONArray steps = recipe.getJSONArray(BakingUtility.STEPS);
                            for (int i = 0; i < steps.length(); i++) {
                                JSONObject step = (JSONObject) steps.get(i);
                                int id = step.getInt(BakingUtility.ID);
                                String shortDescription = step.getString(BakingUtility.SHORT_DESCRIPTION);
                                String describe = step.getString(BakingUtility.DESCRIPTION);
                                String videoURL = step.getString(BakingUtility.VIDEO_URL);
                                descriptionArrayList.add(new Description(id, shortDescription,
                                        describe, videoURL));
                            }
                            if (cookingButton != null ) {
                                cookingButton.setVisibility(View.VISIBLE);
                            }
                            if(cardViewIngredients!=null){
                                cardViewIngredients.setVisibility(View.VISIBLE);
                            }
                            if (cookingButton != null) {
                                cookingButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelableArrayList(DESCRIPTION_ARRAY_LIST,
                                                descriptionArrayList);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                            }
                            ingredientHead.setVisibility(View.VISIBLE);
                            ingredientAdapter.changeData(ingredientArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "Error: " + e.getMessage());
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
        outState.putStringArrayList(RESTORE_INGREDIENT_LIST, ingredientArrayList);
        outState.putInt(ITEM_POSITION,pos);
    }
}
