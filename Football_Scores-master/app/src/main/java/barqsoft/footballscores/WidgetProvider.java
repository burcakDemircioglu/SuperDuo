package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.WidgetService;

/**
 * Created by burcakdemircioglu on 21/10/15.
 */


public class WidgetProvider extends AppWidgetProvider {

    /*
     * called every 30 mins as specified on widgetinfo.xml
     * called on every phone reboot
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; ++i) {
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
        Intent svcIntent = new Intent(context, WidgetService.class);

        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget, svcIntent);

        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        return remoteViews;
    }

}