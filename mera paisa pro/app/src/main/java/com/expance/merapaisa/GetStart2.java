package com.expance.merapaisa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.ExitScreen;
import vocsy.ads.GoogleAds;

public class GetStart2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start2);
        GoogleAds.getInstance().addNativeView(this, (LinearLayout) findViewById(R.id.nativeLay));
        SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_DARK);
        findViewById(R.id.getStartedButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAds.getInstance().showCounterInterstitialAd(GetStart2.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {
                        startActivity(new Intent(GetStart2.this, GetStart.class));
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GetStart2.this, ExitScreen.class));
    }
}