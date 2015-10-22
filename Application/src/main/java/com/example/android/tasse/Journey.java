package com.example.android.tasse;

import com.example.android.common.logger.Log;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Journey {
    private static final String TAG = "Journey";

    @SerializedName("onwardCalls")
    private List<Stop> mStops;

    @SerializedName("operatorRef")
    private String operatorReference = "";

    @SerializedName("vehicleRef")
    private String vehicleReference = "";

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
        return mStops != null && mStops.size() > 0 ? mStops.get(0) : null;
    }

    public String getVehicleReference() {
        return vehicleReference;
    }

    public String getOperatorReference() {
        return operatorReference;
    }
}
