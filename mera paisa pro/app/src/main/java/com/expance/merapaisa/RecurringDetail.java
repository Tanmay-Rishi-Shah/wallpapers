package com.expance.merapaisa;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.ViewModel.ModelFactory;
import com.expance.merapaisa.Database.ViewModel.RecurringDetailViewModel;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.RecurringHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: classes3.dex */
public class RecurringDetail extends AppCompatActivity {
    TextView amountLabel;
    TextView categoryLabel;
    View colorView;
    ImageView imageView;
    TextView nameLabel;
    Recurring recurring;
    RecurringDetailViewModel recurringDetailViewModel;
    int recurringId;
    TextView repeatLabel;
    TextView typeLabel;
    TextView walletLabel;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
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
//        if (Build.VERSION.SDK_INT >= 26) {
//            if (SharePreferenceHelper.getThemeMode(this) == 1) {
//                getWindow().getDecorView().setSystemUiVisibility(0);
//            } else {
//                getWindow().getDecorView().setSystemUiVisibility(8208);
//            }
//            getWindow().setNavigationBarColor(Color.parseColor(SharePreferenceHelper.getThemeMode(this) == 1 ? "#1F1F1F" : "#F8F8F8"));
//        } else {
//            getWindow().setNavigationBarColor(Color.parseColor("#000000"));
//        }
        setContentView(R.layout.activity_recurring_detail);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
//        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.recurringId = getIntent().getIntExtra("recurringId", 0);
        setUpLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
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

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.recurring);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.nameLabel = (TextView) findViewById(R.id.nameLabel);
        this.categoryLabel = (TextView) findViewById(R.id.categoryLabel);
        this.amountLabel = (TextView) findViewById(R.id.amountLabel);
        this.walletLabel = (TextView) findViewById(R.id.walletLabel);
        this.colorView = findViewById(R.id.colorView);
        this.typeLabel = (TextView) findViewById(R.id.typeLabel);
        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.repeatLabel = (TextView) findViewById(R.id.repeatLabel);
        RecurringDetailViewModel recurringDetailViewModel = (RecurringDetailViewModel) new ViewModelProvider(this, new ModelFactory(getApplication(), this.recurringId)).get(RecurringDetailViewModel.class);
        this.recurringDetailViewModel = recurringDetailViewModel;
        recurringDetailViewModel.getRecurring().observe(this, new Observer<Recurring>() { // from class: com.ktwapps.walletmanager.RecurringDetail.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(Recurring recurring) {
                if (recurring != null) {
                    RecurringDetail.this.recurring = recurring;
                    String note = recurring.getNote(RecurringDetail.this.getApplicationContext());
                    String category = recurring.getCategory(RecurringDetail.this.getApplicationContext());
                    String beautifyAmount = Helper.getBeautifyAmount(DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(recurring.getCurrency())), recurring.getAmount());
                    String type = RecurringDetail.this.getType(recurring.getType());
                    String wallet = recurring.getWallet();
                    String color = recurring.getColor();
                    java.util.Calendar.getInstance().setTimeInMillis(RecurringHelper.getNextOccurrence(recurring.getDateTime(), recurring.getLastUpdateTime().getTime(), recurring.getRecurringType(), recurring.getIncrement(), recurring.getRepeatType(), recurring.getRepeatDate()));
                    RecurringDetail.this.repeatLabel.setText(RecurringDetail.this.getRecurringType(recurring.getRecurringType()));
                    RecurringDetail.this.amountLabel.setTextColor(RecurringDetail.this.getAmountColor(recurring.getType()));
                    RecurringDetail.this.nameLabel.setText(note);
                    RecurringDetail.this.categoryLabel.setText(category);
                    RecurringDetail.this.amountLabel.setText(beautifyAmount);
                    RecurringDetail.this.walletLabel.setText(wallet);
                    RecurringDetail.this.typeLabel.setText(type);
                    RecurringDetail.this.imageView.setImageResource(DataHelper.getCategoryIcons().get(recurring.getIcon()).intValue());
                    RecurringDetail.this.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getRecurringType(int type) {
        if (type != 1) {
            if (type != 2) {
                if (type == 3) {
                    return getResources().getString(R.string.monthly);
                }
                return getResources().getString(R.string.yearly);
            }
            return getResources().getString(R.string.weekly);
        }
        return getResources().getString(R.string.daily);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getAmountColor(int type) {
        if (type == 1) {
            return getResources().getColor(R.color.expense);
        }
        return getResources().getColor(R.color.income);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getType(int type) {
        if (type == 1) {
            return getResources().getString(R.string.expense);
        }
        return getResources().getString(R.string.income);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_delete) {
            deleteRecurring();
            return true;
        } else if (item.getItemId() == R.id.menu_action_edit) {
            Intent intent = new Intent(getApplicationContext(), CreateRecurring.class);
            intent.putExtra(JamXmlElements.TYPE, -10);
            intent.putExtra("recurringId", this.recurringId);
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            return false;
        } else {
            return false;
        }
    }

    private void deleteRecurring() {
        Helper.showDialog(this, "", getResources().getString(R.string.recurring_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new AnonymousClass2());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.RecurringDetail$2  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements DialogInterface.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.RecurringDetail.2.1
                @Override // java.lang.Runnable
                public void run() {
                    AppDatabaseObject.getInstance(RecurringDetail.this.getApplicationContext()).recurringDaoObject().deleteRecurring(RecurringDetail.this.recurringId);
                    RecurringDetail.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.RecurringDetail.2.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            RecurringDetail.this.finish();
                            RecurringDetail.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                        }
                    });
                }
            });
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }
}
