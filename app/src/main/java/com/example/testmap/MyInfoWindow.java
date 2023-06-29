package com.example.testmap;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

public class MyInfoWindow extends BasicInfoWindow {

    public MyInfoWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);
    }

    @Override
    public void onOpen(Object item) {

    }
}
