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
    private Double lat;
    private Double lon;

    // class constructors
    public LocationClass (String mTitle, String mDetail, Double mLat, Double mLon) {
        title=mTitle;
        detail=mDetail;
        lat=mLat;
        lon=mLon;

    }


    // class getters
    public String getTitle(){return title;}
    public String getDetail(){return detail;}
    public Double getLat(){
        return lat;
    }
    public Double getLong(){
        return lon;
    }

    public void setTitle(String mTitle)
    {
        this.title = title;
    }
}
