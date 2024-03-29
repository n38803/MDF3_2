package fullsail.com.mdf3w1;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.support.v4.app.NotificationCompat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import java.io.IOException;
import java.util.Random;

/*
 * Created by shaunthompson on 2/8/15.
*/


public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static final int FOREGROUND_NOTIFICATION = 0x01001;
    private static final int REQUEST_NOTIFY_LAUNCH = 0x02001;

    final String TAG = "MediaPlayer";

    public String artist;
    public String title;
    public int art;


    MediaPlayer mediaPlayer;
    boolean idleState;
    boolean mActivityResumed;
    boolean mPrepared;
    boolean fromPending;
    boolean isLooping = false;
    int currentSong;
    int currentPosition;
    int currentSongLength;



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
        idleState = false;
        fromPending = false;

        Random random = new Random();
        currentSong = random.nextInt(2);



        Log.i(TAG, "onCreate - Service // Randomized Song Selection: " + currentSong);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand - Service");

        onPlay();


        return Service.START_STICKY;
    }



    // --[ UI CONTROL METHODS ---------------------------

    protected void onBack(){
        Log.i(TAG, "STATE CHECK - onBack");

        if(currentSong != 0 && currentSong <= 2 && isLooping == false){
            resetState(); // place mediaplayer into idle & reset song position
            currentSong--; // move backwards 1 position in array
            onPlay(); // resume media playback
        }
        else if(isLooping == true) // place player into idle, reset position, & play from start
        {
            resetState();
            onPlay();
        }
        else if (currentSong == 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Action Aborted: Start of Playlist Reached.",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    protected void onPlay() {
        Log.i(TAG, "STATE CHECK - onPlay");


        // Create custom objects & assign information
        PlayerClass song1 = new PlayerClass("Echoes of Aeons", "New Life", ("android.resource://" + getPackageName() + "/" + R.raw.newlife), R.drawable.gotmphoto);
        PlayerClass song2 = new PlayerClass("Echoes of Aeons", "Serenity", ("android.resource://" + getPackageName() + "/" + R.raw.serenity), R.drawable.gotmphoto2);
        PlayerClass song3 = new PlayerClass("Echoes of Aeons", "Aerials", ("android.resource://" + getPackageName() + "/" + R.raw.aerials), R.drawable.gotmphoto3);

        // Store objects into array
        PlayerClass playlist [] = {song1, song2, song3};

        // store existing song information into global variable
        artist  = (playlist[currentSong].getArtist());
        title   = (playlist[currentSong].getTitle());
        art     = (playlist[currentSong].getArt());

        // pending intent
        Intent songIntent = new Intent(this, MainActivity.class);
        songIntent.setAction(Intent.ACTION_MAIN);
        songIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pIntent =
                PendingIntent.getActivity(this, REQUEST_NOTIFY_LAUNCH, songIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        // Build foreground notification w/expanded notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.headphone);
        builder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(getResources(), art))
                .setSummaryText(title)
                .setBigContentTitle(artist));


        //builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), art));

        // Set notification text dynamically based on array position
        builder.setContentTitle(playlist[currentSong].getArtist());
        builder.setContentText(playlist[currentSong].getTitle());

        // set notification paramaters w/intent
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setContentIntent(pIntent);
        startForeground(FOREGROUND_NOTIFICATION, builder.build());

        // verify state/existence of mediaplayer via conditional
        if(mediaPlayer == null) {

            // create media player
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);

            // assign current song to position 0 **NOTE Switched this to randomized song selection onCreate
            //currentSong = 0;

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

        // TODO - Modify logic to detect pending intent versus pause button being clicked

        //this conditional is causing song to pause when returning from pending intent
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

        // state conditional - player exists & not prepared
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
            // TODO unbind service

        }
    }

    protected void onForward(){
        Log.i(TAG, "STATE CHECK - onForward");

        if(currentSong != 2 && currentSong >= 0 && isLooping == false){
            resetState();
            currentSong++;
            onPlay();
        }
        else if(isLooping == true) // place player into idle, reset position, & play from start
        {
            resetState();
            onPlay();
        }
        else if (currentSong == 2){
            Toast toast = Toast.makeText(getApplicationContext(), "Action Aborted: End of Playlist Reached.",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
            toast.show();
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

    @Override
    public void onCompletion(MediaPlayer mp) {

        onForward();

    }

    public boolean isNotNull(){
        if (mediaPlayer != null)
        {
            return true;

        }
        else
        {
            return false;
        }

    }

    // --------------------------------------------------






    // --[ FRAGMENT COMMUNICATION METHODS ---------------

    public String getArtist(){



        return artist;
    }

    public String getTitle(){

        return title;
    }

    public int getArt(){

        return art;
    }

    public Boolean getLooping(){
        return isLooping;
    }

    public int getSongLength() {

        currentSongLength = mediaPlayer.getDuration();
        return currentSongLength;
    }

    public int getSongPosition(){

        if (mediaPlayer.isPlaying()){
            currentPosition = mediaPlayer.getCurrentPosition();
            Log.i("From getSongPosition Method", "Position: " + currentPosition);
        }
        else if (!mediaPlayer.isPlaying() && idleState == false)
        {
            currentPosition = 0;
        }

        return currentPosition;
    }

    public void setSongPosition(int newPosition){
        if(mediaPlayer.isPlaying())
        {
            //onReset();
            currentPosition = newPosition;
            mediaPlayer.seekTo(currentPosition);
            Log.i("From Seek Change", "Current Position: " + newPosition);
        }
        else if(!mediaPlayer.isPlaying())
        {
            currentPosition = newPosition;
            onPlay();
            Log.i("From Seek Change", "Current Position: " + currentPosition);
        }


    }

    public void setLooping(boolean checkLooping){
        isLooping = checkLooping;
        Log.i("LOOP", "Status: " + checkLooping);
    }



    // --------------------------------------------------



}
