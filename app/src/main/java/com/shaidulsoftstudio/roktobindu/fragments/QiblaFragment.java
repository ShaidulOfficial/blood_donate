package com.shaidulsoftstudio.roktobindu.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaidulsoftstudio.roktobindu.R;


public class QiblaFragment extends Fragment implements SensorEventListener {

    View view;
    ImageView compass;
    TextView marqTV;
    private static SensorManager sensorManager;
    private float current_degrees;

    public QiblaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_qibla, container, false);

        compass = view.findViewById(R.id.compass);
        marqTV = view.findViewById(R.id.marqTV);
        marqTV.setSelected(true);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager
                .getDefaultSensor(Sensor.TYPE_ORIENTATION), sensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float degree = Math.round(sensorEvent.values[0]);
        RotateAnimation rotateAnimation = new RotateAnimation(current_degrees, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(120);
        rotateAnimation.setFillAfter(true);
        compass.startAnimation(rotateAnimation);
        current_degrees = -degree;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}