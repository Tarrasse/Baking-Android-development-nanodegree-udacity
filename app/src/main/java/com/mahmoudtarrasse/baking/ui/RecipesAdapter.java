package com.mahmoudtarrasse.baking.ui;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.data.RecipesContract;
import com.mahmoudtarrasse.baking.modules.Ingredients;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by mahmoud on 06/05/17.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder>{

    private Cursor recipes ;
    private ItemClickListener listener;
    private Context mContext ;

    public RecipesAdapter(Context context, Cursor recipes, ItemClickListener listener) {
        this.mContext = context;
        this.recipes = recipes;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        return  new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            Gson gson = new Gson();
            recipes.moveToPosition(position);
            String name = recipes.getString(recipes.getColumnIndex(RecipesContract.RecipeTable.NAME_COLUMN));
            String ingredientsJson = recipes.getString(recipes.getColumnIndex(RecipesContract.RecipeTable.INGREDIENTS_JSON));
            final int id = recipes.getInt(recipes.getColumnIndex(RecipesContract.RecipeTable.ID_COLUMN));
            JSONArray ingredientsArray = new JSONArray(ingredientsJson);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i<ingredientsArray.length(); i++){
                Ingredients ingredient = gson.fromJson(ingredientsArray.get(i).toString(), Ingredients.class);
                builder.append(ingredient.getIngredient() + "\n");
            }
            holder.mTitleTextView.setText(name);
            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(v, position, id);
                }
            });
            holder.mIngredientsTextView.setText(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (recipes == null){
            return 0;
        }
        return recipes.getCount();
    }

    public void swapCursor(Cursor recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTitleTextView;
        public TextView mIngredientsTextView;
        public ViewHolder(View v) {
            super(v);
            mTitleTextView = (TextView) v.findViewById(R.id.recipes_title);
            mCardView = (CardView) v.findViewById(R.id.recipes_card);
            mIngredientsTextView = (TextView) v.findViewById(R.id.ingredients_textView);
        }
    }

    public interface ItemClickListener {
        public void OnItemClick(View v, int position, int id);
    }


}
