package com.fullsail.android.mdf3w4.dataclass;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by shaunthompson on 2/25/15.
 */
public class LocationClass implements Serializable {

    private static final long serialVersionUID = 517116325584636891L;

    // class variables
    private String title;
    private String detail;
    private LatLng coordinates;
    private String image;

    // class constructors
    public LocationClass (String lTitle, String lDetail, LatLng lCoordinates, String lImage) {
        title=lTitle;
        detail=lDetail;
        coordinates=lCoordinates;
        image=lImage;

    }


    // class getters
    public String getTitle(){return title;}
    public String getDetail(){return detail;}
    public LatLng getCoordinates(){
        return coordinates;
    }
    public String getImage(){
        return image;
    }
}
