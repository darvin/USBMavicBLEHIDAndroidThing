package com.darvin.usbmavicblehidandroidthing;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.darvin.usbmavicblehidandroidthing.R.string;
import jp.kshoji.blehid.util.BleUtils;

/**
 * Common procedures for BLE activities
 * 
 * @author K.Shoji
 */
public abstract class AbstractBleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!BleUtils.isBluetoothEnabled(this)) {
            BleUtils.enableBluetooth(this);
            return;
        }

        if (!BleUtils.isBleSupported(this) || !BleUtils.isBlePeripheralSupported(this)) {
            // display alert and exit
            final AlertDialog alertDialog = new Builder(this).create();
            alertDialog.setTitle("BLE not supported!");
            alertDialog.setMessage("BLE not supported!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ok",
                    new OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(final DialogInterface dialog) {
                    finish();
                }
            });
            alertDialog.show();
        } else {
            setupBlePeripheralProvider();
        }
    }
    
    abstract void setupBlePeripheralProvider();

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BleUtils.REQUEST_CODE_BLUETOOTH_ENABLE) {
            if (!BleUtils.isBluetoothEnabled(this)) {
                // User selected NOT to use Bluetooth.
                // do nothing
                Toast.makeText(this, "needs to enable bluetooth", Toast.LENGTH_LONG).show();
                return;
            }

            if (!BleUtils.isBleSupported(this) || !BleUtils.isBlePeripheralSupported(this)) {
                // display alert and exit
                final AlertDialog alertDialog = new Builder(this).create();
                alertDialog.setTitle("BLE not supported!");
                alertDialog.setMessage("BLE not supported!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ok",
                        new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(final DialogInterface dialog) {
                        finish();
                    }
                });
                alertDialog.show();
            } else {
                setupBlePeripheralProvider();
            }
        }
    }
}
