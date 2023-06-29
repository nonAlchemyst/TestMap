package com.example.testmap.viewmodel;

public class User {

    private final float _lat;
    private final float _lon;

    public User(float lat, float lon) {
        _lat = lat;
        _lon = lon;
    }

    public float getLat() {
        return _lat;
    }

    public float getLon() {
        return _lon;
    }
}
