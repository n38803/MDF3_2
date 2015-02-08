package fullsail.com.mdf3w1;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by shaunthompson on 2/8/15.
 */
public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private static final int FOREGROUND_NOTIFICATION = 0x01001;

    final String TAG = "MediaPlayer";

    MediaPlayer mediaPlayer;
    boolean mActivityResumed;
    boolean mPrepared;
    int mAudioPosition;

    @Override
    public void onCreate() {
        super.onCreate();

        mPrepared = mActivityResumed = false;
        mAudioPosition = 0;

        Log.i(TAG, "onCreate - Service");


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand - Service");

        onStart();


        return Service.START_NOT_STICKY;
    }


    protected void onStart() {


        Log.i(TAG, "onStart");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.headphone);
        builder.setContentTitle("Playing Music");
        builder.setContentText("Blah Blah Song Playing");
        builder.setAutoCancel(false);
        builder.setOngoing(true);

        startForeground(FOREGROUND_NOTIFICATION, builder.build());



        if(mediaPlayer == null) {


            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);


            try {
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.aerials));
                onResume();

            } catch(IOException e) {
                Log.e(TAG, "--> PLAYER RELEASED <--");
                e.printStackTrace();

                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

    }




    protected void onResume() {

        Log.i(TAG, "onResume");

        mActivityResumed = true;
        if(mediaPlayer != null && !mPrepared) {
            mediaPlayer.prepareAsync();
        } else if(mediaPlayer != null && mPrepared) {
            mediaPlayer.seekTo(mAudioPosition);
            mediaPlayer.start();
        }
    }


    protected void onPause() {

        Log.i(TAG, "onPause");

        mActivityResumed = false;

        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    protected void onStop() {

        Log.e(TAG, "onStop");


        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mPrepared = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e(TAG, "onDestroy");

        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPrepared = true;

        if(mediaPlayer != null && mActivityResumed) {
            mediaPlayer.seekTo(mAudioPosition);
            mediaPlayer.start();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Don't allow binding.
        return null;
    }

}
