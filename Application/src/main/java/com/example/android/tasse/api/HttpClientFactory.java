package com.example.android.tasse.api;

import com.squareup.okhttp.OkHttpClient;
import java.util.concurrent.TimeUnit;

public enum HttpClientFactory {
    INSTANCE;

    public OkHttpClient getClient() {
        OkHttpClient client;
        client = new OkHttpClient();
        client.setReadTimeout(10, TimeUnit.SECONDS);
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        return client;
    }
}
