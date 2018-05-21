package com.techease.salonidm.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.techease.salonidm.ui.adapters.BusinessHourAdapter;
import com.techease.salonidm.ui.adapters.ServiceAdapter;
import com.techease.salonidm.ui.models.BusinessHoursModel;
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

public class BusinessHoursFragment extends Fragment {

    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    ArrayList<BusinessHoursModel> bsnshours_model_list;
    BusinessHourAdapter bsns_hours_adapter;
    Unbinder unbinder;

    @BindView(R.id.rv_bsnshours)
    RecyclerView recyclerView;
    Typeface typeface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_business_hours, container, false);
        unbinder = ButterKnife.bind(this, v);
        typeface= Typeface.createFromAsset(getActivity().getAssets(),"Fonts/Montserrat-Medium.otf");
        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        customActionBar();

        if (InternetUtils.isNetworkConnected(getActivity())) {

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            bsnshours_model_list = new ArrayList<>();
            apicall();
            if (alertDialog == null)
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
            bsns_hours_adapter = new BusinessHourAdapter(getActivity(), bsnshours_model_list);
            recyclerView.setAdapter(bsns_hours_adapter);

        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        return v;
    }


    private void apicall() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.salonidm.com/salonpro/api/web/v1/merchant-working-hours/timeslots"
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

                            BusinessHoursModel  model = new BusinessHoursModel();
                            
                            String day = temp.getString("day");
                            String start_time = temp.getString("start_time");
                            String end_time = temp.getString("end_time");
                            String check_day = temp.getString("check_day");
                            String break_day = temp.getString("break_day");
                            String break_start_time = temp.getString("break_start_time");
                            String break_end_time = temp.getString("break_end_time");

                            model.setDay(day);
                            model.setStart_time(start_time);
                            model.setEnd_time(end_time);
                            model.setCheck_day(check_day);
                            model.setBreak_day(break_day);
                            model.setBreak_start_time(break_start_time);
                            model.setBreak_end_time(break_end_time);

                            bsnshours_model_list.add(model);


                        }
                        bsns_hours_adapter.notifyDataSetChanged();

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
        ImageView backbutton = (ImageView)mCustomView.findViewById(R.id.back);
        mTitleTextView.setText("Business Hours");
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