package fullsail.com.mdf3w1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by shaunthompson on 2/8/15.
 */
public class PlayerService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Don't allow binding.
        return null;
    }


}
