package com.techease.salonidm.ui.adapters;

/**
 * Created by kaxhiftaj on 2/28/18.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.techease.salonidm.R;
import com.techease.salonidm.ui.models.BusinessHoursModel;
import java.util.ArrayList;


public class BusinessHourAdapter extends RecyclerView.Adapter<BusinessHourAdapter.MyViewHolder> {

    ArrayList<BusinessHoursModel> bsnsHourModelArrayList;
    Context context;

    public BusinessHourAdapter(Context context, ArrayList<BusinessHoursModel> bsnsHourModels) {
        this.context=context;
        this.bsnsHourModelArrayList = bsnsHourModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_business_hours, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BusinessHoursModel model= bsnsHourModelArrayList.get(position);

        holder.day.setText(model.getDay());
        holder.from.setText(model.getStart_time());
        holder.to.setText(model.getEnd_time());
        if (!model.getBreak_start_time().equals("") && !model.getBreak_end_time().equals("")) {
            holder.brake.setText(model.getBreak_start_time() + " to " + model.getBreak_end_time());
        }
        if (model.getCheck_day().equals("0")){

            holder.swich.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {

        return bsnsHourModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView day, to, from, brake ;
        android.support.v7.widget.SwitchCompat swich ;

        public MyViewHolder(View itemView) {
            super(itemView);
            day=(TextView)itemView.findViewById(R.id.day);
            to=(TextView) itemView.findViewById(R.id.to);
            from = (TextView) itemView.findViewById(R.id.from);
            brake = (TextView) itemView.findViewById(R.id.brake);
            swich = (android.support.v7.widget.SwitchCompat)itemView.findViewById(R.id.swich);

        }


    }
}

