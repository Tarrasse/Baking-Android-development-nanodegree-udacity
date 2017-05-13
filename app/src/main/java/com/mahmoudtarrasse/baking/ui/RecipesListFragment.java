package com.mahmoudtarrasse.baking.ui;

import android.content.Context;
import android.content.Intent;
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

import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.Utility;
import com.mahmoudtarrasse.baking.data.RecipesContract;
import com.mahmoudtarrasse.baking.modules.Recipe;

import java.util.ArrayList;

import timber.log.Timber;


public class RecipesListFragment extends Fragment {
    private RecyclerView recipesRecyclerView;
    private RecipesAdapter adapter;

    private ArrayList<Recipe> recipes ;


    public RecipesListFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        recipesRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipes_list);
        adapter = new RecipesAdapter(getActivity(), null, new RecipesAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(View v, int position, int id) {
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra(Utility.EXTRA_RECIPE_ID, id);
                startActivity(intent);
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
            data.moveToFirst();
            adapter.swapCursor(data);
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
