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
    boolean idleState;
    boolean mActivityResumed;
    boolean mPrepared;
    int currentSong;
    int currentPosition;

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
        currentPosition = 0;
        currentSong = 0;
        idleState = false;

        Log.i(TAG, "onCreate - Service");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand - Service");

        onPlay();


        return Service.START_STICKY;
    }



    // --[ UI CONTROL METHODS ---------------------------

    protected void onForward(){
        Log.i(TAG, "STATE CHECK - onForward");

        if(currentSong < 2 && currentSong <= 0){
            resetState();
            currentSong++;
            onResume();
        }
    }

    protected void onPlay() {
        Log.i(TAG, "STATE CHECK - onPlay");


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

            // grab data source for media player to play
            try {
                mediaPlayer.setDataSource(this, Uri.parse(playlist[currentSong].getFile()));
                onResume();
            }

            catch(IOException e) {
                Log.e(TAG, "MEDIA ERROR");
                e.printStackTrace();

                mediaPlayer.release();
                mediaPlayer = null;
            }


        }
        else if (mediaPlayer != null && idleState == false){
            if(mediaPlayer.isPlaying()){
                onPause();
            }
            else if (!mediaPlayer.isPlaying()){
                onResume();
            }
        }

        // grab data source for media player to resume
        else if (mediaPlayer != null && idleState == true){
            try {
                mediaPlayer.setDataSource(this, Uri.parse(playlist[currentSong].getFile()));
                idleState = false;
                onResume();
            }

            catch(IOException e) {
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

        // state conditional - not null &
        if(mediaPlayer != null && !mPrepared) {
            mediaPlayer.prepareAsync();
        }

        // if player exists and is prepared
        else if(mediaPlayer != null && mPrepared) {
            mediaPlayer.seekTo(currentPosition);
            mediaPlayer.start();
        }
    }

    protected void onPause() {
        Log.i(TAG, "STATE CHECK - onPause");

        mActivityResumed = false;

        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // set mediaplayer to pause.
            currentPosition = mediaPlayer.getCurrentPosition(); // grab current position of song
            stopForeground(true); // only show notification when song is playing
        }
    }

    protected void onStop() {
        Log.i(TAG, "STATE CHECK - onPause");


        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop(); // stop media from playing
            resetState();
            stopForeground(true); // only show notification when song is playing

        }
    }

    protected void onBack(){
        Log.i(TAG, "STATE CHECK - onBack");

        if(currentSong > 0 && currentSong <= 2){
            resetState(); // place mediaplayer into idle & reset song position
            currentSong--; // move backwards 1 position in array
            onResume(); // resume media playback
        }
    }

    protected void resetState() {
        if(mediaPlayer != null) {

            currentPosition = 0; // set current song position to 0
            mediaPlayer.reset(); // reset mediaplayer
            mPrepared = false; // boolean for prepared state
            idleState = true; // identifier for idle state


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
            mediaPlayer.seekTo(currentPosition);
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
