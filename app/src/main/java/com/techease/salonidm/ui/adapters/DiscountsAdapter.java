package com.techease.salonidm.ui.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techease.salonidm.R;
import com.techease.salonidm.ui.activities.FullScreenActivity;
import com.techease.salonidm.ui.fragments.AppointmentDetails;
import com.techease.salonidm.ui.fragments.EditDiscounts;
import com.techease.salonidm.ui.models.AppointmentModel;
import com.techease.salonidm.ui.models.DiscountsModel;

import java.util.ArrayList;

/**
 * Created by kaxhiftaj on 3/5/18.
 */

public class DiscountsAdapter extends RecyclerView.Adapter<DiscountsAdapter.MyViewHolder> {

    ArrayList<DiscountsModel> discountsModelArrayList;
    Context context;

    public DiscountsAdapter(Context context, ArrayList<DiscountsModel> discountsModels) {
        this.context=context;
        this.discountsModelArrayList = discountsModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_discounts, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DiscountsModel model = discountsModelArrayList.get(position);

        holder.discount_code.setText(model.getDiscount_code());
        holder.discount_percent.setText(model.getDiscount_percentage());
        holder.dis_status.setText(model.getStatus());
        holder.dis_valid.setText("Valid from : " +model.getValid_from() + " to " + model.getValid_to());

        holder.edit_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditDiscounts();

                String merchant_id = model.getMerchant_id();
                String discount_cod = model.getDiscount_code();
                String discount_percen = model.getDiscount_percentage();
                String discount_stat = model.getStatus();
                String validfrom = model.getValid_from();
                String validto = model.getValid_to();
                String discount_offer_id = model.getDiscount_offer_id();
                String usage = model.getUsage_limit();


                Bundle bundle=new Bundle();
                bundle.putString("merchant_id",merchant_id);
                bundle.putString("discount_code",discount_cod);
                bundle.putString("discount_percent",discount_percen);
                bundle.putString("discount_status",discount_stat);
                bundle.putString("discount_validation_from",validfrom);
                bundle.putString("discount_validation_to",validto);
                bundle.putString("discount_offer_id", discount_offer_id);
                bundle.putString("usage_limit", usage );

                fragment.setArguments(bundle);
                Activity activity = (FullScreenActivity) context;
                activity.getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("view").commit();

            }
        });

    }

    @Override
    public int getItemCount() {

        return discountsModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView discount_code, discount_percent, dis_status, dis_valid;
        ImageButton edit_discount;
        Typeface typeface;

        public MyViewHolder(View itemView) {
            super(itemView);
            typeface= Typeface.createFromAsset(context.getAssets(),"Fonts/Montserrat-Medium.otf");
            discount_code = (TextView) itemView.findViewById(R.id.discount_code);
            discount_percent = (TextView) itemView.findViewById(R.id.discount_percent);
            dis_status = (TextView)itemView.findViewById(R.id.discount_status);
            dis_valid = (TextView) itemView.findViewById(R.id.validation);
            edit_discount = (ImageButton)itemView.findViewById(R.id.edit_discount);

            dis_status.setTypeface(typeface);
            dis_valid.setTypeface(typeface);
            discount_code.setTypeface(typeface);
            discount_percent.setTypeface(typeface);


        }
    }
}

