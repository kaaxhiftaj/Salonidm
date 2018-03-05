package com.techease.salonidm.ui.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.salonidm.R;
import com.techease.salonidm.ui.activities.FullScreenActivity;
import com.techease.salonidm.ui.fragments.AppointmentDetails;
import com.techease.salonidm.ui.fragments.EditService;
import com.techease.salonidm.ui.models.AppointmentModel;
import com.techease.salonidm.ui.models.ServicesModel;

import java.util.ArrayList;

/**
 * Created by kaxhiftaj on 3/2/18.
 */

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.MyViewHolder> {

    ArrayList<AppointmentModel> appointmentModelArrayList;
    Context context;

    public AppointmentsAdapter(Context context, ArrayList<AppointmentModel> appointmentsModels) {
        this.context=context;
        this.appointmentModelArrayList = appointmentsModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_appointments, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AppointmentModel model = appointmentModelArrayList.get(position);

        holder.service_name.setText(model.getService_name());
        holder.client_name.setText(model.getClient_name());
        holder.appointment_status.setText(model.getAppointment_status());

        holder.appointments_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AppointmentDetails();

                String merchant_id = model.getMerchant_id();
                String client_name = model.getClient_name();
                String service_name = model.getService_name();
                String booking_start_time = model.getBooking_start_time();
                String booking_end_time = model.getBooking_end_time();
                String appointment_status = model.getAppointment_status();
                String appointment_type = model.getAppointment_type();
                String price = model.getPrice();
                String payment_status =  model.getPayment_status();
                String booking_date = model.getBooking_date();

               Bundle bundle=new Bundle();
               bundle.putString("merchant_id",merchant_id);
                bundle.putString("client_name",client_name);
                bundle.putString("service_name",service_name);
                bundle.putString("booking_start_time",booking_start_time);
                bundle.putString("booking_end_time",booking_end_time);
                bundle.putString("appointment_status",appointment_status);
                bundle.putString("appointment_type",appointment_type);
                bundle.putString("price",price);
                bundle.putString("payment_status",payment_status);
                bundle.putString("booking_date",booking_date);
                fragment.setArguments(bundle);
                Activity activity = (FullScreenActivity) context;
                activity.getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("view").commit();

            }
        });

    }

    @Override
    public int getItemCount() {

        return appointmentModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView service_name, client_name, appointment_status;
        LinearLayout appointments_layout;


        public MyViewHolder(View itemView) {
            super(itemView);
            service_name = (TextView) itemView.findViewById(R.id.service_name);
            client_name = (TextView) itemView.findViewById(R.id.client_name);
            appointment_status = (TextView)itemView.findViewById(R.id.appointment_status);
            appointments_layout = (LinearLayout) itemView.findViewById(R.id.appointment_layout);


        }
    }
}

