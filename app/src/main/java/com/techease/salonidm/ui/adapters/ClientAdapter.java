package com.techease.salonidm.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techease.salonidm.R;
import com.techease.salonidm.ui.models.ClientsModel;
import com.techease.salonidm.ui.models.ComissionsModel;

import java.util.ArrayList;

/**
 * Created by kaxhiftaj on 4/18/18.
 */

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder> {

    ArrayList<ClientsModel> clientModelArrayList;
    Context context;

    public ClientAdapter(Context context, ArrayList<ClientsModel> cleintsModels) {
        this.context=context;
        this.clientModelArrayList= cleintsModels;
    }

    @Override
    public ClientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_clients, parent, false);
        return new ClientAdapter.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final ClientsModel model=clientModelArrayList.get(position);

        holder.client_name.setText(model.getClient_name());
        holder.client_phone.setText(model.getClient_phone());
        holder.client_email.setText(model.getClient_email());
        holder.client_address.setText(model.getClient_address());

    }


    @Override
    public int getItemCount() {

        return clientModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView client_name, client_email, client_phone, client_address;
        Typeface typeface;

        public MyViewHolder(View itemView) {
            super(itemView);
            typeface= Typeface.createFromAsset(context.getAssets(),"Fonts/Montserrat-Medium.otf");
            client_name=(TextView)itemView.findViewById(R.id.client_name);
            client_email=(TextView) itemView.findViewById(R.id.client_email);
            client_phone = (TextView) itemView.findViewById(R.id.client_phone);
            client_address = (TextView) itemView.findViewById(R.id.client_address);

            client_address.setTypeface(typeface);
            client_email.setTypeface(typeface);
            client_name.setTypeface(typeface);
            client_phone.setTypeface(typeface);

        }


    }
}

