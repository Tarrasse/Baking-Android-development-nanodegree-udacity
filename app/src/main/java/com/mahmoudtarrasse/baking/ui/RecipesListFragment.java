package com.mahmoudtarrasse.baking.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.app.Fragment;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.TextView;

import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.Utility;
import com.mahmoudtarrasse.baking.data.RecipesContract;
import com.mahmoudtarrasse.baking.modules.Recipe;
import com.mahmoudtarrasse.baking.sync.RecipeSyncAdapter;

import java.util.ArrayList;

import timber.log.Timber;


public class RecipesListFragment extends Fragment {
    private static final String LIST_STATE_KEY = "list_state_1";
    private RecyclerView recipesRecyclerView;
    private RecipesAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Recipe> recipes ;
    private Parcelable mListState = null;

    private TextView emptyView;

    private int listPosition = -1;



    public RecipesListFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            listPosition = savedInstanceState.getInt(LIST_STATE_KEY);
            Timber.d("pos = " + String.valueOf(listPosition));
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        recipesRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipes_list);
        emptyView = (TextView) rootView.findViewById(R.id.recipe_list_empty_view);

        adapter = new RecipesAdapter(getActivity(), null, new RecipesAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(View v, int position, int id) {
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra(Utility.EXTRA_RECIPE_ID, id);
                startActivity(intent);
            }
        },emptyView
        );

        recipesRecyclerView.setAdapter(adapter);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mLayoutManager = new GridLayoutManager(getActivity(), calculateNoOfColumns(getActivity()));
        }else{
            mLayoutManager = new LinearLayoutManager(getActivity());
        }
        recipesRecyclerView.setLayoutManager(mLayoutManager);

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
            if (listPosition != -1)
                recipesRecyclerView.scrollToPosition(listPosition);
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

    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        int pos = ((LinearLayoutManager)recipesRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        state.putInt(LIST_STATE_KEY, pos);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
            recipesRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
            Timber.d("ok");
        }
    }

    public class ConnectionChangeReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive( Context context, Intent intent )
        {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE );
            if ( activeNetInfo != null ||  mobNetInfo != null)
            {
                RecipeSyncAdapter.syncImmediately(getActivity());
            }

        }
    }

}
