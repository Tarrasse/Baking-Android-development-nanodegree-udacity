package com.mahmoudtarrasse.baking.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.Utility;
import com.mahmoudtarrasse.baking.data.RecipesContract;
import com.mahmoudtarrasse.baking.modules.Ingredient;

import org.json.JSONArray;
import org.json.JSONException;

import timber.log.Timber;

/**
 * Created by mahmoud on 4/21/2017.
 */
public class RecipeRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new RecipeRemoteViewsFactory(this.getApplicationContext(), intent);

    }

    class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private Cursor mCursor;

        private JSONArray ingredients;
        private int recipeId ;
        private String recipeName;

        public RecipeRemoteViewsFactory(Context mContext, Intent intent) {
            this.mContext = mContext;

        }

        @Override
        public void onCreate() {
            recipeId = PreferenceManager.getDefaultSharedPreferences(mContext)
                    .getInt(Utility.WIDGET_RECIPE_ID_PREF, 1);
            mCursor = mContext.getContentResolver().query(RecipesContract.RecipeTable.buildRecipeId(recipeId),
                    null, null,
                    null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
                String ingredientsJson = mCursor.getString(
                        mCursor.getColumnIndex(RecipesContract.RecipeTable.INGREDIENTS_JSON)
                );
                String name = mCursor.getString(mCursor.getColumnIndex(RecipesContract.RecipeTable.NAME_COLUMN));
                recipeName = name;
                try {
                    ingredients = new JSONArray(ingredientsJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onDataSetChanged() {
            onCreate();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (null == ingredients) {
                return 0;
            } else {
                return ingredients.length();
            }
        }

        @Override
        public RemoteViews getViewAt(int i) {

            try {
                String ingredientJson = ingredients.get(i).toString();
                Gson gson = new Gson();
                Ingredient ingredient = gson.fromJson(ingredientJson, Ingredient.class);

                RemoteViews nameRemoteView = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
                nameRemoteView.setTextViewText(R.id.symbol, ingredient.getIngredient());

                Intent fillInIntent = new Intent();
                fillInIntent.putExtra(Utility.EXTRA_RECIPE_ID, recipeId);
                nameRemoteView.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return nameRemoteView;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
