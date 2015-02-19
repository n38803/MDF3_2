package fullsail.com.mdf3_w3.widgetclasses;

/**
 * Created by shaunthompson on 2/17/15.
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import fullsail.com.mdf3_w3.AddActivity;
import fullsail.com.mdf3_w3.DetailsActivity;
import fullsail.com.mdf3_w3.R;
import fullsail.com.mdf3_w3.dataclass.NewsArticle;

    //          TASK                                                                    RESULT
    // TODO     Fix PendingIntent for Detail to return to widget (homescreen)           COMPLETED
    // TODO     Re-implement add activity from widget                                   COMPLETED
    // TODO     Ensure pendingintent for add returns to widget                          COMPLETED
    // TODO     Implement manual or auto refresh                                        ----------



public class CollectionWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_VIEW_DETAILS = "fullsail.com.mdf3_w3.ACTION_VIEW_DETAILS";
    public static final String ACTION_ADD_ARTICLE = "fullsail.com.mdf3_w3.ACTION_ADD_ARTICLE";
    public static final String EXTRA_ITEM = "fullsail.com.CollectionWidgetProvider.EXTRA_ITEM";

    private static final int REQUEST_NOTIFY_LAUNCH = 0x02001;

    RemoteViews rView;

    public final String TAG = "WIDGET PROVIDER";

    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent.getAction().equals(ACTION_VIEW_DETAILS)) {
            NewsArticle article = (NewsArticle)intent.getSerializableExtra(EXTRA_ITEM);
            if(article != null) {
                Intent details = new Intent(context, DetailsActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                details.putExtra(DetailsActivity.EXTRA_ITEM, article);
                //details.putExtra("Detail", "From_Widget");

                //details.putExtra("APP", false);
                context.startActivity(details);

                Log.i(TAG, "Pending Intent launched from onReceive(): DETAIL ACTIVITY");
            }
        }

        else if(intent.getAction().equals(ACTION_ADD_ARTICLE)) {

            Intent publish = new Intent(context, AddActivity.class);
            publish.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            publish.putExtra("Add", "From_Widget");
            context.startActivity(publish);

            Log.i(TAG, "Pending Intent launched from onReceive(): ADD ACTIVITY");




        }




        super.onReceive(context, intent);
        // super.onUpdate(mContext, AppWidgetManager.getInstance(mContext), );
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for(int i = 0; i < appWidgetIds.length; i++) {

            Log.e(TAG, "onUpdate() Launched");

            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, CollectionWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            rView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            rView.setRemoteAdapter(R.id.article_list, intent);
            rView.setRemoteAdapter(R.id.widgetAdd, intent);
            rView.setEmptyView(R.id.article_list, R.id.empty);



            Intent detailIntent = new Intent(ACTION_VIEW_DETAILS);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rView.setPendingIntentTemplate(R.id.article_list, pIntent);

            Intent addIntent = new Intent(ACTION_ADD_ARTICLE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_NOTIFY_LAUNCH, addIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rView.setOnClickPendingIntent(R.id.widgetAdd, pendingIntent );



            //AppWidgetManager.getInstance(context).updateAppWidget(widgetId, rView);

            // MAIN ONE appWidgetManager.updateAppWidget(widgetId, rView);

            //appWidgetManager.partiallyUpdateAppWidget(widgetId, rView);


            //ComponentName component=new ComponentName(context,CollectionWidgetService.class);
            //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.layout.widget_layout);
            //appWidgetManager.updateAppWidget(component, rView);


            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.article_list);
            appWidgetManager.updateAppWidget(widgetId, rView);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }








}
