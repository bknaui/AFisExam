package com.myapp.apangcatan.facebook_stream;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.myapp.apangcatan.facebook_stream.callbacks.StreamKeyRequestCalback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookStream {
    public static final String DIFFERENT_USER = "User logged in as different Facebook user";

    private Activity activity;
    private CallbackManager callbackManager;

    public FacebookStream(Activity activity) {
        this.activity = activity;
    }

    public void loginCallBack(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void logout() {
        LoginManager.getInstance().logOut();
    }

    public void createStreamKey(final StreamKeyRequestCalback keyRequestCalback) {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                addGraphRequest(accessToken, keyRequestCalback);
            }

            @Override
            public void onCancel() {
                keyRequestCalback.onError("Login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        keyRequestCalback.onError(DIFFERENT_USER);
                    }
                    return;
                }
                keyRequestCalback.onError(error.getMessage());
            }
        });
        LoginManager.getInstance().logInWithPublishPermissions(activity, Arrays.asList("publish_video"));
    }

    private void addGraphRequest(final AccessToken accessToken, final StreamKeyRequestCalback keyRequestCalback) {
        Log.e("UserID", accessToken.getUserId());
        Log.e("AccessToken", accessToken.getToken());
        GraphRequest graphRequest = GraphRequest.newPostRequest(accessToken,
                "/" + accessToken.getUserId() + "/live_videos", null,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject jsonObject = response.getJSONObject();
                        try {
                            String stream_key = jsonObject.getString("secure_stream_url");
                            int size = stream_key.split("/").length;
                            keyRequestCalback.onResponse(stream_key.split("/")[size - 1]);
                        } catch (JSONException e) {
                            keyRequestCalback.onError(e.getMessage());
                        }
                    }
                });
        graphRequest.executeAsync();
    }
}
