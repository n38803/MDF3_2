package com.fullsail.android.mdf3w4.dataclass;

/**
 * Created by shaunthompson on 2/25/15.
 */
public class LocationClass {

    // class variables
    private String title;
    private String detail;
    private int image;

    // class constructors
    public LocationClass (String lTitle, String lDetail, int lImage) {
        title=lTitle;
        detail=lDetail;
        image=lImage;

    }


    // class getters
    public String getTitle(){return title;}
    public String getDetail(){return detail;}
    public int getImage(){
        return image;
    }
}
