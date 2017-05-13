package com.mahmoudtarrasse.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.ui.RecipeActivity;

/**
 * Created by mahmoud on 4/21/2017.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        int[] appids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(appids, R.id.recipes_widget_list_view);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, RecipeRemoteViewService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            rv.setRemoteAdapter(appWidgetId, R.id.recipes_widget_list_view, intent);

            Intent clickIntentTemplate = new Intent(context, RecipeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.recipes_widget_list_view, pendingIntent);

            rv.setEmptyView(R.id.recipes_widget_list_view, R.id.widget_empty_view);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}
