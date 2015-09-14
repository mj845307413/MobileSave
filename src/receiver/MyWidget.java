package receiver;

import service.UpdateWidgetService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		System.out.println("mj_onreceive");
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		System.out.println("mj_onupdate");
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, UpdateWidgetService.class);
		context.startService(intent);
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, UpdateWidgetService.class);
		context.stopService(intent);
		super.onDisabled(context);
	}

}
