package com.techease.salonidm.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.techease.salonidm.R;
import com.techease.salonidm.utils.AlertsUtils;
import com.techease.salonidm.utils.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BusinessFragment extends Fragment {


    @BindView(R.id.travel_charge)
    MaterialSpinner travel_charge;

    @BindView(R.id.free_travel_over)
    MaterialSpinner free_travel_over;

    @BindView(R.id.free_cancelation)
    MaterialSpinner free_cancelation;

    @BindView(R.id.temp_close_appoint)
    Switch temp_close_appoint;

    @BindView(R.id.min_book_appoint)
    Switch min_book_appoint;

    @BindView(R.id.is_travel_charged)
    Switch is_travel_charged;


    @BindView(R.id.save)
    Button save;

    Unbinder unbinder;
    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    Typeface typeface;
    SharedPreferences.Editor editor;
    String token, bsns_id , merchant_id, temp_close , is_travel ,  min_book , t_charge, t_over , free_cancel ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_business, container, false);

        typeface= Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Montserrat-Medium.ttf");
        unbinder = ButterKnife.bind(this, v);
        customActionBar();
        if (alertDialog == null)
            alertDialog = AlertsUtils.createProgressDialog(getActivity());
        alertDialog.show();
        apicall();


        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        save.setTypeface(typeface);
        free_cancelation.setTypeface(typeface);
        free_travel_over.setTypeface(typeface);
        temp_close_appoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                Toast.makeText(getActivity(), String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
            }
        });

        min_book_appoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getActivity(), String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
            }
        });

        is_travel_charged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getActivity(), String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
            }
        });


        travel_charge.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                t_charge = item;

            }
        });

        free_travel_over.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                t_over = item;

            }
        });

        free_cancelation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                free_cancel = item;

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (temp_close_appoint.isChecked() == true ){
                    temp_close = "1" ;
                }else {
                    temp_close = "0.00" ;
                }

                if (is_travel_charged.isChecked() == true){
                    is_travel = "1";
                }else {
                    is_travel = "0.00";
                }

                if (free_cancel.equals("At least 5 hours before appointment")){
                    free_cancel = "5hr" ;
                }else if (free_cancel.equals("At least 12 hours before appointment")){
                    free_cancel = "12hr" ;
                }
                else if (free_cancel.equals("At least 24 hours before appointment")){
                    free_cancel = "24hr" ;
                }
                else if (free_cancel.equals("At least 48 hours before appointment")){
                    free_cancel = "48hr" ;
                }

                if (t_charge.equals("$10")){
                    t_charge = "10" ;
                }else if (t_charge.equals("$20")){
                    t_charge = "20" ;
                }


                if (t_over.equals("$60")){
                    t_over = "60" ;
                }else if (t_over.equals("$80")){
                    t_over = "80" ;
                } else if (t_over.equals("$120")){
                    t_over = "120" ;
                }
                apicallupdate();
            }
        });


        return v;
    }


    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.salonidm.com/salonpro/api/web/v1/merchant/business-setting"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("200")) {
                    if (alertDialog != null)
                        alertDialog.dismiss();


                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        JSONObject temp = jsonObject.getJSONObject("data");

                        bsns_id = temp.getString("bussiess_id");
                        merchant_id = temp.getString("merchant_id");
                        t_charge = temp.getString("travell_charge");
                        t_over  = temp.getString("free_travel_over");
                        free_cancel = temp.getString("free_cancellation");
                         temp_close = temp.getString("temp_close_appointment");
                        is_travel = temp.getString("is_travell_charge");
                         min_book = temp.getString("travell_charge");


                        if (temp.getString("temp_close_appointment").equals("1")) {
                            temp_close_appoint.setChecked(true);
                        }

                        if (temp.getString("is_travell_charge").equals("1")) {
                            is_travel_charged.setChecked(true);
                        }


                        travel_charge.setItems(t_charge, "10", "20");
                        free_travel_over.setItems(t_over, "60" ,"80" , "120");
                        free_cancelation.setItems(free_cancel, "At least 5 hours before appointment", "At least 12 hours before appointment" , "At least 24 hours before appointment" , "At least 48 hours before appointment");


                    } catch (JSONException e) {
                        e.printStackTrace();


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
        ImageButton backbutton = (ImageButton) mCustomView.findViewById(R.id.back);
        mTitleTextView.setText("Business Settings");
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





    private void apicallupdate() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.salonidm.com/salonpro/api/web/v1/merchant/business-setting-update"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("200")) {
                    if (alertDialog != null)
                        alertDialog.dismiss();


                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        JSONObject temp = jsonObject.getJSONObject("data");
                        Fragment fragment = new MainFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("view").commit();

                        Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();


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
                params.put("temp_close_appointment", temp_close);
                params.put("min_book_amnt", "10p");
                params.put("travell_charge", t_charge);
                params.put("is_travell_charge",is_travel );
                params.put("free_travel_over", t_over);
                params.put("free_cancellation", free_cancel);
                params.put("business_logo", "");




                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }
}