package com.ilya.MeetingMap.Map.shake_logik

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
    context: Context,
    private val shakeListener: OnShakeListener
) : SensorEventListener {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastShakeTime: Long = 0
    private val shakeThresholdGravity = 5f
    private val shakeSlopTimeMs = 500

    init {
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            // Вычисление общей силы тряски
            val gForce = sqrt(gX * gX + gY * gY + gZ * gZ)

            if (gForce > shakeThresholdGravity) {
                val now = System.currentTimeMillis()
                if (lastShakeTime + shakeSlopTimeMs > now) {
                    return
                }
                lastShakeTime = now
                shakeListener.onShake()
            }
        }
    }

    interface OnShakeListener {
        fun onShake()
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }
}


