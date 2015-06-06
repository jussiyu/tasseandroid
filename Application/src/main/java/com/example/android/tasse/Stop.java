package com.example.android.tasse;

import com.example.android.common.logger.Log;

import java.net.URI;
import java.net.URISyntaxException;

public class Stop {
    private static final String TAG = "Stop";
    private String mRef;
    private Integer mId = -1;

    public Stop(String ref) {
        mRef =ref;
    }

    public Stop() {
    }

    public void setRef(String ref) {
        mRef = ref;
//        Log.i(TAG, mRef );

        try {
            URI uri = new URI(mRef);
            String path = uri.getPath();
            String idStr = path.substring(path.lastIndexOf('/') + 1);
            mId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "stop id:" + mId.toString() );
    }

    public String getRef() {
        return mRef;
    }

    public int getId() {
        return mId;
    }
}
