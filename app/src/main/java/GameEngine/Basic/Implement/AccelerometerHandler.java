package GameEngine.Basic.Implement;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Quang Tung on 11/23/2015.
 */
public class AccelerometerHandler implements SensorEventListener {
    float accelX, accelY, accelZ;

    public AccelerometerHandler(Context context) {
        SensorManager sensorManager = (SensorManager)
                context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            Sensor accel = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);

            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    //Run in UI Thread
    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            accelX = event.values[0];
            accelY = event.values[1];
            accelZ = event.values[2];
        }
    }

    public float getAccelX() {
        synchronized (this) {
            return accelX;
        }
    }

    public float getAccelY() {
        synchronized (this) {
            return accelY;
        }
    }

    public float getAccelZ() {
        synchronized (this) {
            return accelZ;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do here
    }
}
