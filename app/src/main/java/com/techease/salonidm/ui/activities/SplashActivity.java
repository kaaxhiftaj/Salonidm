package com.techease.salonidm.ui.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.techease.salonidm.R;
import com.techease.salonidm.ui.fragments.LoginFragment;
import com.techease.salonidm.ui.fragments.MainFragment;
import com.techease.salonidm.utils.Configuration;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        sharedPreferences = SplashActivity.this.getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token","");

        if (token.equals("")) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(3000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                        editor.putString("check", "login").commit();
                        startActivity(new Intent(SplashActivity.this, FullScreenActivity.class));
                        finish();

                    }
                }
            };
            timer.start();
        } else {

            editor.putString("check", "main").commit();
            startActivity(new Intent(SplashActivity.this, FullScreenActivity.class));
            finish();

        }
    }
}
