package com.techease.salonidm.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.techease.salonidm.ui.activities.FullScreenActivity;
import com.techease.salonidm.ui.adapters.ServiceAdapter;
import com.techease.salonidm.ui.models.ServicesModel;
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


public class BusinessInfo extends Fragment {


    @BindView(R.id.merchant_name)
    TextView merchant_name;

    @BindView(R.id.merchant_email)
    TextView merchant_email;

    @BindView(R.id.salon_name)
    TextView salon_name;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.website)
    TextView website;

    @BindView(R.id.state)
    TextView state;

    @BindView(R.id.city)
    TextView city;

    @BindView(R.id.licensed_number)
    TextView licensed_number;

    @BindView(R.id.saloon_phone)
    TextView saloon_phone ;

    @BindView(R.id.edit_info)
    Button edit_info ;

    Unbinder unbinder ;
    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    String m_name, m_email, s_name , s_website , s_address , s_city , s_state , s_licensed_number, s_saloon_phone ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_business_info, container, false);
        unbinder = ButterKnife.bind(this, v);
        customActionBar();
        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");


        if (InternetUtils.isNetworkConnected(getActivity())) {


            apicall();
            if (alertDialog == null)
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();


        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditBusinessInfo();

                Bundle bundle=new Bundle();
                bundle.putString("merchant_name",m_name);
                bundle.putString("merchant_email",m_email);
                bundle.putString("salon_name",s_name);
                bundle.putString("address",s_address);
                bundle.putString("website",s_website);
                bundle.putString("state",s_state);
                bundle.putString("city",s_city);
                bundle.putString("licensed_number",s_licensed_number);
                bundle.putString("salon_phone",s_saloon_phone);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("view").commit();
            }
        });



        return v;
    }




    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.salonidm.com/salonpro/api/web/v1/merchant/saloninfo"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("200")) {
                    try {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);

                        JSONObject temp = jsonObject.getJSONObject("data");


                             m_name = temp.getString("merchant_name");
                            m_email = temp.getString("merchant_email");
                             s_name = temp.getString("salon_name");
                             s_website = temp.getString("website");
                            s_address = temp.getString("address");
                            s_city = temp.getString("city");
                            s_state = temp.getString("state");
                            s_licensed_number = temp.getString("licensed_number");
                            s_saloon_phone = temp.getString("salon_phone");

                             merchant_name.setText(m_name);
                             merchant_email.setText(m_email);
                             salon_name.setText(s_name);
                             website.setText(s_website);
                             address.setText(s_address);
                             city.setText(s_city);
                             state.setText(s_state);
                             saloon_phone.setText(s_saloon_phone);
                             licensed_number.setText(s_licensed_number);




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
        ImageButton backbutton = (ImageButton)mCustomView.findViewById(R.id.back);
        mTitleTextView.setText("Business Information");
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
