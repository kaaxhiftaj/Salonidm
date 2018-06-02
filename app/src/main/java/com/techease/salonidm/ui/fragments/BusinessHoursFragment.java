package com.techease.salonidm.ui.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
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
    Unbinder unbinder;
    Typeface typeface;

    @BindView(R.id.check_break)
    LinearLayout layout_break;

    @BindView(R.id.check_break_tue)
    LinearLayout layout_break_tue;
    @BindView(R.id.check_break_wed)
    LinearLayout layout_break_wed;
    @BindView(R.id.check_break_thur)
    LinearLayout layout_break_thur;
    @BindView(R.id.check_break_fri)
    LinearLayout layout_break_fri;
    @BindView(R.id.check_break_sat)
    LinearLayout layout_break_sat;
    @BindView(R.id.check_break_sun)
    LinearLayout layout_break_sun;


    @BindView(R.id.to_tue)
    TextView to_tue;
    @BindView(R.id.from_tue)
    TextView from_tue;

    @BindView(R.id.to_wed)
    TextView to_wed;
    @BindView(R.id.from_wed)
    TextView from_wed;

    @BindView(R.id.to_thur)
    TextView to_thur;
    @BindView(R.id.from_thur)
    TextView from_thur;

    @BindView(R.id.to_fri)
    TextView to_fri;
    @BindView(R.id.from_fri)
    TextView from_fri;

    @BindView(R.id.to_sat)
    TextView to_sat;
    @BindView(R.id.from_sat)
    TextView from_sat;

    @BindView(R.id.to_sun)
    TextView to_sun;
    @BindView(R.id.from_sun)
    TextView from_sun;

    LinearLayout layout_checkDay, layout_checkHour, layout_checkday_tue, layout_checkHour_tue, layout_checkday_wed, layout_checkHour_wed;
    LinearLayout layout_checkday_thur, layout_checkHour_thur, layout_checkday_fri, layout_checkHour_fri;
    LinearLayout layout_checkday_sat, layout_checkHour_sat, layout_checkday_sun, layout_checkHour_sun;
    TextView to, from;
    android.support.v7.widget.SwitchCompat swich, swich_tue, swich_wed, swich_thur, swich_fri, swich_sat, swich_sun;
    String strHours, strTo;
    @BindView(R.id.break_mon)
    TextView tv_break_mon;
    @BindView(R.id.break_tue)
    TextView tv_break_tue;
    @BindView(R.id.break_wed)
    TextView tv_break_wed;
    @BindView(R.id.break_thur)
    TextView tv_break_thur;
    @BindView(R.id.break_fri)
    TextView tv_break_fri;
    @BindView(R.id.break_sat)
    TextView tv_break_sat;
    @BindView(R.id.break_sun)
    TextView tv_break_sun;

    @BindView(R.id.start_break_mon)
    TextView start_break_mon;
    @BindView(R.id.end_break_mon)
    TextView end_break_mon;
    @BindView(R.id.start_break_tue)
    TextView start_break_tue;
    @BindView(R.id.end_break_tue)
    TextView end_break_tue;
    @BindView(R.id.start_break_wed)
    TextView start_break_wed;
    @BindView(R.id.end_break_wed)
    TextView end_break_wed;
    @BindView(R.id.start_break_thur)
    TextView start_break_thur;
    @BindView(R.id.end_break_thur)
    TextView end_break_thur;
    @BindView(R.id.start_break_fri)
    TextView start_break_fri;
    @BindView(R.id.end_break_fri)
    TextView end_break_fri;
    @BindView(R.id.start_break_sat)
    TextView start_break_sat;
    @BindView(R.id.end_break_sat)
    TextView end_break_sat;
    @BindView(R.id.start_break_sun)
    TextView start_break_sun;
    @BindView(R.id.end_break_sun)
    TextView end_break_sun;

    @BindView(R.id.remove_break_mon)
    TextView tv_remove_break_mon;
    @BindView(R.id.remove_break_tue)
    TextView tv_remove_break_tue;
    @BindView(R.id.remove_break_wed)
    TextView tv_remove_break_wed;
    @BindView(R.id.remove_break_thur)
    TextView tv_remove_break_thur;
    @BindView(R.id.remove_break_fri)
    TextView tv_remove_break_fri;
    @BindView(R.id.remove_break_sat)
    TextView tv_remove_break_sat;
    @BindView(R.id.remove_break_sun)
    TextView tv_remove_break_sun;
    final Calendar calendar=Calendar.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_business_hours, container, false);
        unbinder = ButterKnife.bind(this, v);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "Fonts/Montserrat-Medium.otf");
        to = (TextView) v.findViewById(R.id.to);
        from = (TextView) v.findViewById(R.id.from);
        swich = (android.support.v7.widget.SwitchCompat) v.findViewById(R.id.swich);
        swich_tue = (android.support.v7.widget.SwitchCompat) v.findViewById(R.id.swich_tue);
        swich_wed = (android.support.v7.widget.SwitchCompat) v.findViewById(R.id.swich_wed);
        swich_thur = (android.support.v7.widget.SwitchCompat) v.findViewById(R.id.swich_thur);
        swich_fri = (android.support.v7.widget.SwitchCompat) v.findViewById(R.id.swich_fri);
        swich_sat = (android.support.v7.widget.SwitchCompat) v.findViewById(R.id.swich_sat);
        swich_sun = (android.support.v7.widget.SwitchCompat) v.findViewById(R.id.swich_sun);
        layout_checkDay = v.findViewById(R.id.check_day);
        layout_checkHour = v.findViewById(R.id.check_hour);
        layout_checkday_tue = v.findViewById(R.id.check_day_tue);
        layout_checkHour_tue = v.findViewById(R.id.check_hour_tue);
        layout_checkday_wed = v.findViewById(R.id.check_day_wed);
        layout_checkHour_wed = v.findViewById(R.id.check_hour_wed);
        layout_checkday_thur = v.findViewById(R.id.check_day_thur);
        layout_checkHour_thur = v.findViewById(R.id.check_hour_thur);
        layout_checkday_fri = v.findViewById(R.id.check_day_fri);
        layout_checkHour_fri = v.findViewById(R.id.check_hour_fri);
        layout_checkday_sat = v.findViewById(R.id.check_day_sat);
        layout_checkHour_sat = v.findViewById(R.id.check_hour_sat);
        layout_checkday_sun = v.findViewById(R.id.check_day_sun);
        layout_checkHour_sun = v.findViewById(R.id.check_hour_sun);
        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        customActionBar();

        apicall();


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
                        JSONObject jsonObject_all = jsonObject.getJSONObject("data");

                        //for monday
                        JSONObject temp = jsonObject_all.getJSONObject("monday");

                        final String start_time = temp.getString("start_time_monday");
                        final String end_time = temp.getString("end_time_monday");
                        String check_day = temp.getString("check_day_monday");
                        String break_day = temp.getString("break_day_monday");
                        String break_start_time = temp.getString("break_start_time_monday");
                        String break_end_time = temp.getString("break_end_time_monday");


                        to.setText(end_time);
                        from.setText(start_time);

                        if (check_day.contains("0")) {
                            swich.setChecked(false);
                            swich.setClickable(false);
                            layout_checkHour.setVisibility(View.GONE);
                            layout_break.setVisibility(View.GONE);
                            tv_remove_break_mon.setVisibility(View.GONE);
                        }

                        if (break_day.contains("0") || break_day.contains("")) {
                            layout_break.setVisibility(View.GONE);
                            tv_break_mon.setVisibility(View.VISIBLE);
                            tv_remove_break_mon.setVisibility(View.GONE);
                            tv_break_mon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog("break_day_monday","break_start_time_monday", "break_end_time_monday", start_time, end_time);

                                }
                            });
                        }
                        if (break_day.contains("1")) {
                            swich.setClickable(false);
                            swich.setChecked(true);
                            layout_break.setVisibility(View.VISIBLE);
                            tv_break_mon.setVisibility(View.GONE);
                            tv_remove_break_mon.setVisibility(View.VISIBLE);
                            start_break_mon.setText(break_start_time);
                            end_break_mon.setText(break_end_time);
                        }

                        //for tuesday
                        JSONObject temp_tue = jsonObject_all.getJSONObject("tuesday");

                        final String start_time_tue = temp_tue.getString("start_time_tuesday");
                        final String end_time_tue = temp_tue.getString("end_time_tuesday");
                        String check_day_tue = temp_tue.getString("check_day_tuesday");
                        String break_day_tue = temp_tue.getString("break_day_tuesday");
                        String break_start_time_tue = temp_tue.getString("break_start_time_tuesday");
                        String break_end_time_tue = temp_tue.getString("break_end_time_tuesday");

                        from_tue.setText(start_time_tue);
                        to_tue.setText(end_time_tue);


                        if (check_day_tue.contains("0")) {
                            swich_tue.setChecked(false);
                            swich_tue.setClickable(false);
                            layout_checkHour_tue.setVisibility(View.GONE);
                            layout_break_tue.setVisibility(View.GONE);
                            tv_remove_break_tue.setVisibility(View.GONE);
                        }
                        if (break_day_tue.contains("0") || break_day_tue.contains("")) {
                            layout_break_tue.setVisibility(View.GONE);
                            tv_break_tue.setVisibility(View.VISIBLE);
                            tv_remove_break_tue.setVisibility(View.GONE);
                            tv_break_tue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog("break_day_tuesday","break_start_time_tuesday", "break_end_time_tuesday", start_time_tue, end_time_tue);

                                }
                            });
                        }

                        if (break_day_tue.contains("1")) {
                            swich_tue.setChecked(true);
                            swich_tue.setClickable(false);
                            layout_break_tue.setVisibility(View.VISIBLE);
                            tv_break_tue.setVisibility(View.GONE);
                            tv_remove_break_tue.setVisibility(View.VISIBLE);
                            start_break_tue.setText(break_start_time_tue);
                            end_break_tue.setText(break_end_time_tue);
                        }
                        //end

                        //for wednesday
                        JSONObject temp_wed = jsonObject_all.getJSONObject("wednesday");

                        final String start_time_wed = temp_wed.getString("start_time_wednesday");
                        final String end_time_wed = temp_wed.getString("end_time_wednesday");
                        String check_day_wed = temp_wed.getString("check_day_wednesday");
                        String break_day_wed = temp_wed.getString("break_day_wednesday");
                        String break_start_time_wed = temp_wed.getString("break_start_time_wednesday");
                        String break_end_time_wed = temp_wed.getString("break_end_time_wednesday");

                        to_wed.setText(end_time_wed);
                        from_wed.setText(start_time_wed);

                        if (check_day_wed.contains("0")) {
                            swich_wed.setChecked(false);
                            swich_wed.setClickable(false);
                            layout_checkHour_wed.setVisibility(View.GONE);
                            layout_break_wed.setVisibility(View.GONE);
                        }
                        if (break_day_wed.contains("0") || break_day_wed.contains("")) {
                            layout_break_wed.setVisibility(View.GONE);
                            tv_break_wed.setVisibility(View.VISIBLE);
                            tv_break_wed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog("break_day_wednesday","break_start_time_wednesday", "break_end_time_wednesday", start_time_wed, end_time_wed);
                                }
                            });
                        }

                        if (break_day_wed.contains("1")) {
                            swich_wed.setChecked(true);
                            swich_wed.setClickable(false);
                            layout_break_wed.setVisibility(View.VISIBLE);
                            tv_break_wed.setVisibility(View.GONE);
                            tv_remove_break_wed.setVisibility(View.VISIBLE);
                            start_break_wed.setText(break_start_time_wed);
                            end_break_wed.setText(break_end_time_wed);
                        }

                        //end

                        //for thursday
                        JSONObject temp_thur = jsonObject_all.getJSONObject("thursday");

                        final String start_time_thur = temp_thur.getString("start_time_thursday");
                        final String end_time_thur = temp_thur.getString("end_time_thursday");
                        String check_day_thur = temp_thur.getString("check_day_thursday");
                        String break_day_thur = temp_thur.getString("break_day_thursday");
                        String break_start_time_thur = temp_thur.getString("break_start_time_thursday");
                        String break_end_time_thur = temp_thur.getString("break_end_time_thursday");

                        to_thur.setText(start_time_thur);
                        from_thur.setText(end_time_thur);

                        if (check_day_thur.contains("0")) {
                            swich_thur.setChecked(false);
                            swich_thur.setClickable(false);
                            layout_checkHour_thur.setVisibility(View.GONE);
                            layout_break_thur.setVisibility(View.GONE);
                        }
                        if (break_day_thur.contains("0") || break_day_thur.contains("")) {
                            layout_break_thur.setVisibility(View.GONE);
                            tv_break_thur.setVisibility(View.VISIBLE);
                            tv_break_thur.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog("break_day_thursday","break_start_time_thursday", "break_end_time_thursday", start_time_thur, end_time_thur);
                                }
                            });
                        }

                        if (break_day_thur.contains("1")) {
                            swich_thur.setChecked(true);
                            swich_thur.setClickable(false);
                            layout_break_thur.setVisibility(View.VISIBLE);
                            tv_break_thur.setVisibility(View.GONE);
                            tv_remove_break_thur.setVisibility(View.VISIBLE);
                            start_break_thur.setText(break_start_time_thur);
                            end_break_thur.setText(break_end_time_thur);
                        }

                        //end

                        //for friday
                        JSONObject temp_fri = jsonObject_all.getJSONObject("friday");

                        final String start_time_fri = temp_fri.getString("start_time_friday");
                        final String end_time_fri = temp_fri.getString("end_time_friday");
                        String check_day_fri = temp_fri.getString("check_day_friday");
                        String break_day_fri = temp_fri.getString("break_day_friday");
                        String break_start_time_fri = temp_fri.getString("break_start_time_friday");
                        String break_end_time_fri = temp_fri.getString("break_end_time_friday");

                        to_fri.setText(start_time_fri);
                        from_fri.setText(end_time_fri);

                        if (check_day_fri.contains("0")) {
                            swich_fri.setChecked(false);
                            swich_fri.setClickable(false);
                            layout_checkHour_fri.setVisibility(View.GONE);
                            layout_break_fri.setVisibility(View.GONE);
                        }
                        if (break_day_fri.contains("0") || break_day_fri.contains("")) {
                            layout_break_fri.setVisibility(View.GONE);
                            tv_break_fri.setVisibility(View.VISIBLE);
                            tv_break_fri.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog("break_day_friday","break_start_time_friday", "break_end_time_friday", start_time_fri, end_time_fri);
                                }
                            });
                        }

                        if (break_day_fri.contains("1")) {
                            swich_fri.setChecked(true);
                            swich_fri.setClickable(false);
                            layout_break_fri.setVisibility(View.VISIBLE);
                            tv_break_fri.setVisibility(View.GONE);
                            tv_remove_break_fri.setVisibility(View.VISIBLE);
                            start_break_fri.setText(break_start_time_fri);
                            end_break_fri.setText(break_end_time_fri);
                        }

                        //end

                        //for saturday
                        JSONObject temp_sat = jsonObject_all.getJSONObject("saturday");

                        final String start_time_sat = temp_sat.getString("start_time_saturday");
                        final String end_time_sat = temp_sat.getString("end_time_saturday");
                        String check_day_sat = temp_sat.getString("check_day_saturday");
                        String break_day_sat = temp_sat.getString("break_day_saturday");
                        String break_start_time_sat = temp_sat.getString("break_start_time_saturday");
                        String break_end_time_sat = temp_sat.getString("break_end_time_saturday");

                        to_sat.setText(start_time_sat);
                        from_sat.setText(start_time_sat);
                        if (check_day_sat.contains("0")) {
                            swich_sat.setChecked(false);
                            swich_sat.setClickable(false);
                            layout_checkHour_sat.setVisibility(View.GONE);
                            layout_break_sat.setVisibility(View.GONE);
                        }
                        if (break_day_sat.contains("0") || break_day_sat.contains("")) {
                            layout_break_sat.setVisibility(View.GONE);
                            tv_break_sat.setVisibility(View.VISIBLE);
                            tv_break_sat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog("break_day_saturday","break_start_time_saturday", "break_end_time_saturday", start_time_sat, end_time_sat);

                                }
                            });
                        }

                        if (break_day_sat.contains("1")) {
                            swich_sat.setChecked(true);
                            swich_sat.setClickable(false);
                            layout_break_sat.setVisibility(View.VISIBLE);
                            tv_break_sat.setVisibility(View.GONE);
                            tv_remove_break_sat.setVisibility(View.VISIBLE);
                            start_break_sat.setText(break_start_time_sat);
                            end_break_sat.setText(break_end_time_sat);
                        }

                        //end

                        //for sunday
                        JSONObject temp_sun = jsonObject_all.getJSONObject("sunday");

                        final String start_time_sun = temp_sun.getString("start_time_sunday");
                        final String end_time_sun = temp_sun.getString("end_time_sunday");
                        String check_day_sun = temp_sun.getString("check_day_sunday");
                        String break_day_sun = temp_sun.getString("break_day_sunday");
                        String break_start_time_sun = temp_sun.getString("break_start_time_sunday");
                        String break_end_time_sun = temp_sun.getString("break_end_time_sunday");

                        from_sun.setText(start_time_sun);
                        to_sun.setText(end_time_sun);
                        if (check_day_sun.contains("0")) {
                            swich_sun.setChecked(false);
                            swich_sun.setClickable(false);
                            layout_checkHour_sun.setVisibility(View.GONE);
                            layout_break_sun.setVisibility(View.GONE);
                        }
                        if (break_day_sun.contains("0") || break_day_sun.contains("")) {
                            layout_break_sun.setVisibility(View.GONE);
                            tv_break_sun.setVisibility(View.VISIBLE);
                            tv_break_sun.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog("break_day_sunday","break_start_time_sunday", "break_end_time_sunday", start_time_sun, end_time_sun);

                                }
                            });
                        }

                        if (break_day_sun.contains("1")) {
                            swich_sun.setChecked(true);
                            swich_sun.setClickable(false);
                            layout_break_sun.setVisibility(View.VISIBLE);
                            tv_break_sun.setVisibility(View.GONE);
                            tv_remove_break_sun.setVisibility(View.VISIBLE);
                            start_break_sun.setText(break_start_time_sun);
                            end_break_sun.setText(break_end_time_sun);
                        }

                        //end


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
        ImageView backbutton = (ImageView) mCustomView.findViewById(R.id.back);
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


    public void apiCallforBusinessHour(final String breakDay,final String parameterStart, final String parameterEnd, final String startTime, final String endTime) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.salonidm.com/salonpro/api/web/v1/merchant-working-hours/update-timeslots"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                if (alertDialog != null)
                    alertDialog.dismiss();
                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){

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
                params.put(breakDay, "1");
                params.put(parameterStart, startTime);
                params.put(parameterEnd, endTime);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    public void showDialog(String breakDay,String parameterOne, String parametertwo, String from, String to) {
        final String strBreakDay = breakDay;
        final String breakStart_time = parameterOne;
        final String breakEnd_time = parametertwo;
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialoge_layout);
        TextView tv_dialog_from = dialog.findViewById(R.id.tv_dialoge_from);
        TextView tv_dialog_to = dialog.findViewById(R.id.tv_dialoge_to);
        final TextView Text1 = dialog.findViewById(R.id.dialoge_break_from);
        final TextView Text2 = dialog.findViewById(R.id.dialog_break_to);
        Button btnUpdate = (Button) dialog.findViewById(R.id.update);
        tv_dialog_from.setText(from);
        tv_dialog_to.setText(to);

        Text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                final int second = calendar.get(Calendar.SECOND);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(),R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)   {
                        Text1.setText( selectedHour + ":" + selectedMinute + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        Text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                final int second = calendar.get(Calendar.SECOND);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(),R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)   {
                        Text2.setText( selectedHour + ":" + selectedMinute + ":" + second);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallforBusinessHour(strBreakDay,breakStart_time, breakEnd_time, Text1.getText().toString(), Text2.getText().toString());
                dialog.dismiss();
                apicall();
            }
        });

        dialog.show();
    }
}
