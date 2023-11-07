package com.shaidulsoftstudio.roktobindu.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shaidulsoftstudio.roktobindu.R;


public class BMIFragment extends Fragment {

    View view;
    private SeekBar seekbarFeet, seekbarInch;
    private TextView feetTv, inchTv, current_age, current_weight;
    private RelativeLayout male, female;
    private Button calculate_bmi;
    private ImageView increment_age, decrement_age, increment_weight, decrement_weight;
    int weight = 50;
    int age = 20;
    int currentFeetProgress;
    int currentInchProgress;
    String feetProgress = "8";
    String inchProgress = "12";
    String typeofUser = "0";
    String weight2 = "50";
    String age2 = "20";


    public BMIFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_b_m_i, container, false);

        seekbarFeet = view.findViewById(R.id.seekbarFeet);
        seekbarInch = view.findViewById(R.id.seekbarInch);
        feetTv = view.findViewById(R.id.current_height_feet);
        inchTv = view.findViewById(R.id.current_height_Inch);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        calculate_bmi = view.findViewById(R.id.calculate_bmi);
        increment_age = view.findViewById(R.id.increment_age);
        increment_weight = view.findViewById(R.id.increment_weight);
        decrement_age = view.findViewById(R.id.decrement_age);
        decrement_weight = view.findViewById(R.id.decrement_weight);
        current_weight = view.findViewById(R.id.current_weight);
        current_age = view.findViewById(R.id.current_age);



        seekbarFeet.setMax(8);
        seekbarInch.setMax(12);

        calculate_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bmiCalculate();
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.card_back_btn));
                female.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.card_back_not_focus));
                typeofUser = "Male";
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.card_back_btn));
                male.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.card_back_not_focus));
                typeofUser = "Female";
            }
        });

        seekbarFeet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                currentFeetProgress = progress;
                feetProgress = String.valueOf(currentFeetProgress);
                feetTv.setText(feetProgress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarInch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentInchProgress = progress;
                inchProgress = String.valueOf(currentInchProgress);
                inchTv.setText(inchProgress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        increment_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                age = age + 1;
                age2 = String.valueOf(age);
                current_age.setText(age2);
            }
        });
        decrement_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = age - 1;
                age2 = String.valueOf(age);
                current_age.setText(age2);
            }
        });
        increment_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weight = weight + 1;
                weight2= String.valueOf(weight);
                current_weight.setText(weight2);

            }
        });
        decrement_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weight = weight - 1;
                weight2= String.valueOf(weight);
                current_weight.setText(weight2);
            }
        });

        return view;
    }

    private void bmiCalculate() {

       String feetValue=feetTv.getText().toString().trim();

        if (typeofUser.equals("0")) {
            Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
        } else if (weight == 0 || weight < 0) {
            Toast.makeText(getActivity(), "Weight is need", Toast.LENGTH_SHORT).show();
        } else if (feetValue.equals("00")) {
            Toast.makeText(getActivity(), "Feet is need", Toast.LENGTH_SHORT).show();
        } else if (age == 0 || age < 0) {
            Toast.makeText(getActivity(), "Age is need", Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("genderBmi", typeofUser);
            bundle.putInt("weight", Integer.parseInt(weight2));
            bundle.putString("age", age2);
            bundle.putInt("feetProgress", Integer.parseInt(feetProgress));
            bundle.putInt("inchProgress", Integer.parseInt(inchProgress));

            BMIResultFragment bmiResultFragment = new BMIResultFragment();
            bmiResultFragment.setArguments(bundle);
            FragmentTransaction bmiResultfragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            bmiResultfragmentTransaction.replace(R.id.frameLayout_dashboard, bmiResultFragment)
                    .commit();
        }
    }
}