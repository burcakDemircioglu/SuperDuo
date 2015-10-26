package barqsoft.footballscores;

/**
 * Created by burcakdemircioglu on 24/10/15.
 */

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
    private Context context = null;
    private int appWidgetId;
    public static final int COL_HOME = 0;
    public static final int COL_AWAY = 1;
    public static final int COL_MATCHTIME = 2;
    public static final int COL_HOME_GOALS = 3;
    public static final int COL_AWAY_GOALS = 4;


    private final ContentResolver mContentResolver;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mContentResolver = context.getContentResolver();
        populateListItem();
    }

    private void populateListItem() {

        Uri uri= DatabaseContract.scores_table.buildScoreWithDate();
        Log.v("widgetUri", uri.toString());

        long dateInMillis= System.currentTimeMillis();
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        Cursor cursor = mContentResolver.query(
                DatabaseContract.scores_table.buildScoreWithDate(),
                new String[] { DatabaseContract.scores_table.HOME_COL,DatabaseContract.scores_table.AWAY_COL, DatabaseContract.scores_table.TIME_COL,
                        DatabaseContract.scores_table.HOME_GOALS_COL,DatabaseContract.scores_table.AWAY_GOALS_COL},
                "date" + " = ?",
                new String[] {dayFormat.format(dateInMillis)} ,
                null
        );

        if(cursor != null && cursor.getCount() > 0) {
                Log.e("widget", "Fixtures found: " + cursor.getCount());

            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                ListItem listItem = new ListItem();

                listItem.heading = cursor.getString(COL_HOME)+" - " + cursor.getString(COL_AWAY);
                if(cursor.getInt(COL_HOME_GOALS)>=0&&cursor.getInt(COL_AWAY_GOALS)>=0)
                    listItem.content = cursor.getString(COL_MATCHTIME)+" // "+ cursor.getString(COL_HOME_GOALS)+" - "+cursor.getString(COL_AWAY_GOALS);
                else
                    listItem.content = cursor.getString(2);
                listItemList.add(listItem);
                cursor.moveToNext();
            }
        }
    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.list_row);
        ListItem listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.heading, listItem.heading);
        remoteView.setTextViewText(R.id.content, listItem.content);

        return remoteView;
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

}