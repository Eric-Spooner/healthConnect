package com.ceg.med.healthconnect;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import static android.view.View.VISIBLE;

/**
 * Activity for the Myo Details view.
 */
public class DetailActivity extends AppCompatActivity implements CallbackAble<Integer> {

    public static final String PARAMETER_MAC = "myoMacAddress";
    public static final String PARAMETER_BLUETOOTH_DEVICE = "myoBluetoothDevice";
    public static final int DISPLAY_SIZE_ADAPTION = 46;

    private ProgressBar connectionBar;

    private BluetoothGatt bluetoothGatt;

    private boolean alternative;

    private ProgressBar bar;
    private int maxVal;

    private MyoGattCallback myoGattCallback;
    private MyoCommandList commandList = new MyoCommandList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i = getIntent();
        String myoMacAddress = i.getStringExtra(PARAMETER_MAC);
        BluetoothDevice bluetoothDevice = i.getExtras().getParcelable(PARAMETER_BLUETOOTH_DEVICE);

        ImageView imageView = findViewById(R.id.first_image);
        imageView.setImageDrawable(getDrawable(R.mipmap.hc));

        connectionBar = findViewById(R.id.detail_progress);
        connectionBar.setProgress(0);
        connectionBar.setVisibility(VISIBLE);

        bar = findViewById(R.id.myo_sensor_one);
        maxVal = 0;
        bar.setProgress(0);
        myoGattCallback = new MyoGattCallback(this);
        bluetoothGatt = bluetoothDevice.connectGatt(this, false, myoGattCallback);
        bar.setProgress(0);
        connectionBar.setProgress(100);
        connectionBar.setVisibility(View.GONE);
        myoGattCallback.setBluetoothGatt(bluetoothGatt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detailtoolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeBluetoothConnection();
                finish();
                return true;

            case R.id.myo_clear_max:
                bar.setSecondaryProgress(0);
                maxVal = 0;
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        closeBluetoothConnection();
        finish();
    }

    /**
     * Closes the bluetooth connection to the myo.
     */
    public void closeBluetoothConnection() {
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.close();
        bluetoothGatt = null;
    }

    @Override
    public void callback(Integer value) {
        bar.setProgress(value);
        if (maxVal < value) {
            maxVal = value;
            bar.setSecondaryProgress(value);
        }
    }

}
