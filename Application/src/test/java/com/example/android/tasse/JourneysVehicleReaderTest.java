package com.example.android.tasse;

import com.example.android.tasse.api.JourneysVehicleReader;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import junit.framework.TestCase;

public class JourneysVehicleReaderTest extends TestCase {

    String json = "{ \n" +
            "  \"status\" : \"success\", \n" +
            "  \"body\" : [ \n" +
            "    { \n" +
            "      \"monitoredVehicleJourney\" : { \n" +
            "        \"lineRef\" : \"1\", \n" +
            "        \"vehicleLocation\" : { \"longitude\" : \"23.9074487\", \"latitude\" : \"61.4930180\" }, \n" +
            "        \"operatorRef\" : \"Paunu\", \n" +
            "        \"vehicleRef\" : \"Paunu_166\", \n" +
            "        \"journeyPatternRef\" : \"1A\", \n" +
            "        \"onwardCalls\" : [ \n" +
            "          { \n" +
            "            \"expectedArrivalTime\" : \"2015-10-22T09:29:00+03:00\", \n" +
            "            \"expectedDepartureTime\" : \"2015-10-22T09:29:00+03:00\", \n" +
            "            \"stopPointRef\" : \"http://data.itsfactory.fi/journeys/api/1/stop-points/5132\", \n" +
            "            \"order\" : \"50\" \n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    MockWebServer mMockWebServer;
    private OkHttpClient mOkHttpClient;
    private HttpUrl mUrl;

    public void setUp() throws Exception {
        super.setUp();
        mOkHttpClient = new OkHttpClient();
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();

        mUrl = mMockWebServer.url("/journeys/api/1/vehicle-activity");
    }

    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }

    public void testLoadFromNetwork() throws Exception {

        mMockWebServer.enqueue(new MockResponse().setBody(json));

        final JourneysVehicleReader journeysVehicleReader = new JourneysVehicleReader(mOkHttpClient);
        journeysVehicleReader.loadFromNetwork(mUrl.toString());
        assertTrue(journeysVehicleReader.getVehicles().size() == 1);

    }

}