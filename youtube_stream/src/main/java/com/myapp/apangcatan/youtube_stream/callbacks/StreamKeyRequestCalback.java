package com.myapp.apangcatan.youtube_stream.callbacks;

public interface StreamKeyRequestCalback {
    void onResponse(String streamerKey);

    void onError(String error);
}
