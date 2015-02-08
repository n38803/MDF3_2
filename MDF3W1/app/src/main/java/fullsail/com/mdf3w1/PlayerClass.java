package fullsail.com.mdf3w1;

/**
 * Created by shaunthompson on 2/8/15.
 */


public class PlayerClass {

    // class variables
    private String artist;
    private String title;
    private String file;

    // class constructors
    public PlayerClass (String sArtist, String sTitle, String sFile) {
        artist=sArtist;
        title=sTitle;
        file=sFile;
    }


    // class getters
    public String getArtist(){return artist;}
    public String getTitle(){return title;}
    public String getFile(){return file;}



}
