package com.techease.salonidm.ui.fragments;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.techease.salonidm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AppointmentDetails extends Fragment {

    @BindView(R.id.client_name)
    TextView client_name;

    @BindView(R.id.service_name)
    TextView service_name;

    @BindView(R.id.booking_start_time)
    TextView booking_start_time;

    @BindView(R.id.booking_end_time)
    TextView booking_end_time;

    @BindView(R.id.appointment_status)
    TextView appointment_status;

    @BindView(R.id.appointment_type)
    TextView appointment_type;

    @BindView(R.id.price)
    TextView price;

    @BindView(R.id.payment_status)
    TextView payment_status;

    @BindView(R.id.booking_date)
    TextView booking_date;


    Typeface typeface;
    Unbinder unbinder;
    android.support.v7.app.AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_appointment_details, container, false);
        unbinder = ButterKnife.bind(this,v);

        typeface= Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Montserrat-Medium.ttf");
        Bundle bundle = new Bundle();
        String client_nam = getArguments().getString("client_name");
        String service_nam = getArguments().getString("service_name");
        String booking_start_tim = getArguments().getString("booking_start_time");
        String booking_end_tim = getArguments().getString("booking_end_time");
        String appointment_statu = getArguments().getString("appointment_status");
        String appointment_typ = getArguments().getString("appointment_type");
        String pric = getArguments().getString("price");
        String payment_statu = getArguments().getString("payment_status");
        String booking_dat = getArguments().getString("booking_date");

        client_name.setText(client_nam);
        service_name.setText(service_nam);
        booking_start_time.setText(booking_start_tim);
        booking_end_time.setText(booking_end_tim);
        appointment_status.setText(appointment_statu);
        appointment_type.setText(appointment_typ);
        price.setText(pric);
        payment_status.setText(payment_statu);
        booking_date.setText(booking_dat);

        client_name.setTypeface(typeface);
        service_name.setTypeface(typeface);
        booking_date.setTypeface(typeface);
        booking_end_time.setTypeface(typeface);
        booking_start_time.setTypeface(typeface);
        price.setTypeface(typeface);
        payment_status.setTypeface(typeface);
        appointment_status.setTypeface(typeface);
        appointment_type.setTypeface(typeface);





        return v;
    }
}
