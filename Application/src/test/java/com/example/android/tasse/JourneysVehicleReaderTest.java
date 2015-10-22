package com.example.android.tasse;

import com.example.android.tasse.api.JourneysVehicleReader;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import junit.framework.TestCase;

public class JourneysVehicleReaderTest extends TestCase {

    MockWebServer mMockWebServer;
    private OkHttpClient mOkHttpClient;

    public void setUp() throws Exception {
        super.setUp();
        mOkHttpClient = new OkHttpClient();
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();
    }

    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }

    public void testLoadFromNetwork() throws Exception {
        final HttpUrl url = mMockWebServer.url("/journeys/api/1/vehicle-activity");

        mMockWebServer.enqueue(new MockResponse().setBody("it's all cool"));

        final JourneysVehicleReader journeysVehicleReader = new JourneysVehicleReader(mOkHttpClient);
        journeysVehicleReader.loadFromNetwork(url.toString());
        assertTrue(journeysVehicleReader.getVehicles().isEmpty());

    }

}