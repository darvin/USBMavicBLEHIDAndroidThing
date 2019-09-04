package com.darvin.usbmavicblehidandroidthing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.things.bluetooth.BluetoothProfileManager;
import com.google.android.things.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothClass
import android.util.Log
import com.google.android.things.bluetooth.BluetoothClassFactory
import com.google.android.things.bluetooth.BluetoothConfigManager
import jp.kshoji.blehid.JoystickPeripheral

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : AbstractBleActivity() {
    private lateinit var joystick: JoystickPeripheral

    override fun setupBlePeripheralProvider() {
        joystick = JoystickPeripheral(this)
        joystick.setDeviceName("rpijoy")
//        joystick.startAdvertising()

    }

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBTManager()
        initBTProfiles()
    }

    private fun initBTManager() {
        val manager = BluetoothConfigManager.getInstance()
// Report the local Bluetooth device class as a speaker
        manager.bluetoothClass = BluetoothClassFactory.build(
            BluetoothClass.Service.POSITIONING,
            BluetoothClass.Device.TOY_CONTROLLER
        )
        manager.ioCapability = BluetoothConfigManager.IO_CAPABILITY_IO
    }

    private fun initBTProfiles() {
        val manager = BluetoothProfileManager.getInstance()
        val toEnable = listOf(BluetoothProfile.GATT_SERVER)
        val toDisable = listOf(
            BluetoothProfile.A2DP_SINK,
            BluetoothProfile.AVRCP_CONTROLLER,
            BluetoothProfile.A2DP,
            BluetoothProfile.HEADSET,
            BluetoothProfile.HEALTH,
            BluetoothProfile.SAP
            )

        manager.enableAndDisableProfiles(toEnable, toDisable)
        val enabledProfiles = manager.enabledProfiles

        Log.d(TAG, "enabled profiles")

    }


    override fun onDestroy() {
        super.onDestroy()

        joystick.stopAdvertising()
    }

}
