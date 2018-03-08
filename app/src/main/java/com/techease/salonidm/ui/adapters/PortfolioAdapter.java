package com.techease.salonidm.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.techease.salonidm.R;
import com.techease.salonidm.ui.models.PortfolioModel;

import java.util.List;

/**
 * Created by kaxhiftaj on 3/8/18.
 */

public class PortfolioAdapter extends BaseAdapter {


    private List<PortfolioModel> contestents;
    private Context context;
    private LayoutInflater layoutInflater;
    MyViewHolder viewHolder = null;
    Typeface typefaceBold;
    public PortfolioAdapter(Context context, List<PortfolioModel> contestents) {

        this.context = context;
        this.contestents = contestents;
        if (context!=null)
        {
            this.layoutInflater=LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (contestents!=null) return contestents.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(contestents != null && contestents.size() > i) return  contestents.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        final PortfolioModel model= contestents.get(i);
        if(contestents != null && contestents.size() > i) return  contestents.size();
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final PortfolioModel model=contestents.get(i);
        viewHolder=new MyViewHolder() ;
        view=layoutInflater.inflate(R.layout.custom_portfolio,viewGroup,false);
        viewHolder.imageView=(ImageView)view.findViewById(R.id.ivPhotofrag);
        Glide.with(context).load(model.getGallary_img_name()).into(viewHolder.imageView);
        final Drawable drawable=viewHolder.imageView.getDrawable();
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_zoom_image);
                dialog.setCancelable(true);
                ImageView img = (ImageView) dialog.findViewById(R.id.ivZoomImage);
                Glide.with(context).load(model.getGallary_img_name()).into(img);
                dialog.show();
            }
        });
        view.setTag(viewHolder);
        return view;
    }


    public class MyViewHolder {

        ImageView imageView;

    }


}