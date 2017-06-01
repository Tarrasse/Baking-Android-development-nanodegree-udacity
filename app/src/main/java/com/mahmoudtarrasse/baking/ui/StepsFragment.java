package com.mahmoudtarrasse.baking.ui;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.Utility;
import com.mahmoudtarrasse.baking.data.RecipesContract;
import com.mahmoudtarrasse.baking.data.RecipesContract.*;

import org.json.JSONArray;
import org.json.JSONException;

import com.mahmoudtarrasse.baking.modules.*;
import timber.log.Timber;

public class StepsFragment extends Fragment {

    private static final String LIST_STATE_KEY = "list_state";
    private Parcelable mListState;
    private RecyclerView.LayoutManager mLayoutManager;


    private RecyclerView stepsRecyclerView;
    private LinearLayout ingredientsListLayout;
    private StepsAdapter adapter;

    private OnclickListener listener;



    public StepsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final int id = getArguments().getInt(Utility.ARG_RECIPE_ID);
        getLoaderManager().initLoader(id, null, loaderCallbacks);

        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        stepsRecyclerView = (RecyclerView)rootView.findViewById(R.id.steps_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        stepsRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new StepsAdapter(getActivity(), null);
        adapter.setOnClickListner(new StepsAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Timber.d("item: " + position + " clicked");
                if (listener != null){
                    listener.onclick(position);
                }else{
                    Intent intent = new Intent(getActivity(), StepActivity.class);
                    intent.putExtra(Utility.EXTRA_STEP_ID, position);
                    intent.putExtra(Utility.EXTRA_RECIPE_ID, id);
                    startActivity(intent);
                }

            }
        });
        stepsRecyclerView.setAdapter(adapter);
        ingredientsListLayout = (LinearLayout) rootView.findViewById(R.id.ingredients_list_linear_layout);
        return rootView;
    }

    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity() ,RecipesContract.RecipeTable.buildRecipeId(id),
                    null, null,
                    null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            data.moveToFirst();
            ingredientsListLayout.removeAllViews();
            String ingredientsJson = data.getString(data.getColumnIndex(RecipeTable.INGREDIENTS_JSON));
            String stepsJson = data.getString(data.getColumnIndex(RecipeTable.STEPS_JSON));
            Gson gson = new Gson();
            try {
                adapter.switchData(new JSONArray(stepsJson));
                JSONArray ingredientsList = new JSONArray(ingredientsJson);
                for (int i = 0 ; i< ingredientsList.length(); i++){
                    Ingredient ingredient = gson.fromJson(ingredientsList.get(i).toString(), Ingredient.class);
                    TextView temp = new TextView(getActivity());
                    temp.setText(ingredient.getIngredient());
                    ingredientsListLayout.addView(temp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Timber.d(stepsJson);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };


    public void setOnClickLIstner(OnclickListener lIstner){
        this.listener = lIstner;
    }

    public static interface OnclickListener {
        public void  onclick(int position);
    }

    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if(savedInstanceState != null)
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }


}
