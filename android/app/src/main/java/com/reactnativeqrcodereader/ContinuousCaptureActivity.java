package com.reactnativeqrcodereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class ContinuousCaptureActivity extends AppCompatActivity {
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private TextView textView;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("data", result.getText());
                setResult(Activity.RESULT_OK, returnIntent);
                // Prevent duplicate scans
                Log.d("RESULT: ", result.getText());
                finish();
                return;
            }
            lastText = result.getText();
            beepManager.playBeepSoundAndVibrate();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String placeholder = getIntent().getStringExtra("placeholder");
        String title = getIntent().getStringExtra("title");
        setContentView(R.layout.activity_continuous_capture);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        textView = (TextView) findViewById(R.id.toolBarTitle);
        textView.setText(title);
        barcodeView.decodeContinuous(callback);
        barcodeView.setStatusText(placeholder);
        beepManager = new BeepManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
