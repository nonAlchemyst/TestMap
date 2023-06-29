package com.example.testmap;

import com.example.testmap.viewmodel.Data;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class DataMarker extends Marker {

    private final Data _data;

    public DataMarker(MapView mapView, Data data) {
        super(mapView);
        _data = data;
    }


    public Data getData() {
        return _data;
    }
}
