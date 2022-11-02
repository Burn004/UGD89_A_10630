package com.example.ugd89_a_10630_project1

import android.annotation.SuppressLint
import android.hardware.Camera
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import java.lang.Exception
import android.content.Context
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.BatteryManager
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    private var camBackId = Camera.CameraInfo.CAMERA_FACING_BACK
    private var camFrontId = Camera.CameraInfo.CAMERA_FACING_FRONT
    private var id_camera: Int = 1

    lateinit var proximitySensor: Sensor
    lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        id_camera = 0
        viewCamera(0)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null) {
            Toast.makeText(this, "No proximity sensor found in device..", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
            findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener { view: View? -> System.exit(0)}
    }

    var proximitySensorEventListener: SensorEventListener? = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0f) {
                    if(id_camera == 1){
                        id_camera = 0
                    }else{
                        id_camera = 1
                    }

                    mCamera!!.stopPreview()
                    mCamera!!.release()
                    viewCamera(id_camera)
                }
            }
        }
    }

    private fun viewCamera(id: Int)  {
        if(id==0){
            try {
                mCamera = Camera.open(0)
            } catch (e: Exception) {
                Log.d("Error","failed to get Camera" + e.message)
            }

        }else{
            try {
                mCamera = Camera.open(1)
            } catch (e: Exception) {
                Log.d("Error","failed to get Camera" + e.message)
            }
        }
        if (mCamera != null){
            mCameraView = CameraView(this,mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }
    }
}