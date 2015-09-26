/**
 * Created by jussi on 27.8.15.
 */
package com.example.android.tasse;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

/**
 * Tests for NetworkConnect sample.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Activity mTestActivity;
    private EditText mLineIdEditText;
    private EditText mVehicleIdEditText;
    private EditText mStopIdEditText;
    private String title;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTestActivity = getActivity();
        mLineIdEditText = (EditText) mTestActivity.findViewById(R.id.lineId);
        mVehicleIdEditText = (EditText) mTestActivity.findViewById(R.id.vehId);
        mStopIdEditText = (EditText) mTestActivity.findViewById(R.id.stopId);
        title = (String) mTestActivity.getActionBar().getTitle().toString();
    }

    /**
     * Test if the test fixture has been set up correctly.
     */
    public void testPreconditions() {
        //Try to add a message to add context to your assertions. These messages will be shown if
        //a tests fails and make it easy to understand why a test failed
        assertNotNull("mTestActivity is null", mTestActivity);
        assertNotNull("lineId is null" , mLineIdEditText);
        assertNotNull("vehId is null", mVehicleIdEditText);
        assertNotNull("stopId is null", mStopIdEditText);
    }

    /**
     * Add more tests below.
     */

    public void testInitialEditTextValue_lineId() throws Exception {
        assertEquals("line Id initial value is incorrect", mLineIdEditText.getText().toString(),
                mTestActivity.getString(R.string.line_id_value_default).toString());
    }
    public void testInitialEditTextValue_vehId() throws Exception {
        assertEquals("vehicle Id initial value is incorrect", mVehicleIdEditText.getText().toString(),
                mTestActivity.getString(R.string.vehicle_id_value_default).toString());
    }
    public void testInitialEditTextValue_stopId() throws Exception {
        assertEquals("stopId initial value is incorrect", mStopIdEditText.getText().toString(),
                mTestActivity.getString(R.string.bus_stop_value_default).toString());
    }
    public void testEditTextHint_stopId() throws Exception {
        assertEquals("stopId hint is incorrect", mStopIdEditText.getHint().toString(),
                mTestActivity.getString(R.string.bus_stop_hint));
    }
    public void testEditTextHint_vehId() throws Exception {
        assertEquals("vehId hint is incorrect", mVehicleIdEditText.getHint().toString(),
                mTestActivity.getString(R.string.vehicle_id_hint));
    }
}
