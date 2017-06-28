package com.mahmoudtarrasse.baking.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.sync.RecipeSyncAdapter;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecipeSyncAdapter.initializeSyncAdapter(this);
        Timber.plant(new Timber.DebugTree());

        RecipeSyncAdapter.initializeSyncAdapter(this);
        RecipeSyncAdapter.syncImmediately(this);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.recipes_list_place_holder, new RecipesListFragment())
                .commit();

    }
}
