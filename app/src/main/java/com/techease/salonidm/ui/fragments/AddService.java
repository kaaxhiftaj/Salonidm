package com.techease.salonidm.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.techease.salonidm.R;
import com.techease.salonidm.controllers.VolleyMultipartRequest;
import com.techease.salonidm.utils.AlertsUtils;
import com.techease.salonidm.utils.Configuration;
import com.techease.salonidm.utils.GeneralUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.techease.salonidm.utils.GeneralUtils.getMimeTypeofFile;

public class AddService extends Fragment {


    @BindView(R.id.service_name)
    EditText service_name;

    @BindView(R.id.service_description)
    EditText service_desc;

    @BindView(R.id.service_price)
    EditText service_price;


    @BindView(R.id.service_discount)
    EditText service_discount;


    @BindView(R.id.select_colours)
    Button selectColoursButton;

    @BindView(R.id.service_duration)
    com.jaredrummler.materialspinner.MaterialSpinner service_duration;

    @BindView(R.id.add_image)
    ImageView add_image;

    @BindView(R.id.sendButton)
    Button send;


    StringBuilder stringBuilder ;
    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    Unbinder unbinder;
    String name, desc, price, discount, duration, service_cat;
    final int CAMERA_CAPTURE = 1;
    final int RESULT_LOAD_IMAGE = 2;
    File file;
    CharSequence[] colours = {"HEALTHY HAIR CARE", "BRAIDS" , "MAKEUP" , "MEN'S HAIR CUT", "MICROBLADING", "NAIL TECHNICIAN", "MASSAGE THERAIST", "HAIR CUTTING" , "TWIST & DREADS"};
    ArrayList<CharSequence> selectedColours;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_add_service, container, false);

        customActionBar();
        unbinder = ButterKnife.bind(this, v);

        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");


        selectedColours = new ArrayList<CharSequence>();

        selectColoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectColoursDialog();
            }
        });

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                }

            }
        });

        service_duration.setItems("Service Duration", "15 min", "30 min", "45 min", "1 hr", "1 hr 15 min", "1 hr 30 min", "1 hr 45 min", " 2 hr",
                "2 hr 15 min", " 2 hr 30 min", " 2 hr 45 min", " 3 hr");

        service_duration.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                duration = item;
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataInput();
            }
        });
        return v;
    }

    public void onDataInput() {
        name = service_name.getText().toString();
        desc = service_desc.getText().toString();
        price = service_price.getText().toString();
        discount = service_discount.getText().toString();


        if ((desc.equals(""))) {
            service_name.setError("Please enter valid name");
        } else if (desc.equals("")) {
            service_desc.setError("Please enter description");
        } else if (price.equals("")) {
            service_price.setError("Please enter price");
        } else if (discount.equals("")) {
            service_discount.setError("Please enter discount");
        } else if (duration.equals("")) {
            Toast.makeText(getActivity(), "Set Service Duration", Toast.LENGTH_SHORT).show();
        } else if (file == null) {
            Toast.makeText(getActivity(), "Please select image", Toast.LENGTH_SHORT).show();
        } else {
            // DialogUtils.showProgressSweetDialog(getActivity(), " Submiting your Application");
            if (alertDialog == null) {
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
                alertDialog.show();
            }
            apiCall();
        }
    }


    public void apiCall() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://www.salonidm.com/salonpro/api/web/v1/merchant-salon-services/createservice", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                if (alertDialog != null)
                    alertDialog.dismiss();
                if (response.statusCode == 200) {
                    Fragment fragment = new ServicesFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    Toast.makeText(getActivity(), "Service Created", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    // DialogUtils.sweetAlertDialog.dismiss();
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    if (alertDialog != null)
                        alertDialog.dismiss();

                    String result = new String(networkResponse.data);
                    Log.d("zma error response", String.valueOf(result));
                    //     DialogUtils.showWarningAlertDialog(getActivity(), result);
                    try {
                        JSONObject response = new JSONObject(result);
                        Log.d("zma response, obj", String.valueOf(response));
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Log.d("zma response, obj, s", String.valueOf(status));
                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        } else if (networkResponse.statusCode == 406) {
                            errorMessage = message + "Please decrease your video size";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("zma Error", errorMessage);
                if (alertDialog != null)
                    alertDialog.dismiss();
                // DialogUtils.showWarningAlertDialog(getActivity(),errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("vAuthToken", token);
                params.put("service_name", name);
                params.put("service_description", desc);
                params.put("service_category", stringBuilder.toString());
                params.put("price", price);
                params.put("discount", discount);
                params.put("duration", duration);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String mimeType = getMimeTypeofFile(file);
                params.put("files", new DataPart("files", GeneralUtils.getByteArrayFromFile(file), mimeType));
                Log.d("zma photo params", file.getPath());
                return params;
            }
        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(volleyMultipartRequest);
    }


    public void cameraIntent() {


        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, CAMERA_CAPTURE);

    }

    public void galleryIntent() {


        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, RESULT_LOAD_IMAGE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            final CharSequence[] itemedit = {"Take Photo", "Choose Image", "Cancel"};
            AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
            build.setTitle("Add Photo!");
            build.setItems(itemedit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    // boolean result=Utility.checkPermission(UsersDetails.this);
                    if (itemedit[item].equals("Take Photo")) {
                        cameraIntent();
                    } else if (itemedit[item].equals("Choose Image")) {
                        galleryIntent();
                    } else if (itemedit[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            build.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && null != data) {


            Uri selectedImage = data.getData();
            file = new File(GeneralUtils.getPath(getActivity().getApplicationContext(), selectedImage));
            Log.d("zma file", file.getPath());
            try {
                add_image.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAMERA_CAPTURE && null != data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            add_image.setImageBitmap(photo);

            Uri tempUri = GeneralUtils.getImageUri(getActivity().getApplicationContext(), photo);
            file = new File(GeneralUtils.getRealPathFromURI(getActivity().getApplicationContext(), tempUri));
            Log.d("zma file", file.getPath());
        }
    }


    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        ImageButton backbutton = (ImageButton) mCustomView.findViewById(R.id.back);
        mTitleTextView.setText("ADD SERVICE");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                mActionBar.setDisplayShowCustomEnabled(false);
                Fragment fragment = new ServicesFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }

    protected void showSelectColoursDialog() {

        boolean[] checkedColours = new boolean[colours.length];

        int count = colours.length;

        for (int i = 0; i < count; i++)

            checkedColours[i] = selectedColours.contains(colours[i]);

        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if (isChecked)

                    selectedColours.add(colours[which]);

                else

                    selectedColours.remove(colours[which]);

                onChangeSelectedColours();

            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Service Category");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            } });
        builder.setMultiChoiceItems(colours, checkedColours, coloursDialogListener);

        AlertDialog dialog = builder.create();

        dialog.show();

    }

    protected void onChangeSelectedColours() {

        stringBuilder = new StringBuilder();

        for(CharSequence colour : selectedColours)

            stringBuilder.append(colour + ",");

        selectColoursButton.setText(stringBuilder.toString());

    }


}