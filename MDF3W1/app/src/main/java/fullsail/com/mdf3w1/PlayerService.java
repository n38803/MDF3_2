package fullsail.com.mdf3w1;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
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
    int currentSong;

    // --[ SERVICE BINDER -------------------------------

    public class PlayerServiceBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {


        return new PlayerServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {



        return super.onUnbind(intent);
    }


    // --------------------------------------------------



    @Override
    public void onCreate() {
        super.onCreate();

        mPrepared = mActivityResumed = false;
        mAudioPosition = 0;
        currentSong = 0;

        Log.i(TAG, "onCreate - Service");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand - Service");

        onStart();


        return Service.START_STICKY;
    }



    // --[ UI CONTROL METHODS ---------------------------

    protected void onForward(){
        Log.i(TAG, "STATE CHECK - onForward");

        if(currentSong < 2 && currentSong <= 0){
            onStop();
            currentSong++;
            onResume();
        }
    }

    protected void onStart() {
        Log.i(TAG, "STATE CHECK - onStart");


        // Create custom objects & assign information
        PlayerClass song1 = new PlayerClass("Echoes of Aeons", "New Life", ("android.resource://" + getPackageName() + "/" + R.raw.newlife));
        PlayerClass song2 = new PlayerClass("Echoes of Aeons", "Serenity", ("android.resource://" + getPackageName() + "/" + R.raw.serenity));
        PlayerClass song3 = new PlayerClass("Echoes of Aeons", "Aerials", ("android.resource://" + getPackageName() + "/" + R.raw.aerials));

        // Store objects into array
        PlayerClass playlist [] = {song1, song2, song3};

        // Build foreground notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.headphone);

        // Set notification text dynamically based on array position
        builder.setContentTitle(playlist[currentSong].getArtist());
        builder.setContentText(playlist[currentSong].getTitle());

        builder.setAutoCancel(false);
        builder.setOngoing(true);
        startForeground(FOREGROUND_NOTIFICATION, builder.build());

        // verify state/existence of mediaplayer via conditional
        if(mediaPlayer == null) {

            // create media player
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);

            // assign current song to position 0
            currentSong = 0;


            // TODO - MAKE ASSIGNMENT OF SONG DYNAMIC



            // grab data source for media player to play
            try {
                mediaPlayer.setDataSource(this, Uri.parse(playlist[currentSong].getFile()));
                onResume();

            } catch(IOException e) {
                Log.e(TAG, "MEDIA ERROR");
                e.printStackTrace();

                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

    }

    protected void onResume() {
        Log.i(TAG, "STATE CHECK - onResume");

        mActivityResumed = true;
        if(mediaPlayer != null && !mPrepared) {
            mediaPlayer.prepareAsync();
        } else if(mediaPlayer != null && mPrepared) {
            mediaPlayer.seekTo(mAudioPosition);
            mediaPlayer.start();
        }
    }

    protected void onPause() {
        Log.i(TAG, "STATE CHECK - onPause");

        mActivityResumed = false;

        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    protected void onStop() {
        Log.i(TAG, "STATE CHECK - onPause");


        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mPrepared = false;
        }
    }

    protected void onBack(){
        Log.i(TAG, "STATE CHECK - onBack");

        if(currentSong > 0 && currentSong <= 2){
            onStop();
            currentSong--;
            onResume();
        }
    }


    // --------------------------------------------------




    // --[ STATE IDENTIFIERS ----------------------------
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

    // --------------------------------------------------






    // --[ FRAGMENT COMMUNICATION METHODS ---------------
    public String getArtist(){

        String artist = "";

        return artist;
    }

    public String getTitle(){

        String title = "";

        return title;
    }

    // --------------------------------------------------



}
