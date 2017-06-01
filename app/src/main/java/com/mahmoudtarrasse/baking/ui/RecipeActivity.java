package com.mahmoudtarrasse.baking.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.Utility;
import com.mahmoudtarrasse.baking.widget.RecipeWidgetProvider;

import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity {
    private boolean twoBane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recipes_activity_toolbar);
        toolbar.setTitle(getString(R.string.recipe_activity_title));
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(this.getColor(R.color.colorPrimaryDark));
        }else{
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        final int recipeId = getIntent().getIntExtra(Utility.EXTRA_RECIPE_ID, -1);


        if (findViewById(R.id.steps_fragment_palace_holder) != null){
            twoBane = true;

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.steps_fragment_palace_holder, createStepFragment(recipeId, 0))
                    .commit();

            StepsFragment.OnclickListener listener = new StepsFragment.OnclickListener() {
                @Override
                public void onclick(int position) {
                    if (twoBane)
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.steps_fragment_palace_holder, createStepFragment(recipeId, position))
                                .commit();
                    else{
                        Intent intent = new Intent(RecipeActivity.this, StepActivity.class);
                        intent.putExtra(Utility.EXTRA_STEP_ID, position);
                        intent.putExtra(Utility.EXTRA_RECIPE_ID, position);
                        startActivity(intent);
                    }

                }
            };

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.ingredients_Fragment_placeHolder, createFragmentSteps(recipeId, listener))
                    .commit();
        }else{
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.ingredients_Fragment_placeHolder, createFragmentSteps(recipeId))
                    .commit();
        }

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putInt(Utility.WIDGET_RECIPE_ID_PREF, recipeId)
                .apply();
        updateWidgets(getApplicationContext());
    }

    private StepsFragment createFragmentSteps(int id, StepsFragment.OnclickListener listener){
        Timber.d(String.valueOf(id));
        StepsFragment ingredientsFragment = new StepsFragment();
        ingredientsFragment.setOnClickLIstner(listener);
        Bundle args = new Bundle();
        args.putInt(Utility.ARG_RECIPE_ID, id);
        ingredientsFragment.setArguments(args);
        return ingredientsFragment;
    }

    private StepsFragment createFragmentSteps(int id){
        Timber.d(String.valueOf(id));
        StepsFragment ingredientsFragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putInt(Utility.ARG_RECIPE_ID, id);
        ingredientsFragment.setArguments(args);
        return ingredientsFragment;
    }


    private StepFragment createStepFragment(int recipe, int step){
        Bundle args = new Bundle();
        args.putInt(Utility.ARG_RECIPE_ID, recipe);
        args.putInt(Utility.ARG_STEP_ID, step);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static void updateWidgets(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), RecipeWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            widgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

}
