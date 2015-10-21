package com.example.android.tasse.api;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.common.logger.Log;
import com.example.android.tasse.Vehicle;

import java.util.List;

public class NetworkApi {
    private static final String TAG = "NetworkApi";
    private static NetworkApiCallback networkApiCallback;

    public interface NetworkApiCallback {
        public void onSuccess(List<Vehicle> result);
    }

    public void executeNetworkOperation(Integer lineId, String vehId, NetworkApiCallback networkApiCallback) {
        NetworkApi.networkApiCallback = networkApiCallback;
        try {
            String uri = getUri(lineId, vehId);
            Log.v(TAG, uri);
            new DownloadTask().execute(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUri(Integer lineId, String vehId) {
        Uri.Builder uriB = Uri.parse("http://data.itsfactory.fi/journeys/api/1/vehicle-activity")
                .buildUpon();
        if (lineId > 0) {
            uriB.appendQueryParameter("lineRef", lineId.toString());
        }
        if (vehId.length() > 0 && !vehId.equals("*")) {
            uriB.appendQueryParameter("vehicleRef", vehId);
        }
        return uriB.build().toString();
    }

    /**
     * Implementation of AsyncTask, to fetch the data in the background away from
     * the UI thread.
     */
    private class DownloadTask extends AsyncTask<String, Void, List<Vehicle>> {

        @Override
        protected List<Vehicle> doInBackground(String... url) {
            assert url.length == 1;
            JourneysVehicleReader reader = new JourneysVehicleReader(HttpClientFactory.INSTANCE.getClient());
            reader.loadFromNetwork(url[0]);
            return reader.getVehicles();
        }

        /**
         * Uses the logging framework to display the output of the fetch
         * operation in the log fragment.
         */
        @Override
        protected void onPostExecute(List<Vehicle> result) {
            if (NetworkApi.networkApiCallback != null) {
                networkApiCallback.onSuccess(result);
            }
        }


    }

}
