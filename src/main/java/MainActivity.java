package main.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private float mLightReading;

    public static final FrameLayout.LayoutParams PARAMETERS = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup contents = findViewById(R.id.tab_contents);
        Toast.makeText(this, getString(R.string.home_transition), Toast.LENGTH_SHORT).show();

        // Initialize sensorManager, get access to light sensor, register sensorManager
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.mSensorManager = mSensorManager;
        mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this,
                sensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Inflates the correct views when corresponding nav bar button is clicked
        ImageButton homeBtn = findViewById(R.id.tab_home_page);
        homeBtn.setOnClickListener(item -> {
            contents.removeAllViews();
            contents.addView(getLayoutInflater().inflate(R.layout.home,
                    null), PARAMETERS);
            Toast.makeText(this,
                    getString(R.string.home_transition), Toast.LENGTH_SHORT).show();
        });
        ImageButton measurementBtn = findViewById(R.id.tab_measurement_page);
        measurementBtn.setOnClickListener(item -> {
            contents.removeAllViews();
            contents.addView(getLayoutInflater().inflate(R.layout.measurement,
                    null), PARAMETERS);
            Toast.makeText(this,
                    getString(R.string.measurement_transition), Toast.LENGTH_SHORT).show();
            setupAmbientLightSensor();
        });
        ImageButton tipsBtn = findViewById(R.id.tab_tips_page);
        tipsBtn.setOnClickListener(item -> {
            contents.removeAllViews();
            contents.addView(getLayoutInflater().inflate(R.layout.tips,
                    null), PARAMETERS);
            Toast.makeText(this,
                    getString(R.string.tips_transition), Toast.LENGTH_SHORT).show();
            setupLinksTipsPage();
        });
        ImageButton historyBtn = findViewById(R.id.tab_history_page);
        historyBtn.setOnClickListener(item -> {
            contents.removeAllViews();
            contents.addView(getLayoutInflater().inflate(R.layout.history,
                    null), PARAMETERS);
            Toast.makeText(this,
                    getString(R.string.history_transition), Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * CITATION: Code inspired by Stackoverflow post linked below
     * Sets up the linking functionality for source links
     */
    public void setupLinksTipsPage() {
        // CITATION - Code inspired (and changed) from StackOverflow in order to get link working in Android:
        // Link: https://stackoverflow.com/questions/10246366/autolink-inside-a-textview-in-android
        TextView link1 = (TextView) findViewById(R.id.tips_link_1);
        link1.setMovementMethod(LinkMovementMethod.getInstance());
        // NOTE: inspired (and changed) code completed
        TextView link2 = (TextView) findViewById(R.id.tips_link_2);
        link2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView link3 = (TextView) findViewById(R.id.tips_link_3);
        link3.setMovementMethod(LinkMovementMethod.getInstance());

        TextView link4 = (TextView) findViewById(R.id.tips_link_4);
        link4.setMovementMethod(LinkMovementMethod.getInstance());

        TextView link5 = (TextView) findViewById(R.id.tips_link_5);
        link5.setMovementMethod(LinkMovementMethod.getInstance());
    }


    public void setupAmbientLightSensor() {
        ImageButton measurementBtn = findViewById(R.id.measurement_btn);
        measurementBtn.setOnClickListener(item -> {
            // For reference, 400 lux is roughly equal to the sunrise or sunset on a clear day.
            // Source: Wikipedia: https://en.m.wikipedia.org/wiki/Lux
            ViewGroup contents = findViewById(R.id.tab_contents);
            // If the sensor reading is "dark"
            if (mLightReading < 400) {
                contents.removeAllViews();
                // Show the dark screen, and announce the change
                contents.addView(getLayoutInflater().inflate(R.layout.dark, null), PARAMETERS);
                Toast.makeText(this,
                        getString(R.string.dark_transition), Toast.LENGTH_SHORT).show();
            } else {
                // If the sensor reading is "bright"
                contents.removeAllViews();
                // Show the bright screen, and announce the change
                contents.addView(getLayoutInflater().inflate(R.layout.bright, null), PARAMETERS);
                Toast.makeText(this,
                        getString(R.string.bright_transition), Toast.LENGTH_SHORT).show();
            }
        });
    }


        /**
         * CITATION: Code inspired by Sensor activity slides from Week 8 Lecture and Section
         */
        public void onSensorChanged(SensorEvent event) {
            // ambient light array, stores the light reading value from sensor
            float light_in_lux = event.values[0];
            this.mLightReading = light_in_lux;
        }

        /**
         * CITATION: Code inspired by Sensor activity slides from Week 8 Lecture and Section
         */
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not required for my implementation
        }

    /**
     * CITATION: Code inspired by Sensor activity slides from Week 8 Lecture and Section
     */
    public void onResume() {
        super.onResume();
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, sensor, R.dimen.sensor_delay);
    }

    /**
     * CITATION: Code inspired by Sensor activity slides from Week 8 Lecture and Section
     */
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}