package com.mahmoudtarrasse.baking.ui;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.models.Recipe;

import java.util.ArrayList;

/**
 * Created by mahmoud on 06/05/17.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder>{

    private ArrayList<Recipe> recipes = new ArrayList<>();
    private ItemClickListener listener;
    private Context mContext ;

    public RecipesAdapter(Context context, ArrayList<Recipe> recipes, ItemClickListener listener) {
        this.mContext = context;
        this.recipes = recipes;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(v, parent.indexOfChild(v));
            }
        });

        

        return  new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.mTextView.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        if (recipes == null){
            return 0;
        }
        return recipes.size();
    }

    public void swapCursor(ArrayList<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.recipes_title);
        }
    }

    public interface ItemClickListener {
        public void OnItemClick(View v, int position);
    }


}
