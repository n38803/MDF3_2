package com.fullsail.android.mdf3w4.dataclass;

/**
 * Created by shaunthompson on 2/25/15.
 */
public class LocationClass {

    // class variables
    private String title;
    private String detail;
    private Long coordinates;
    private int image;

    // class constructors
    public LocationClass (String lTitle, String lDetail, Long lCoordinates, int lImage) {
        title=lTitle;
        detail=lDetail;
        coordinates=lCoordinates;
        image=lImage;

    }


    // class getters
    public String getTitle(){return title;}
    public String getDetail(){return detail;}
    public Long getCoordinates(){
        return coordinates;
    }
    public int getImage(){
        return image;
    }
}
