package com.example.android.tasse;

import com.google.gson.annotations.SerializedName;

public class Vehicle {
    @SerializedName("monitoredVehicleJourney")
    private Journey mJourney;

    public Vehicle() {

    }

    public void setJourney(Journey journey) {
        mJourney = journey;
    }

    public Journey getJourney() {
        return mJourney;
    }
}
