package com.example.android.tasse.api;

import android.os.AsyncTask;

import com.example.android.common.logger.Log;
import com.example.android.tasse.Vehicle;
import com.squareup.okhttp.HttpUrl;

import java.util.List;

public class NetworkApi {
    private static final String TAG = "NetworkApi";

    private static final String HOST = "data.itsfactory.fi";
    private static final String SCHEME = "http";
    private final String PATH_JOURNEYS = "journeys";
    private final String PATH_API = "api";
    private static final String VERSION = "1";
    private static final String PATH_VEHICLE_ACTIVITY = "vehicle-activity";

    private static NetworkApiCallback networkApiCallback;

    public interface NetworkApiCallback {
        void onSuccess(List<Vehicle> result);

        void onError(String errorMessage);
    }

    public void executeNetworkOperation(Integer lineId, String vehId, NetworkApiCallback networkApiCallback) {
        NetworkApi.networkApiCallback = networkApiCallback;
        String uri = getUri(lineId, vehId);
        Log.v(TAG, uri);
        new DownloadTask().execute(uri);
    }

    private String getUri(Integer lineId, String vehId) {
        final HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(SCHEME)
                .host(HOST)
                .addPathSegment(PATH_JOURNEYS)
                .addPathSegment(PATH_API)
                .addPathSegment(VERSION)
                .addPathSegment(PATH_VEHICLE_ACTIVITY);

        if (lineId > 0) {
            builder.addQueryParameter("lineRef", lineId.toString());
        }
        if (vehId.length() > 0 && !vehId.equals("*")) {
            builder.addQueryParameter("vehicleRef", vehId);
        }
        return builder.build().toString();
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
            if (!isCancelled() && NetworkApi.networkApiCallback != null && !result.isEmpty()) {
                networkApiCallback.onSuccess(result);
            } else {
                networkApiCallback.onError("Failed to receive data from network.");
            }
        }


    }

}
