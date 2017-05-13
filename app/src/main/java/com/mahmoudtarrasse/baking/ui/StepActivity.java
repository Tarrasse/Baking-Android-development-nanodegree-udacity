package com.mahmoudtarrasse.baking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.Utility;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int step = intent.getIntExtra(Utility.EXTRA_STEP_ID, -1);
        int recipe = intent.getIntExtra(Utility.EXTRA_RECIPE_ID, -1);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.steps_fragment_palace_holder, createStepFragment(recipe, step))
                .commit();
    }

    private StepFragment createStepFragment(int recipe, int step){
        Bundle args = new Bundle();
        args.putInt(Utility.ARG_RECIPE_ID, recipe);
        args.putInt(Utility.ARG_STEP_ID, step);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
