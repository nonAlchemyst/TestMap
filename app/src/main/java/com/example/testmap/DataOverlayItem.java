package com.example.testmap;

import com.example.testmap.viewmodel.Data;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

public class DataOverlayItem extends OverlayItem {

    private final Data _data;

    public DataOverlayItem(IGeoPoint aGeoPoint, Data data) {
        super(null, null, aGeoPoint);
        _data = data;
    }

    public Data getData() {
        return _data;
    }
}
