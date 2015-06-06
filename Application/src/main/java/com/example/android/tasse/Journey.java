package com.example.android.tasse;

import com.example.android.common.logger.Log;

import java.util.ArrayList;
import java.util.List;

public class Journey {
    private static final String TAG = "Journey";
    private List mStops = new ArrayList<Stop>();

    public Journey(List<Stop> stops) {
        mStops = stops;
    }

    public Journey() {
    }

    public void addStop(Stop stop) {
        mStops.add(stop);
    }
    public void addVehRef(String ref) {
        Log.i(TAG, "vehicle id: " + ref);
    }

    public Stop getNextStop() {
        return (Stop) ( mStops.size() > 0 ? mStops.get(0) : null);
    }
}
