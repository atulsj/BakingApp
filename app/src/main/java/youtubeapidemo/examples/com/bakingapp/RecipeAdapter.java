package youtubeapidemo.examples.com.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private ArrayList<Recipes> mRecipes;
    private ListItemClickListener mListItemClickListener;
    private Context mContext;


    RecipeAdapter(Context context, ArrayList<Recipes> arrayList) {
        mListItemClickListener = (ListItemClickListener) context;
        mRecipes = arrayList;
        mContext = context;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.title.setText(mRecipes.get(position).getDish_Name());
        int serving=mRecipes.get(position).getServings();
        holder.servings.setText( String.valueOf(serving ).concat(" Servings"));
        String img_url=mRecipes.get(position).getImageUrl();
        if(img_url!=null && !img_url.isEmpty())
            Picasso.with(mContext).load(img_url).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    void changeData(ArrayList<Recipes> arrayList) {
        mRecipes = arrayList;
        notifyDataSetChanged();
    }


    interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex,String nameOfActivity);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.serving)
        TextView servings;
        @BindView(R.id.image)
        ImageView image;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            String name=mRecipes.get(position).getDish_Name();
            mListItemClickListener.onListItemClick(position,name);
        }
    }
}
