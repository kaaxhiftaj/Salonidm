package com.techease.salonidm.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.techease.salonidm.R;
import com.techease.salonidm.utils.AlertsUtils;
import com.techease.salonidm.utils.Configuration;
import com.techease.salonidm.utils.InternetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class EditBusinessInfo extends Fragment {


    @BindView(R.id.merchant_name)
    EditText merchant_name;

    @BindView(R.id.merchant_email)
    EditText merchant_email;

    @BindView(R.id.salon_name)
    EditText salon_name;

    @BindView(R.id.address)
    EditText address;

    @BindView(R.id.website)
    EditText website;

    @BindView(R.id.state)
    MaterialSpinner state;

    @BindView(R.id.city)
    EditText city;

    @BindView(R.id.licensed_number)
    EditText licensed_number;

    @BindView(R.id.phone)
    EditText salon_number;

    @BindView(R.id.edit_info)
    Button edit_info;

    Unbinder unbinder;
    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    String m_name, m_email, s_name, s_website, s_address, s_city, s_state, s_licensed_number, s_salon_phone ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_business_info, container, false);
        unbinder = ButterKnife.bind(this, v);
        customActionBar();

        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        Bundle bundle = new Bundle();
        bundle.getString("id");
        m_name = getArguments().getString("merchant_name");
        m_email = getArguments().getString("merchant_email");
        s_name = getArguments().getString("salon_name");
        s_website = getArguments().getString("website");
        s_address = getArguments().getString("address");
        s_city = getArguments().getString("city");
        s_state = getArguments().getString("state");
        s_licensed_number = getArguments().getString("licensed_number");
        s_salon_phone = getArguments().getString("salon_phone");

        merchant_name.setText(m_name);
        merchant_email.setText(m_email);
        salon_name.setText(s_name);
        website.setText(s_website);
        address.setText(s_address);
        city.setText(s_city);
        salon_number.setText(s_salon_phone);
        licensed_number.setText(s_licensed_number);

        state.setItems(s_state, "Alabama", "Arizona", "Alaska", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District" , "Florida");
        state.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                s_state = item;

            }
        });


        edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (InternetUtils.isNetworkConnected(getActivity())) {

                    m_name = merchant_name.getText().toString();
                    m_email = merchant_email.getText().toString();
                    s_name = salon_name.getText().toString();
                    s_website = website.getText().toString();
                    s_address = address.getText().toString();
                    s_city = city.getText().toString();
                    s_salon_phone = salon_number.getText().toString();
                    s_licensed_number = licensed_number.getText().toString();

                    if ((s_state.equals("Alabama"))){
                        s_state = "1";
                    } else if (s_state.equals("Arizona")){
                        s_state = "2";
                    }else if (s_state.equals("Alaska")){
                        s_state = "3";
                    }else if (s_state.equals("Arkansas")){
                        s_state = "4";
                    }else if (s_state.equals("California")){
                        s_state = "5";
                    }else if (s_state.equals("Colorado")){
                        s_state = "6";
                    }else if (s_state.equals("Connecticut")){
                        s_state = "7";
                    }else if (s_state.equals("Delaware")){
                        s_state = "8";
                    }else if (s_state.equals("District")){
                        s_state = "9";
                    }else if (s_state.equals("Florida")){
                        s_state = "10";
                    }
                    

                    apicall();
                    if (alertDialog == null)
                        alertDialog = AlertsUtils.createProgressDialog(getActivity());
                    alertDialog.show();


                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;

    }


    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.salonidm.com/salonpro/api/web/v1/merchant/editsaloninfo"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("200")) {
                    if (alertDialog != null)
                        alertDialog.dismiss();

                    Fragment fragment = new BusinessInfo();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("view").commit();
                    Toast.makeText(getActivity(), "Information Saved", Toast.LENGTH_SHORT).show();


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
                params.put("merchant_name",m_name );
                params.put("merchant_email",m_email );
                params.put("salon_name", s_name);
                params.put("website", s_website);
                params.put("address", s_address);
                params.put("state", s_state);
                params.put("city", s_city);
                params.put("salon_phone", s_salon_phone);
                params.put("licensed_number",s_licensed_number);
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
        ImageButton backbutton = (ImageButton) mCustomView.findViewById(R.id.back);
        mTitleTextView.setText("Edit Business Information");
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