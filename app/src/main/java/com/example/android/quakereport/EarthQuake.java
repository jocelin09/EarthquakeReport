package com.example.android.quakereport;

/**
 * Created by jocelinthomas on 09/11/18.
 */

public class EarthQuake {

    private double mMagnitude;
    private String mLocation;
    private String mURL;
    private long mTimeInMilliseconds;


    public EarthQuake(double s, String s1, long s2,String s3) {
        mMagnitude = s;
        mLocation = s1;
        mTimeInMilliseconds = s2;
        mURL = s3;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public void setmMagnitude(double mMagnitude) {
        this.mMagnitude = mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public void setmTimeInMilliseconds(long mTimeInMilliseconds) {
        this.mTimeInMilliseconds = mTimeInMilliseconds;
    }

    public String getmURL() {
        return mURL;
    }

    public void setmURL(String mURL) {
        this.mURL = mURL;
    }
}
