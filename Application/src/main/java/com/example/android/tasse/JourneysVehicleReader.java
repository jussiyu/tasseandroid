package com.example.android.tasse;

import android.util.JsonReader;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JourneysVehicleReader {

    private List<Vehicle> mVehicles;
    private final OkHttpClient client;


    public JourneysVehicleReader() {
        client = new OkHttpClient();
        client.setReadTimeout(10, TimeUnit.SECONDS);
        client.setConnectTimeout(15, TimeUnit.SECONDS);
    }

    /** Initiates the fetch operation. */
    public void loadFromNetwork(String urlString) {
        mVehicles = null;
        InputStream stream = null;

        try {
            stream = downloadUrl(urlString);
            mVehicles = loadVehiclesFromNetwork(stream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // Nothing to do
                }
            }
        }
    }

    public List<Vehicle> getVehicles() {
        return mVehicles;
    }


    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws java.io.IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        final Request request = new Request.Builder()
                .url(urlString)
                .build();

        final Response response = client.newCall(request).execute();

        return response.body().byteStream();
    }

    /** Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from targeted site.
     * @param len Length of string that this method returns.
     * @return String concatenated according to len parameter.
     * @throws java.io.IOException
     * @throws java.io.UnsupportedEncodingException
     */
    private String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private List<Vehicle> loadVehiclesFromNetwork(InputStream stream) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
        try {
            return readVehiclesFromBody(reader);
        } finally{
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
            } else if (name.equals("vehicleRef" )) {
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
        while(reader.hasNext()) {
            reader.skipValue();
        }
        reader.endArray();
        return stop;
    }
}

