package barqsoft.footballscores.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.ListProvider;

/**
 * Created by burcakdemircioglu on 24/10/15.
 */

public class WidgetService extends RemoteViewsService {
	/*
	 * So pretty simple just defining the Adapter of the listview
	 * here Adapter is ListProvider
	 * */

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext(), intent));
    }

}