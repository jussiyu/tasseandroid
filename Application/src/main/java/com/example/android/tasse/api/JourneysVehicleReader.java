package com.example.android.tasse.api;

import android.support.annotation.NonNull;
import android.util.JsonReader;

import com.example.android.tasse.Journey;
import com.example.android.tasse.Stop;
import com.example.android.tasse.Vehicle;
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

    public @NonNull List<Vehicle> getVehicles() {
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
        JsonReader jsonReader = new JsonReader(reader);
        try {
            return readVehiclesFromBody(jsonReader);
        } finally {
            reader.close();
        }
    }

    private List<Vehicle> readVehiclesFromBody(JsonReader reader) throws IOException {
        List vehicles = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("body")) {
                vehicles = readVehicles(reader);
            } else {
                reader.skipValue();
            }
        }
        return vehicles;
    }

    private List<Vehicle> readVehicles(JsonReader reader) throws IOException {
        List<Vehicle> vehicles = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            vehicles.add(readVehicle(reader));
        }
        reader.endArray();
        return vehicles;

    }

    private Vehicle readVehicle(JsonReader reader) throws IOException {
        Vehicle v = new Vehicle();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("monitoredVehicleJourney")) {
                v.setJourney(readJourney(reader));
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return v;
    }

    private Journey readJourney(JsonReader reader) throws IOException {
        Journey j = new Journey();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("onwardCalls")) {
                j.addStop(readStop(reader));
            } else if (name.equals("vehicleRef")) {
                j.addVehRef(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return j;
    }

    private Stop readStop(JsonReader reader) throws IOException {
        Stop stop = new Stop();

        reader.beginArray();
        if (reader.hasNext()) {

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("stopPointRef")) {
                    stop.setRef(reader.nextString());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }
        // consume the rest of the bus stops as we don't yet use them
        while (reader.hasNext()) {
            reader.skipValue();
        }
        reader.endArray();
        return stop;
    }
}

