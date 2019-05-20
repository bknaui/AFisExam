package com.myapp.apangcatan.livestreamapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.myapp.apangcatan.facebook_stream.FacebookStream;
import com.myapp.apangcatan.facebook_stream.callbacks.StreamKeyRequestCalback;
import com.myapp.apangcatan.youtube_stream.YoutubeStream;

public class MainActivity extends AppCompatActivity {
    private FacebookStream facebookStream;
    private YoutubeStream youtubeStream;

    private TextView fbStreamKey;
    private TextView youtubeStreamKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fbStreamKey = findViewById(R.id.fb_stream_key);
        youtubeStreamKey = findViewById(R.id.youtube_stream_key);
    }

    public void getYoutubeStreamerKey(View view) {
        youtubeStream = new YoutubeStream(this);
        youtubeStream.createStreamKey(new com.myapp.apangcatan.youtube_stream.callbacks.StreamKeyRequestCalback() {
            @Override
            public void onResponse(String streamerKey) {
                youtubeStreamKey.setText(streamerKey);
            }

            @Override
            public void onError(String error) {
                youtubeStreamKey.setText(error);
            }
        });
    }

    public void getFacebookStreamerKey(View view) {

        facebookStream = new FacebookStream(this);
        facebookStream.createStreamKey(new StreamKeyRequestCalback() {
            @Override
            public void onResponse(String streamerKey) {
                fbStreamKey.setText(streamerKey);
            }

            @Override
            public void onError(String error) {
                fbStreamKey.setText("Error: " + error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            youtubeStream.loginCallBack(requestCode, resultCode, data);
            return;
        }
        facebookStream.loginCallBack(requestCode, resultCode, data);
    }

}
