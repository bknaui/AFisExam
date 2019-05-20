package com.myapp.apangcatan.facebook_stream.callbacks;

public interface StreamKeyRequestCalback {
    void onResponse(String streamerKey);

    void onError(String error);
}
