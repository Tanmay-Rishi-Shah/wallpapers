package com.expance.merapaisa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import vocsy.ads.AppUtil;
import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

public class GetStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);
        GoogleAds.getInstance().addNativeView(this, (LinearLayout) findViewById(R.id.nativeLay1));
        SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_DARK);
        ImageView gifView;
        gifView = findViewById(R.id.gifView);
        Glide.with(this).asGif().load(R.drawable.gif11).into(gifView);
        findViewById(R.id.start).setOnClickListener(v -> {

                    startActivity(new Intent(GetStart.this, MainActivity.class));



        });
        findViewById(R.id.rate).setOnClickListener(v -> {
            GoogleAds.getInstance().showCounterInterstitialAd(GetStart.this, new CustomAdsListener() {
                @Override
                public void onFinish() {
                    AppUtil.rateApp(GetStart.this);
                }
            });

        });
        findViewById(R.id.privacy).setOnClickListener(v -> {
            GoogleAds.getInstance().showCounterInterstitialAd(GetStart.this, new CustomAdsListener() {
                @Override
                public void onFinish() {
                    AppUtil.privacyPolicy(GetStart.this, getString(R.string.privacy_policy));
                }
            });

        });
        findViewById(R.id.share).setOnClickListener(v -> {
            GoogleAds.getInstance().showCounterInterstitialAd(GetStart.this, new CustomAdsListener() {
                @Override
                public void onFinish() {
                    AppUtil.shareApp(GetStart.this);
                }
            });

        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GetStart.this, GetStart2.class));
    }
}