package com.example.testmap.viewmodel;

public class Data {
    private final float lat;
    private final float lon;
    private final String name;
    private final int image;
    private final long date;

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public long getDate() {
        return date;
    }

    public Data(float lat, float lon, String name, int image, long date) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.image = image;
        this.date = date;
    }
}
