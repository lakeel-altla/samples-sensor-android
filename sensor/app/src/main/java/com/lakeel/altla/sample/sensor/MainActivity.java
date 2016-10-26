package com.lakeel.altla.sample.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final HashMap<Integer, SensorDefinition> sensorDefinitionMap =
            new HashMap<Integer, SensorDefinition>() {
                {
                    put(Sensor.TYPE_ACCELEROMETER,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_ACCELEROMETER,
                                R.id.textViewAccelerometer));
                    put(Sensor.TYPE_AMBIENT_TEMPERATURE,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_AMBIENT_TEMPERATURE,
                                R.id.textViewAmbientTemperature));
                    put(Sensor.TYPE_GAME_ROTATION_VECTOR,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_GAME_ROTATION_VECTOR,
                                R.id.textViewGameRotationVector));
                    put(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_GEOMAGNETIC_ROTATION_VECTOR,
                                R.id.textViewGeomagneticRotationVector));
                    put(Sensor.TYPE_GRAVITY,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_GRAVITY,
                                R.id.textViewGravity));
                    put(Sensor.TYPE_GYROSCOPE,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_GYROSCOPE,
                                R.id.textViewGyroscope));
                    put(Sensor.TYPE_GYROSCOPE_UNCALIBRATED,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_GYROSCOPE_UNCALIBRATED,
                                R.id.textViewGyroscopeUncalibrated));
                    put(Sensor.TYPE_HEART_RATE,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_HEART_RATE,
                                R.id.textViewHeartRate));
                    put(Sensor.TYPE_LIGHT,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_LIGHT,
                                R.id.textViewLight));
                    put(Sensor.TYPE_LINEAR_ACCELERATION,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_LINEAR_ACCELERATION,
                                R.id.textViewLinearAcceleration));
                    put(Sensor.TYPE_MAGNETIC_FIELD,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_MAGNETIC_FIELD,
                                R.id.textViewMagneticField));
                    put(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_MAGNETIC_FIELD_UNCALIBRATED,
                                R.id.textViewMagneticFieldUncalibrated));
                    put(Sensor.TYPE_PRESSURE,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_PRESSURE,
                                R.id.textViewPressure));
                    put(Sensor.TYPE_PROXIMITY,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_PROXIMITY,
                                R.id.textViewProximity));
                    put(Sensor.TYPE_RELATIVE_HUMIDITY,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_RELATIVE_HUMIDITY,
                                R.id.textViewRelativeHumidity));
                    put(Sensor.TYPE_ROTATION_VECTOR,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_ROTATION_VECTOR,
                                R.id.textViewRotationVector));
                    put(Sensor.TYPE_SIGNIFICANT_MOTION,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_SIGNIFICANT_MOTION,
                                R.id.textViewSignificantMotion));
                    put(Sensor.TYPE_STEP_COUNTER,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_STEP_COUNTER,
                                R.id.textViewStepCounter));
                    put(Sensor.TYPE_STEP_DETECTOR,
                        new SensorDefinition(
                                Sensor.STRING_TYPE_STEP_DETECTOR,
                                R.id.textViewStepDetector));
                }
            };

    private SensorManager sensorManager;

    private Sensor ambientTemperatureSensor;

    private Sensor lightSensor;

    private Map<Integer, Sensor> sensorMap = new HashMap<>();

    private Map<Integer, TextView> sensorViewMap = new HashMap<>();

    private TextView ambientTemperatureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        initializeSensors();
    }

    private void initializeSensors() {
        for (int type : sensorDefinitionMap.keySet()) {
            int viewId = sensorDefinitionMap.get(type).viewId;
            TextView textView = (TextView) findViewById(viewId);
            sensorViewMap.put(type, textView);

            SensorDefinition sensorDefinition = sensorDefinitionMap.get(type);

            Sensor sensor = sensorManager.getDefaultSensor(type);
            if (sensor != null) {
                Log.i(LOG_TAG, "Sensor detected: typeName=" + sensorDefinition.name + ", name=" + sensor.getName() +
                               ", vendor=" + sensor.getVendor());
                sensorMap.put(type, sensor);
            } else {
                Log.e(LOG_TAG, "Sensor not found: typeName=" + sensorDefinition.name);
                textView.setText("N/A");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerSensorEventListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterSensorEventListeners();
    }

    private void registerSensorEventListeners() {
        for (int type : sensorMap.keySet()) {
            Sensor sensor = sensorMap.get(type);
            if (sensor != null) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
            }
        }
    }

    private void unregisterSensorEventListeners() {
        for (int type : sensorMap.keySet()) {
            Sensor sensor = sensorMap.get(type);
            if (sensor != null) {
                sensorManager.unregisterListener(this, sensor);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        showSensorValues(sensorEvent);
    }

    private void showSensorValues(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();

        StringBuilder builder = new StringBuilder();
        for (float value : sensorEvent.values) {
            builder.append(value);
            builder.append(", ");
        }

        if (2 <= builder.length()) {
            builder.setLength(builder.length() - 2);
        }

        TextView textView = sensorViewMap.get(type);
        textView.setText(builder);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private static class SensorDefinition {

        public final String name;

        public final int viewId;

        public SensorDefinition(String name, int viewId) {
            this.name = name;
            this.viewId = viewId;
        }
    }
}
