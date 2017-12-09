package com.reactnativeqrcodereader;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.HashMap;
import java.util.Map;

class QRCodeReader extends ReactContextBaseJavaModule {

    private static final String E_ACTIVITY_NOT_FOUND = "E_ACTIVITY_NOT_FOUND";
    private static final String E_SCAN_ERROR = "E_SCAN_ERROR";

    Promise promise;

    QRCodeReader(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(activityEventListener);
    }

    private final ActivityEventListener activityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            super.onActivityResult(activity, requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK && requestCode == 14) {
                if (data != null) {
                    promise.resolve(data.getStringExtra("data"));
                } else {
                    promise.resolve(null);
                }
            } else {
                promise.reject(E_SCAN_ERROR, E_SCAN_ERROR);
            }
        }
    };

    @Override
    public String getName() {
        return "RNQRCodeReader";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("ACTIVITY_NOT_FOUND", E_ACTIVITY_NOT_FOUND);
        constants.put("SCAN_ERROR", E_SCAN_ERROR);
        return constants;
    }

    @ReactMethod
    public void scan(ReadableMap options, Promise promise) {
        Activity currentActivity = getCurrentActivity();
        this.promise = promise;
        if (currentActivity == null) {
            this.promise.reject(E_ACTIVITY_NOT_FOUND, "Activity does not exist.");
            return;
        }
        try {
            Intent intent = new Intent(getCurrentActivity(), ContinuousCaptureActivity.class);
            if (options != null) {
                String noticeText = options.getString("placeholder");
                String title = options.getString("title");
                intent.putExtra("placeholder", noticeText);
                intent.putExtra("title", title);
            }
            currentActivity.startActivityForResult(intent, 14, null);
        } catch (Exception e) {
            this.promise.reject(E_SCAN_ERROR, e.getMessage());
        }
    }
}
