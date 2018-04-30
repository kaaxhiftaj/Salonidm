package com.techease.salonidm.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.salonidm.R;
import com.techease.salonidm.utils.AlertsUtils;
import com.techease.salonidm.utils.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LoginFragment extends Fragment  {

    @BindView(R.id.et_email_signin)
    EditText et_email_signin ;

    @BindView(R.id.et_password_signin)
    EditText et_password_signin ;

    @BindView(R.id.btn_signin)
    Button btn_signin ;

    android.support.v7.app.AlertDialog alertDialog;

    Unbinder unbinder ;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strEmail, strPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, v);
        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataInput();
            }
        });

        return v ;
    }



    public void onDataInput() {
        strEmail = et_email_signin.getText().toString();
        strPassword = et_password_signin.getText().toString();
        if ((strEmail.length() < 3 ) ){
            et_email_signin.setError("Please enter valid email id");
        } else if (strPassword.equals("")) {
            et_password_signin.setError("Please enter your password");
        } else {
            if (alertDialog == null)
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
            apiCall();
        }
    }

    public void apiCall() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,  "http://www.salonidm.com/salonpro/api/web/v1/merchant/login"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("200")) {
                    try {
                            if (alertDialog != null)
                                alertDialog.dismiss();

                        JSONObject jsonObject = new JSONObject(response).getJSONObject("data");
                        String salon_name = jsonObject.getString("salon_name");
                        String merchant_name =jsonObject.getString("merchant_name");
                        String merchant_email =jsonObject.getString("merchant_email");
                        String vAuthToken = jsonObject.getString("authToken");

                        editor.putString("token", vAuthToken).commit();
                        Fragment fragment = new MainFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


                    } catch (JSONException e) {
                        e.printStackTrace();
                            if (alertDialog != null)
                                alertDialog.dismiss();
                        Log.d("error", String.valueOf(e.getMessage()));
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
                    }

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog != null)
                    alertDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                } else if (error instanceof AuthFailureError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Auth Failure");
                } else if (error instanceof ServerError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Wrong username or password");
                } else if (error instanceof NetworkError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Network Error");
                } else if (error instanceof ParseError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Parsing Error");
                }


            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("vUserName", strEmail);
                params.put("vPassword", strPassword);
                params.put("txDeviceToken", "sdfjhaskdfjh");
                params.put("tiDeviceType", "android");
                params.put("vVersion", "1");
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

}
