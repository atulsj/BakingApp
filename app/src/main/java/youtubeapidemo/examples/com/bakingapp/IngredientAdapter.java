package youtubeapidemo.examples.com.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {
    private Context mContext;
    private ArrayList<String> mIngredients;

    public IngredientAdapter(Context context, ArrayList<String> ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IngredientHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.ingredient_item, parent, false));
    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {
        String item = mIngredients.get(position);
        holder.no_column.setText(position + 1 + ".");
        holder.recipe_ingredient.setText(item.substring(0, 1).toUpperCase() + item.substring(1));

    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public void changeData(ArrayList<String> arrayList) {
        mIngredients = arrayList;
        notifyDataSetChanged();
    }

    public class IngredientHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_ingredient)
        TextView recipe_ingredient;
        @BindView(R.id.no_column)
        TextView no_column;

        public IngredientHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
