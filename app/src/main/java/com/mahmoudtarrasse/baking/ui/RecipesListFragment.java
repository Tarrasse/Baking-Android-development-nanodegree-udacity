package com.mahmoudtarrasse.baking.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.app.Fragment;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.data.RecipesContract;
import com.mahmoudtarrasse.baking.models.Recipe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;

/**
 *
 */
public class RecipesListFragment extends Fragment {
    private RecyclerView recipesRecyclerView;
    private RecipesAdapter adapter;

    private ArrayList<Recipe> recipes ;


    public RecipesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        recipesRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipes_list);
        adapter = new RecipesAdapter(getActivity(), recipes, new RecipesAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Timber.d(String.format("item %d is clicked", position));
            }
        });

        recipesRecyclerView.setAdapter(adapter);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            recipesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), calculateNoOfColumns(getActivity())));
        }else{
            recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        getLoaderManager().initLoader(1, null, loaderCallbacks);
        return rootView;
    }


    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(),
                    RecipesContract.RecipeTable.CONTENT_URI,
                    null, null,
                    null, null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            recipes = new ArrayList<>();
            if (null != data && !(data.getCount() < 1)){
                data.moveToFirst();
                while (! data.isAfterLast()){
                    String temp = data.getString(RecipesContract.RecipeFile.MATRIX_CURSOR_JSON_COLUMNS_INDEX);
                    Recipe recipe = new Recipe();
                    try {
                        JSONObject json = new JSONObject(temp);
                        recipe.setName(json.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    Timber.d(temp);
                    recipe = gson.fromJson(temp, Recipe.class);
                    recipes.add(recipe);
                    data.moveToNext();
                }
            }
            adapter.swapCursor(recipes);
            Timber.d(String.valueOf(data.getCount()));
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }


}
