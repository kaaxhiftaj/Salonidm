package com.techease.salonidm.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.DatePicker;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.techease.salonidm.R;
import com.techease.salonidm.ui.fragments.DiscountFragment;
import com.techease.salonidm.ui.fragments.MainFragment;
import com.techease.salonidm.utils.AlertsUtils;
import com.techease.salonidm.utils.Configuration;
import com.techease.salonidm.utils.InternetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AddDiscount extends Fragment{


    @BindView(R.id.discount_code)
    EditText discount_code;

    @BindView(R.id.discount_percentage)
    EditText discount_percentage;


    @BindView(R.id.valid_from)
    EditText valid_from;

    @BindView(R.id.valid_to)
    EditText valid_to;

    @BindView(R.id.status)
    MaterialSpinner status;

    @BindView(R.id.add_dis)
    Button add_dis;

    Unbinder unbinder;
    boolean check;
    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    private int day,month,year;
    private Calendar myCalendar;
    DatePickerDialog datePicker;
    DatePickerDialog.OnDateSetListener date;
    public static String id, to, fro, stat, cod, percent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_discount, container, false);


        unbinder = ButterKnife.bind(this, v);
        customActionBar();

         myCalendar = Calendar.getInstance();

        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");


        status.setItems("Choose ", "Active", "InActive");
        status.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                stat = item;

            }
        });

        valid_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FromDialog();

            }
        });

        valid_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            ToDialog();
            }
        });


        add_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetUtils.isNetworkConnected(getActivity())) {

                    cod = discount_code.getText().toString();
                    percent = discount_percentage.getText().toString();
                    fro = valid_from.getText().toString();
                    to = valid_to.getText().toString();

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.salonidm.com/salon_vendor/api/web/v1/auto-discount/add-discount-offer"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("200")) {
                    if (alertDialog != null)
                        alertDialog.dismiss();

                    Fragment fragment = new DiscountFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("view").commit();
                    Toast.makeText(getActivity(), "Discount Saved", Toast.LENGTH_SHORT).show();


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
                params.put("discount_code", cod);
                params.put("discount_percentage", percent);
                params.put("valid_from", fro);
                params.put("valid_to", to);
                params.put("status", stat);

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
        mTitleTextView.setText("Add Discounts");
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

        public void FromDialog(){
            DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
                {
                    monthOfYear = monthOfYear + 1 ;
                    valid_from.setText(year + "-" + monthOfYear +  "-" + dayOfMonth);
                }};
            DatePickerDialog dpDialog=new DatePickerDialog(getActivity(), listener, year, month, day);
            dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dpDialog.show();
        }


    public void ToDialog()  {
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
            {
                monthOfYear = monthOfYear +1 ;
                valid_to.setText(year + "-" + monthOfYear +"-" + dayOfMonth);
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(getActivity(), listener, year, month, day);
        String myDate = valid_from.getText().toString() +" 24:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        dpDialog.getDatePicker().setMinDate(millis);
        dpDialog.show();
    }



}




