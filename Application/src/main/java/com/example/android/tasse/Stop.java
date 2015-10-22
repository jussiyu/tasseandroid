package com.example.android.tasse;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.net.URISyntaxException;

public class Stop {
    private static final String TAG = "Stop";

    @SerializedName("stopPointRef")
    private String mRef;

    public Stop(String ref) {
        mRef =ref;
    }

    public Stop() {

    }

    public int getId() {

        int id = -1;
        try {
            URI uri = new URI(mRef);
            String path = uri.getPath();
            String idStr = path.substring(path.lastIndexOf('/') + 1);
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return id;
    }
}
