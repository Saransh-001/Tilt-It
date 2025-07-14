package com.example.tiltit.accelerometer

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import com.example.tiltit.accelerometer.baseclass.AndroidSensor

class AccelerometerSensor(
    context: Context
): AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    sensorType = Sensor.TYPE_ACCELEROMETER
)