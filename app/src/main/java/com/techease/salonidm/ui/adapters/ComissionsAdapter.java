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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.salonidm.R;
import com.techease.salonidm.ui.activities.FullScreenActivity;
import com.techease.salonidm.ui.fragments.EditService;
import com.techease.salonidm.ui.models.ComissionsModel;
import com.techease.salonidm.ui.models.ServicesModel;

import java.util.ArrayList;

/**
 * Created by kaxhiftaj on 3/9/18.
 */

public class ComissionsAdapter extends RecyclerView.Adapter<ComissionsAdapter.MyViewHolder> {

    ArrayList<ComissionsModel> comissionsModelArrayList;
    Context context;

    public ComissionsAdapter(Context context, ArrayList<ComissionsModel> comissionsModels) {
        this.context=context;
        this.comissionsModelArrayList=comissionsModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_comissions, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ComissionsModel model=comissionsModelArrayList.get(position);

        holder.pay_method.setText("Payment Method : " +model.getPayment_method());
        holder.total_amount.setText("Total Payment : $" + model.getTotal_amount());
        holder.earn_amount.setText("Earned Payment $" + model.getEarned_amount());
        holder.date.setText("Date :" + model.getDate());



    }

    @Override
    public int getItemCount() {

        return comissionsModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView pay_method, total_amount, earn_amount, date;


        public MyViewHolder(View itemView) {
            super(itemView);
            pay_method=(TextView)itemView.findViewById(R.id.payment_method);
            total_amount=(TextView) itemView.findViewById(R.id.total_amount);
            earn_amount = (TextView) itemView.findViewById(R.id.earned_amount);
            date = (TextView) itemView.findViewById(R.id.date);

        }


    }
}

