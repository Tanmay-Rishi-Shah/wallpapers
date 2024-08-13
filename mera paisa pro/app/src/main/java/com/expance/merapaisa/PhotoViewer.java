package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Adapter.PhotoAdapter;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.cookie.ClientCookie;

/* loaded from: classes3.dex */
public class PhotoViewer extends AppCompatActivity {
    PhotoAdapter adapter;
    ViewPager viewPager;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(SharePreferenceHelper.getThemeMode(this) == 1 ? R.style.AppThemeNight : R.style.AppTheme);
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (SharePreferenceHelper.getThemeMode(this) == 1) {
//                getWindow().getDecorView().setSystemUiVisibility(0);
//            } else {
//                getWindow().getDecorView().setSystemUiVisibility(8192);
//            }
//        }
        setContentView(R.layout.activity_photo_viewer);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        PhotoAdapter photoAdapter = new PhotoAdapter(this, new ArrayList(Arrays.asList(getIntent().getStringExtra(ClientCookie.PATH_ATTR).split(","))));
        this.adapter = photoAdapter;
        this.viewPager.setAdapter(photoAdapter);
        this.viewPager.setCurrentItem(getIntent().getIntExtra(FirebaseAnalytics.Param.INDEX, 0));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        getWindow().getDecorView().setSystemUiVisibility(5894);
        AppEngine appEngine = (AppEngine) getApplication();
        if (appEngine.wasInBackground()) {
            appEngine.setWasInBackground(false);
            if (SharePreferenceHelper.checkPasscode(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), Passcode.class);
                intent.addFlags(65536);
                intent.addFlags(268435456);
                startActivity(intent);
            }
        }
    }
}
