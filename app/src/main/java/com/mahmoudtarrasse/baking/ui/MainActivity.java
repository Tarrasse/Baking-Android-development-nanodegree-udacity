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

        getFragmentManager().beginTransaction().replace(R.id.recipes_list_place_holder, new RecipesListFragment()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public class task extends AsyncTask<Object, Object, String> {
//
//        @Override
//        protected String doInBackground(Object... params) {
//            String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/5907926b_baking/baking.json";
//            OkHttpClient client = new OkHttpClient();
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            okhttp3.Response response = null;
//            try {
//                response = client.newCall(request).execute();
//                ContentValues values = new ContentValues();
//                values.put(RecipeContentProvider.JSON_DATA, response.body().string());
//                getContentResolver().bulkInsert(RecipesContract.RecipeTable.CONTENT_URI, new ContentValues[]{values});
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//    }
}
