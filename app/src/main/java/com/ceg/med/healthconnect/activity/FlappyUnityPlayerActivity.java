package com.ceg.med.healthconnect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ceg.med.healthconnect.UnityPlayerActivity;
import com.ceg.med.healthconnect.data.CallbackAble;
import com.ceg.med.healthconnect.data.NiniGattCallback;
import com.unity3d.player.UnityPlayer;

import static com.ceg.med.healthconnect.activity.MainActivity.HEALTH_CONNECT_LOG_TAG;

public class FlappyUnityPlayerActivity extends UnityPlayerActivity implements CallbackAble<Integer> {

    private boolean active;

    public static FlappyUnityPlayerActivity currentActivity;

    public static FlappyUnityPlayerActivity instance() {
        return currentActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        active = false;
        currentActivity = this;
        Intent intent = getIntent();
        try {
            NiniGattCallback.set(this);
        } catch (Exception ex) {
            Log.d(HEALTH_CONNECT_LOG_TAG, ex.toString());
        }
    }

    @Override
    public void callback(Integer value) {
        if(active && value < 5){
            active = false;
        }else if(value > 5){
            active = true;
            flapBird(value);
        }
    }

    private void flapBird(int value) {
        UnityPlayer.UnitySendMessage("GameController", "androidFlap", String.valueOf(value));
    }

}
