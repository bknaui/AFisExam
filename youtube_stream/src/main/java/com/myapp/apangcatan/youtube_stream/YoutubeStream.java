package com.myapp.apangcatan.youtube_stream;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.myapp.apangcatan.youtube_stream.callbacks.StreamKeyRequestCalback;

public class YoutubeStream {

    private Activity activity;
    private StreamKeyRequestCalback streamKeyRequestCalback;

    public YoutubeStream(Activity activity) {
        this.activity = activity;
    }


    public void loginCallBack(int requestCode, int resultCode, @Nullable Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.e("G-AuthCode", account.getServerAuthCode() + "");
            Log.e("G-Email", account.getEmail() + "");
            Log.e("G-Token", account.getIdToken() + "");
            Log.e("G-Streamer", account.getGrantedScopes().toString() + "");
            streamKeyRequestCalback.onResponse("Email: "+account.getEmail()+"\nStreamer Key: Not implemented");

        } catch (ApiException e) {
            Log.e("Error", e.getMessage() + "");
        }

    }

    public void createStreamKey(StreamKeyRequestCalback streamKeyRequestCalback) {
        this.streamKeyRequestCalback = streamKeyRequestCalback;
        Log.e("CreateStreamKey", "Create");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, 0);
    }


}
