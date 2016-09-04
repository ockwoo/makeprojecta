package it.make.must.model;

import java.io.Serializable;

/**
 * Created by EVOL on 2016-07-04.
 */
public class Event implements Serializable {

    private double latitude;
    private double longitude;
    private String timestamp;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
