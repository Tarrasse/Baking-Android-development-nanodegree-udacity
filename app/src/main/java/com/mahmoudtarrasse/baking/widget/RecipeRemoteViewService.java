package com.mahmoudtarrasse.baking.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.Utility;
import com.mahmoudtarrasse.baking.data.RecipesContract;

import timber.log.Timber;

/**
 * Created by mahmoud on 4/21/2017.
 */
public class RecipeRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Timber.d("public class RecipeRemoteViewService extends RemoteViewsService {\n");
        return new StockRemoteViewsFactory(this.getApplicationContext(), intent);

    }

    class StockRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
        private final int mAppWidgetId;
        private Context mContext;
        private Cursor mCursor;

        public StockRemoteViewsFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            mCursor = mContext.getContentResolver().query(RecipesContract.RecipeTable.CONTENT_URI,
                    null, null,
                    null, null);
            if (mCursor != null){
                mCursor.moveToFirst();
                Timber.d("length = " + mCursor.getCount());
            }
        }

        @Override
        public void onDataSetChanged() {
            mCursor = mContext.getContentResolver().query(RecipesContract.RecipeTable.CONTENT_URI,
                    null, null,
                    null, null);
            if (mCursor != null){
                mCursor.moveToFirst();
                Timber.d("length = " + mCursor.getCount());
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mCursor == null){
                Timber.d("null cursor");
                return 0;
            }else {
                return mCursor.getCount();
            }
        }

        @Override
        public RemoteViews getViewAt(int i) {
            Timber.d("size = " + getCount());
            mCursor.moveToPosition(i);
            String name = mCursor.getString(mCursor.getColumnIndex(RecipesContract.RecipeTable.NAME_COLUMN));
            int id =mCursor.getInt(mCursor.getColumnIndex(RecipesContract.RecipeTable.ID_COLUMN));

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
            rv.setTextViewText(R.id.symbol, name);


            Intent fillInIntent = new Intent();
            Log.i("widget", id + " ");
            Timber.d(id + " ");
            fillInIntent.putExtra(Utility.EXTRA_RECIPE_ID, id);
            rv.setOnClickFillInIntent(R.id.widget_list_item,fillInIntent);
            return rv;
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
