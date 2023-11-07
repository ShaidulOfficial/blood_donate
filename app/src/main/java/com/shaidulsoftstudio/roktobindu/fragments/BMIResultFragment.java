package com.shaidulsoftstudio.roktobindu.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shaidulsoftstudio.roktobindu.R;


public class BMIResultFragment extends Fragment {

    View view;
    private Button re_calculate_bmi;
    private TextView bmi_advice, bmi_result_display, gender_display, bmi_display, bmi_age;
    private ImageView bmi_image_view;
    String bmi;
    float intBmi;
    int feet, inch, weight;
    float intheight, intfeet, intInch, intweight;

    public BMIResultFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_b_m_i_result, container, false);


        re_calculate_bmi = view.findViewById(R.id.re_calculate_bmi);
        bmi_result_display = view.findViewById(R.id.bmi_result_display);
        gender_display = view.findViewById(R.id.gender_display);
        bmi_display = view.findViewById(R.id.bmi_display);
        bmi_age = view.findViewById(R.id.bmi_age);
        bmi_image_view = view.findViewById(R.id.bmi_image_view);
        bmi_advice = view.findViewById(R.id.bmi_advice);

        Bundle bundle = this.getArguments();

        feet = bundle.getInt("feetProgress");
        inch = bundle.getInt("inchProgress");
        weight = bundle.getInt("weight");

        intheight = (float) (((feet * 12) + inch) * 0.0254);

        intBmi = weight / (intheight * intheight);

        //bmi= Float.toString(intBmi);
        bmi = String.valueOf(intBmi);

        if (intBmi < 16) {
            bmi_result_display.setText("Less Weight");
            bmi_result_display.setTextColor(Color.YELLOW);
            bmi_image_view.setImageResource(R.drawable.warning);
        } else if (intBmi < 16.9 && intBmi > 16) {
            bmi_result_display.setText("Less Weight");
            bmi_result_display.setTextColor(Color.YELLOW);
            bmi_image_view.setImageResource(R.drawable.warning);
        } else if (intBmi < 18.4 && intBmi > 17) {
            bmi_result_display.setText("Less Weight");
            bmi_result_display.setTextColor(Color.YELLOW);
            bmi_image_view.setImageResource(R.drawable.warning);
        } else if (intBmi < 25 && intBmi > 18.4) {
            bmi_result_display.setText("Normal");
            bmi_result_display.setTextColor(Color.GREEN);
            bmi_image_view.setImageResource(R.drawable.okk);
        } else if (intBmi < 29.4 && intBmi > 25) {
            bmi_result_display.setText("Over Weight");
            bmi_result_display.setTextColor(Color.BLUE);
            bmi_image_view.setImageResource(R.drawable.cross);
        } else {
            bmi_result_display.setText("Obesity");
            bmi_result_display.setTextColor(Color.RED);
            bmi_image_view.setImageResource(R.drawable.cross);
            bmi_advice.setVisibility(View.VISIBLE);
        }

        gender_display.setText(bundle.getString("genderBmi"));
        bmi_display.setText(bmi);
        bmi_age.setText(bundle.getString("age") + " Years Old");

        re_calculate_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BMIFragment bmiFragment = new BMIFragment();
                FragmentTransaction bmifragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                bmifragmentTransaction.replace(R.id.frameLayout_dashboard, bmiFragment).commit();

            }
        });
        return view;
    }
}