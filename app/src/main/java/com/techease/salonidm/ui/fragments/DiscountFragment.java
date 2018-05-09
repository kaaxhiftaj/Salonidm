package com.techease.salonidm.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.salonidm.R;
import com.techease.salonidm.ui.adapters.DiscountsAdapter;
import com.techease.salonidm.ui.models.DiscountsModel;
import com.techease.salonidm.utils.AlertsUtils;
import com.techease.salonidm.utils.Configuration;
import com.techease.salonidm.utils.InternetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DiscountFragment extends Fragment {


    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    ArrayList<DiscountsModel> discounts_model_list;
    DiscountsAdapter discounts_adapter;
    String  merchant_id, dis_code, dis_percent, dis_valid_from, dis_valid_to, dis_status , discount_offer_id, usage_limit;
    Unbinder unbinder;

    @BindView(R.id.rv_discount)
    RecyclerView recyclerView;

    @BindView(R.id.add_discount)
    Button add_discount ;

    Typeface typeface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_discount, container, false);

        unbinder = ButterKnife.bind(this, v);
        customActionBar();
        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        typeface= Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Montserrat-Medium.ttf");

        add_discount.setTypeface(typeface);
        if (InternetUtils.isNetworkConnected(getActivity())) {



            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            discounts_model_list = new ArrayList<>();
            apicall();
            if (alertDialog == null)
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
            discounts_adapter = new DiscountsAdapter(getActivity(), discounts_model_list);
            recyclerView.setAdapter(discounts_adapter);


        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        add_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddDiscount();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("view").commit();

            }
        });

        return  v;
    }


    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.salonidm.com/salonpro/api/web/v1/auto-discount/discount-offer-list"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("200")) {
                    try {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArr = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject temp = jsonArr.getJSONObject(i);

                            DiscountsModel model = new DiscountsModel();
                            merchant_id = temp.getString("merchant_id");
                            discount_offer_id = temp.getString("discount_offer_id");
                            dis_code = temp.getString("discount_code");
                            dis_percent = temp.getString("discount_percentage");
                            dis_valid_from = temp.getString("valid_from");
                            dis_valid_to = temp.getString("valid_to");
                            dis_status = temp.getString("status");
                            usage_limit = temp.getString("usage_limit");



                            model.setDiscount_offer_id(discount_offer_id);
                            model.setMerchant_id(merchant_id);
                            model.setDiscount_code(dis_code);
                            model.setDiscount_percentage(dis_percent);
                            model.setValid_from(dis_valid_from);
                            model.setValid_to(dis_valid_to);
                            model.setStatus(dis_status);
                            model.setUsage_limit(usage_limit);
                            discounts_model_list.add(model);


                        }
                        discounts_adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (alertDialog != null)
                            alertDialog.dismiss();
                    }
                } else {

                    try {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        AlertsUtils.showErrorDialog(getActivity(), message);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (alertDialog != null)
                            alertDialog.dismiss();
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog != null)
                    alertDialog.dismiss();

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("vAuthToken", token);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }





    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setTypeface(typeface);
        ImageButton backbutton = (ImageButton)mCustomView.findViewById(R.id.back);
        mTitleTextView.setText("Discounts");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                mActionBar.setDisplayShowCustomEnabled(false);
                android.app.Fragment fragment = new MainFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });

    }
}
