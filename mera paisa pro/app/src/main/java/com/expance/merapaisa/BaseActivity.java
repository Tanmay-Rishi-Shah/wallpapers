package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

/* loaded from: classes3.dex */
public class BaseActivity extends AppCompatActivity {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$onCreate$0() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen.installSplashScreen(this).setKeepOnScreenCondition(new SplashScreen.KeepOnScreenCondition() { // from class: com.ktwapps.walletmanager.BaseActivity$$ExternalSyntheticLambda0
            @Override // androidx.core.splashscreen.SplashScreen.KeepOnScreenCondition
            public final boolean shouldKeepOnScreen() {
                return BaseActivity.lambda$onCreate$0();
            }
        });
        if (!SharePreferenceHelper.checkAccountKey(this)) {
            startActivity(new Intent(this, WalkThrough.class));
            overridePendingTransition(0, 0);
        } else {
            startActivity(new Intent(this, GetStart2.class));
            overridePendingTransition(0, 0);
            if (getIntent().getAction() != null && getIntent().getAction().equals("shortcut")) {
                startActivity(new Intent(this, CreateTransaction.class));
                overridePendingTransition(0, 0);
            }
            if (getIntent().hasExtra("widget")) {
                Intent intent = new Intent(this, CreateTransaction.class);
                intent.putExtra("widget", getIntent().getStringExtra("widget"));
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }
        finishAffinity();
    }
}
