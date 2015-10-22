/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
This class is based on NetworkConnect class.
*/

package com.example.android.tasse;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;
import com.example.android.tasse.api.NetworkApi;

import java.util.List;

public class MainActivity extends FragmentActivity {

    public static final String TAG = "MainActivity";

    // Reference to the fragment showing events, so we can clear it with a button
    // as necessary.
    private LogFragment mLogFragment;
    private EditText mLineIdView;
    private EditText mStopIdView;
    private EditText mVehIdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        mLineIdView = (EditText) findViewById(R.id.lineId);
        mStopIdView = (EditText) findViewById(R.id.stopId);
        mVehIdView = (EditText) findViewById(R.id.vehId);

        // Initialize the logging framework.
        initializeLogging();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks FETCH, fetch the first 500 characters of
            // raw HTML from www.google.com.
            case R.id.fetch_action:
                doVehicleListQuery();

                return true;
            // Clear the log view fragment.
//            case R.id.clear_action:
//              mLogFragment.getLogView().setText("");
//              return true;
        }
        return false;
    }

    private void doVehicleListQuery() {
        mLogFragment.getLogView().setText("");
        Integer lineId = 1;
        String vehId = "";
        try {
            lineId = Integer.parseInt(mLineIdView.getText().toString());
            vehId = mVehIdView.getText().toString();
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse input parameters");
        }
        NetworkApi api = new NetworkApi();
        api.executeNetworkOperation(lineId, vehId, new NetworkApi.NetworkApiCallback() {

            @Override
            public void onSuccess(List<Vehicle> result) {
                int stopId = -1;
                try {
                    stopId = Integer.parseInt(mStopIdView.getText().toString());
                } catch (NumberFormatException e) {
                    // Ignore invalid ids
                }

                for (Vehicle v : result) {
                    Journey j = v.getJourney();
                    Stop s = j.getNextStop();
                    Log.i(TAG, "Journey: " + j.getVehicleReference() + ", next stop: " + s.getId());
                    if (s != null && stopId == s.getId()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(R.string.stop_found_message)
                                .setTitle("").setPositiveButton("Ok", null);
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Create a chain of targets that will receive log data
     */
    public void initializeLogging() {

        // Using Log, front-end to the logging chain, emulates
        // android.util.log method signatures.

        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        // A filter that strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        mLogFragment =
                (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());
    }
}
