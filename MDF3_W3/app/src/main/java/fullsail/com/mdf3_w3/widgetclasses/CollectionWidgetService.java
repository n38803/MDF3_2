package fullsail.com.mdf3_w3.widgetclasses;

/**
 * Created by shaunthompson on 2/17/15.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import fullsail.com.mdf3_w3.dataclass.NewsArticle;


public class CollectionWidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.e("WIDGET SERVICE ", "GetViewFactory");
        return new CollectionWidgetViewFactory(getApplicationContext());
    }


}
