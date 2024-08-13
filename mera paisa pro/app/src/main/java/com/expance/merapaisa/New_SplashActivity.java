package com.expance.merapaisa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import vocsy.ads.AdsHandler;
import vocsy.ads.GetSmartAdmob;

public class New_SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_splash);
        SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_DARK);
        String[] adsUrls = new String[]{getString(R.string.bnr_admob)// 1st Banner Ads Id
                , getString(R.string.native_admob)// 2st Native Ads Id
                , getString(R.string.int_admob)// 3st interstitial Ads Id
                , getString(R.string.app_open_admob)// 4st App-Open Ads Id
                , getString(R.string.video_admob)// 5st Rewarded Ads Id
        };


        new GetSmartAdmob(this, adsUrls, (success) -> {
            // admob init Success
        }).execute();

        AdsHandler.setAdsOn(true);
//        ImageView gifView;
//        gifView = findViewById(R.id.gifView);
//        Glide.with(this).asGif().load(R.drawable.gifffnewww).into(gifView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(New_SplashActivity.this, BaseActivity.class));
            }
        }, 2600);
    }
}