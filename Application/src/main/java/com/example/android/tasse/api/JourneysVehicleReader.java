package com.example.android.tasse.api;

import android.support.annotation.NonNull;

import com.example.android.tasse.Vehicle;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class JourneysVehicleReader {

    private List<Vehicle> mVehicles;
    private final OkHttpClient client;


    public JourneysVehicleReader(OkHttpClient client) {
        this.client = client;
    }


    /**
     * Initiates the fetch operation.
     */
    public boolean loadFromNetwork(@NonNull String urlString) {
        mVehicles = null;
        Reader reader = null;

        try {
            reader = downloadUrl(urlString);
            mVehicles = loadVehiclesFromNetwork(reader);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // Nothing to do
                }
            }
        }
    }

    public
    @NonNull
    List<Vehicle> getVehicles() {
        if (mVehicles != null) {
            return mVehicles;
        } else {
            return new ArrayList<Vehicle>();
        }
    }


    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     *
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws java.io.IOException
     */
    private Reader downloadUrl(String urlString) throws IOException {
        final Request request = new Request.Builder()
                .url(urlString)
                .build();

        final Response response = client.newCall(request).execute();

        return response.body().charStream();
    }

    /**
     * Creates list of Vechicles based on content from reader. Reader is closed after
     * consumption.
     *
     * @param reader
     * @return
     * @throws IOException
     */
    private List<Vehicle> loadVehiclesFromNetwork(Reader reader) throws IOException {
        Gson gson = new Gson();
        com.example.android.tasse.api.Response response;
        try {
            response = gson.fromJson(reader, com.example.android.tasse.api.Response.class);
            return response.body;
        } catch (JsonIOException | JsonSyntaxException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
        return new ArrayList<>();
    }

}

