package com.techease.salonidm.ui.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techease.salonidm.R;
import com.techease.salonidm.ui.activities.FullScreenActivity;
import com.techease.salonidm.ui.fragments.EditService;
import com.techease.salonidm.ui.models.ServicesModel;
import com.techease.salonidm.ui.fragments.ServicesFragment;
import com.techease.salonidm.ui.models.ServicesModel;
import java.util.ArrayList;

/**
 * Created by Adam Noor on 05-Feb-18.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    ArrayList<ServicesModel> serviceModelArrayList;
    Context context;

    public ServiceAdapter(Context context, ArrayList<ServicesModel> servicesModels) {
        this.context=context;
        this.serviceModelArrayList=servicesModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_services, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ServicesModel model=serviceModelArrayList.get(position);

       holder.service_name.setText(model.getService_name());
       holder.serice_details.setText("$" + model.getService_price() + " for " + model.getService_duration());
        Glide.with(context).load(model.getService_image()).into(holder.service_image);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditService();
                String service_id = model.getService_id();
                String service_name = model.getService_name();
                String service_desc = model.getService_desc();
                String service_price = model.getService_price();
                String service_discount = model.getService_discount();
                String service_duration = model.getService_duration();
                String service_image = model.getService_image();

                Bundle bundle=new Bundle();
                bundle.putString("service_id",service_id);
                bundle.putString("service_name",service_name);
                bundle.putString("service_desc",service_desc);
                bundle.putString("service_price",service_price);
                bundle.putString("service_discount",service_discount);
                bundle.putString("service_duration",service_duration);
                bundle.putString("service_image",service_image);
                fragment.setArguments(bundle);
                Activity activity = (FullScreenActivity) context;
                activity.getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("view").commit();

            }
        });

    }

    @Override
    public int getItemCount() {

        return serviceModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView service_name, serice_details;
        ImageView service_image ;
        ImageButton edit ;
        Typeface typeface;
        public MyViewHolder(View itemView) {
            super(itemView);
            typeface= Typeface.createFromAsset(context.getAssets(),"Fonts/Montserrat-Medium.ttf");
            service_name=(TextView)itemView.findViewById(R.id.service_name);
           serice_details=(TextView) itemView.findViewById(R.id.service_details);
           service_image = (ImageView)itemView.findViewById(R.id.service_image);
           edit = (ImageButton)itemView.findViewById(R.id.edit_service);

           serice_details.setTypeface(typeface);
           service_name.setTypeface(typeface);


        }


    }
}

