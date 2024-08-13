package com.expance.merapaisa.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.Model.Debt;
import com.expance.merapaisa.Model.Goal;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.AdsHelper;
import com.expance.merapaisa.Utility.BudgetHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.RecurringHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class WalletsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    OnItemClickListener listener;
    private final int CREATE_WALLET = 0;
    private final int CREATE_BUDGET = 1;
    private final int CREATE_GOAL = 2;
    private final int CREATE_DEBT = 3;
    private final int CREATE_RECURRING = 4;
    private final int HEADER_WALLET = 5;
    private final int HEADER_BUDGET = 6;
    private final int HEADER_GOAL = 7;
    private final int HEADER_DEBT = 8;
    private final int HEADER_RECURRING = 9;
    private final int HEADER_EMPTY = 10;
    private final int ADS_VIEW = 11;
    private List<Debt> debtList = new ArrayList();
    private List<Budget> budgetList = new ArrayList();
    private List<Wallets> walletList = new ArrayList();
    private List<Goal> goalList = new ArrayList();
    private List<Recurring> recurringList = new ArrayList();
    private List<Object> list = new ArrayList();
    boolean isAdsLoaded = false;
    boolean isAds = false;
    boolean isAdsDecided = false;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type);
    }

    public WalletsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public List<Object> getList() {
        return this.list;
    }

    public void setIsAds(boolean isAds) {
        this.isAdsDecided = true;
        this.isAds = isAds;
    }

    private void generateList() {
        ArrayList arrayList = new ArrayList();
        if (this.isAds && this.isAdsDecided) {
            arrayList.add(11);
        }
        arrayList.add(5);
        arrayList.addAll(this.walletList);
        arrayList.add(0);
        arrayList.add(6);
        arrayList.addAll(this.budgetList);
        arrayList.add(1);
        arrayList.add(7);
        arrayList.addAll(this.goalList);
        arrayList.add(2);
        arrayList.add(8);
        arrayList.addAll(this.debtList);
        arrayList.add(3);
        arrayList.add(9);
        arrayList.addAll(this.recurringList);
        arrayList.add(4);
        arrayList.add(10);
        this.list = arrayList;
        Log.d("asd", String.valueOf(arrayList.size()));
        notifyDataSetChanged();
    }

    public void setDebtList(List<Debt> debtList) {
        this.debtList = debtList;
        generateList();
    }

    public void setBudgetList(List<Budget> budgetList) {
        this.budgetList = budgetList;
        generateList();
    }

    public void setWalletList(List<Wallets> walletList) {
        this.walletList = walletList;
        generateList();
    }

    public void setGoalList(List<Goal> goalList) {
        this.goalList = goalList;
        generateList();
    }

    public void setRecurringList(List<Recurring> recurringList) {
        this.recurringList = recurringList;
        generateList();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new HeaderHolder(this.inflater.inflate(R.layout.list_wallet_item_header, parent, false));
            case 1:
                return new WalletHolder(this.inflater.inflate(R.layout.list_wallet, parent, false));
            case 2:
                return new BudgetHolder(this.inflater.inflate(R.layout.list_wallet_budget, parent, false));
            case 3:
                return new GoalHolder(this.inflater.inflate(R.layout.list_wallet_goal, parent, false));
            case 4:
                return new DebtHolder(this.inflater.inflate(R.layout.list_wallet_debt, parent, false));
            case 5:
                return new RecurringHolder(this.inflater.inflate(R.layout.list_wallet_recurring, parent, false));
            case 6:
                return new CreateHolder(this.inflater.inflate(R.layout.list_wallet_create, parent, false));
            case 7:
                return new CreateWalletHolder(this.inflater.inflate(R.layout.list_wallet_created, parent, false));
            case 8:
            default:
                return new EmptyHolder(this.inflater.inflate(R.layout.list_wallet_empty, parent, false));
            case 9:
                AdsHolder adsHolder = new AdsHolder(this.inflater.inflate(R.layout.list_ad_view, parent, false));
                AdView adView = new AdView(this.context);
                adView.setAdUnitId(this.context.getResources().getString(R.string.ad_unit_budget));
                adsHolder.adView.addView(adView);
                adView.setAdSize(AdsHelper.getAdSize((Activity) this.context));
                adView.loadAd(new AdRequest.Builder().build());
                adView.setAdListener(new AdListener() { // from class: com.ktwapps.walletmanager.Adapter.WalletsAdapter.1
                    @Override // com.google.android.gms.ads.AdListener
                    public void onAdLoaded() {
                        WalletsAdapter.this.isAdsLoaded = true;
                        WalletsAdapter.this.notifyDataSetChanged();
                    }
                });
                return adsHolder;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Context context;
        int i;
        int itemViewType = getItemViewType(position);
        if (itemViewType == 0) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.manageLabel.setText(R.string.manage);
            switch (((Integer) this.list.get(position)).intValue()) {
                case 5:
                    headerHolder.headerLabel.setText(R.string.wallet_capital);
                    if (this.walletList.size() > 0) {
                        headerHolder.manageLabel.setText(this.context.getString(R.string.manage) + "(" + this.walletList.size() + ")");
                        return;
                    }
                    return;
                case 6:
                    headerHolder.headerLabel.setText(R.string.budget_capital);
                    if (this.budgetList.size() > 0) {
                        headerHolder.manageLabel.setText(this.context.getString(R.string.manage) + "(" + this.budgetList.size() + ")");
                        return;
                    }
                    return;
                case 7:
                    headerHolder.headerLabel.setText(R.string.goal_capital);
                    if (this.goalList.size() > 0) {
                        headerHolder.manageLabel.setText(this.context.getString(R.string.manage) + "(" + this.goalList.size() + ")");
                        return;
                    }
                    return;
                case 8:
                    headerHolder.headerLabel.setText(R.string.debt_capital);
                    if (this.debtList.size() > 0) {
                        headerHolder.manageLabel.setText(this.context.getString(R.string.manage) + "(" + this.debtList.size() + ")");
                        return;
                    }
                    return;
                case 9:
                    headerHolder.headerLabel.setText(R.string.recurring_capital);
                    if (this.recurringList.size() > 0) {
                        headerHolder.manageLabel.setText(this.context.getString(R.string.manage) + "(" + this.recurringList.size() + ")");
                        return;
                    }
                    return;
                default:
                    return;
            }
        } else if (itemViewType == 1) {
            WalletHolder walletHolder = (WalletHolder) holder;
            Wallets wallets = (Wallets) this.list.get(position);
            String name = wallets.getName();
            String beautifyAmount = Helper.getBeautifyAmount(DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(wallets.getCurrency())), wallets.getAmount());
            String color = wallets.getColor();
            int intValue = DataHelper.getWalletIcons().get(wallets.getIcon()).intValue();
            walletHolder.walletAccountLabel.setText(name);
            walletHolder.walletAmountLabel.setText(beautifyAmount);
            walletHolder.walletImage.setImageResource(intValue);
            if (Build.VERSION.SDK_INT >= 29) {
                walletHolder.walletWrapper.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
            } else {
                walletHolder.walletWrapper.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
            }
        } else {
            if (itemViewType == 2) {
                BudgetHolder budgetHolder = (BudgetHolder) holder;
                Budget budget = (Budget) this.list.get(position);
                int budgetType = BudgetHelper.getBudgetType(budget.getPeriod());
                int i2 = position - 1;
                if (this.list.get(i2) instanceof Budget) {
                    if (budgetType == BudgetHelper.getBudgetType(((Budget) this.list.get(i2)).getPeriod())) {
                        budgetHolder.dateLabel.setVisibility(8);
                        budgetHolder.dateLabel.setText("");
                    } else {
                        budgetHolder.dateLabel.setVisibility(0);
                        budgetHolder.dateLabel.setText(BudgetHelper.getBudgetPeriod(this.context, budgetType).toUpperCase());
                    }
                } else {
                    budgetHolder.dateLabel.setVisibility(0);
                    budgetHolder.dateLabel.setText(BudgetHelper.getBudgetPeriod(this.context, budgetType).toUpperCase());
                }
                String name2 = budget.getName();
                String accountSymbol = SharePreferenceHelper.getAccountSymbol(this.context);
                String color2 = budget.getColor();
                String beautifyAmount2 = Helper.getBeautifyAmount(accountSymbol, budget.getAmount() - budget.getSpent());
                String beautifyAmount3 = Helper.getBeautifyAmount(accountSymbol, -(budget.getAmount() - budget.getSpent()));
                budgetHolder.nameLabel.setText(name2);
                budgetHolder.amountLabel.setText(budget.getAmount() - budget.getSpent() < 0 ? this.context.getResources().getString(R.string.overspent_amount, beautifyAmount3) : this.context.getResources().getString(R.string.remain_amount, beautifyAmount2));
                budgetHolder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color2)));
                budgetHolder.progressBar.setProgress(Helper.getProgressValue(budget.getAmount(), budget.getSpent()));
            } else if (itemViewType == 3) {
                GoalHolder goalHolder = (GoalHolder) holder;
                Goal goal = (Goal) this.list.get(position);
                String accountSymbol2 = (goal.getCurrency() == null || goal.getCurrency().length() == 0) ? SharePreferenceHelper.getAccountSymbol(this.context) : DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(goal.getCurrency()));
                String name3 = goal.getName();
                String color3 = goal.getColor();
                String string = this.context.getResources().getString(R.string.amount_of_amount, Helper.getBeautifyAmount(accountSymbol2, goal.getSaved()), Helper.getBeautifyAmount(accountSymbol2, goal.getAmount()));
                goalHolder.nameLabel.setText(name3);
                goalHolder.dateLabel.setText(string);
                goalHolder.progressBar.setTextColor(Color.parseColor(color3));
                goalHolder.progressBar.setFinishedStrokeColor(Color.parseColor(color3));
                goalHolder.progressBar.setProgress(Helper.getProgressValue(goal.getAmount(), goal.getSaved()));
            } else if (itemViewType == 4) {
                DebtHolder debtHolder = (DebtHolder) holder;
                Debt debt = (Debt) this.list.get(position);
                String str = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(debt.getCurrencyCode()));
                Resources resources = this.context.getResources();
                int i3 = debt.getType() == 0 ? R.string.i_owe : R.string.owe_me;
                Object[] objArr = new Object[1];
                objArr[0] = (debt.getLender() == null || debt.getLender().length() <= 0) ? this.context.getResources().getString(R.string.someone) : debt.getLender();
                String string2 = resources.getString(i3, objArr);
                String substring = (debt.getLender() == null || debt.getLender().length() <= 0) ? "?" : debt.getLender().substring(0, 1);
                String string3 = (debt.getName() == null || debt.getName().length() <= 0) ? this.context.getString(R.string.no_description) : debt.getName();
                String color4 = debt.getColor();
                String beautifyAmount4 = Helper.getBeautifyAmount(str, debt.getAmount() - debt.getPay());
                if (debt.getName() == null || debt.getName().length() <= 0) {
                    context = this.context;
                    i = R.attr.untitledTextColor;
                } else {
                    context = this.context;
                    i = R.attr.primaryTextColor;
                }
                int attributeColor = Helper.getAttributeColor(context, i);
                debtHolder.circleLabel.setText(substring);
                debtHolder.nameLabel.setText(string2);
                debtHolder.detailLabel.setText(string3);
                debtHolder.detailLabel.setTextColor(attributeColor);
                debtHolder.amountLabel.setText(beautifyAmount4);
                debtHolder.amountLabel.setTextColor(this.context.getResources().getColor(debt.getType() == 0 ? R.color.income : R.color.expense));
                if (Build.VERSION.SDK_INT >= 29) {
                    debtHolder.circleWrapper.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color4), BlendMode.SRC_OVER));
                } else {
                    debtHolder.circleWrapper.getBackground().setColorFilter(Color.parseColor(color4), PorterDuff.Mode.SRC_OVER);
                }
            } else if (itemViewType != 5) {
                if (itemViewType != 6) {
                    if (itemViewType == 9) {
                        ((AdsHolder) holder).adView.setVisibility(this.isAdsLoaded ? 0 : 8);
                        return;
                    }
                    return;
                }
                CreateHolder createHolder = (CreateHolder) holder;
                int intValue2 = ((Integer) this.list.get(position)).intValue();
                if (intValue2 == 1) {
                    createHolder.createLabel.setText(R.string.create_budget);
                } else if (intValue2 == 2) {
                    createHolder.createLabel.setText(R.string.create_saving_goal);
                } else if (intValue2 == 3) {
                    createHolder.createLabel.setText(R.string.create_debt);
                } else if (intValue2 == 4) {
                    createHolder.createLabel.setText(R.string.create_recurring);
                }
            } else {
                RecurringHolder recurringHolder = (RecurringHolder) holder;
                Recurring recurring = (Recurring) this.list.get(position);
                String str2 = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(recurring.getCurrency()));
                long nextOccurrence = RecurringHelper.getNextOccurrence(recurring.getDateTime(), recurring.getLastUpdateTime().getTime(), recurring.getRecurringType(), recurring.getIncrement(), recurring.getRepeatType(), recurring.getRepeatDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(nextOccurrence);
                String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMM yyyy");
                Resources resources2 = this.context.getResources();
                String string4 = resources2.getString(R.string.recurring_occurrence, new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime()) + ", " + new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar.getTime()));
                if (Build.VERSION.SDK_INT >= 29) {
                    recurringHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(recurring.getColor()), BlendMode.SRC_OVER));
                } else {
                    recurringHolder.colorView.getBackground().setColorFilter(Color.parseColor(recurring.getColor()), PorterDuff.Mode.SRC_OVER);
                }
                recurringHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(recurring.getIcon()).intValue());
                recurringHolder.nameLabel.setText(recurring.getNote(this.context));
                recurringHolder.amountLabel.setText(Helper.getBeautifyAmount(str2, recurring.getAmount()));
                recurringHolder.amountLabel.setTextColor(this.context.getResources().getColor(recurring.getType() == 1 ? R.color.expense : R.color.income));
                recurringHolder.detailLabel.setText(string4);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        Object obj = this.list.get(position);
        if (obj instanceof Wallets) {
            return 1;
        }
        if (obj instanceof Budget) {
            return 2;
        }
        if (obj instanceof Goal) {
            return 3;
        }
        if (obj instanceof Debt) {
            return 4;
        }
        if (obj instanceof Recurring) {
            return 5;
        }
        int intValue = ((Integer) obj).intValue();
        if (intValue == 0) {
            return 7;
        }
        if (intValue == 10) {
            return 8;
        }
        if (intValue == 11) {
            return 9;
        }
        return (intValue == 5 || intValue == 6 || intValue == 8 || intValue == 7 || intValue == 9) ? 0 : 6;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    /* loaded from: classes3.dex */
    private class AdsHolder extends RecyclerView.ViewHolder {
        FrameLayout adView;

        AdsHolder(View itemView) {
            super(itemView);
            this.adView = (FrameLayout) itemView.findViewById(R.id.adView);
        }
    }

    /* loaded from: classes3.dex */
    private class HeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView headerLabel;
        TextView manageLabel;

        HeaderHolder(View itemView) {
            super(itemView);
            this.headerLabel = (TextView) itemView.findViewById(R.id.headerLabel);
            TextView textView = (TextView) itemView.findViewById(R.id.manageLabel);
            this.manageLabel = textView;
            textView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (WalletsAdapter.this.listener != null) {
                int intValue = ((Integer) WalletsAdapter.this.list.get(getLayoutPosition())).intValue();
                if (intValue == 5) {
                    WalletsAdapter.this.listener.onItemClick(v, getLayoutPosition(), -21);
                } else if (intValue == 6) {
                    WalletsAdapter.this.listener.onItemClick(v, getLayoutPosition(), -22);
                } else if (intValue == 7) {
                    WalletsAdapter.this.listener.onItemClick(v, getLayoutPosition(), -23);
                } else if (intValue == 8) {
                    WalletsAdapter.this.listener.onItemClick(v, getLayoutPosition(), -24);
                } else if (intValue == 9) {
                    WalletsAdapter.this.listener.onItemClick(v, getLayoutPosition(), -26);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    private class CreateWalletHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout wrapper;

        public CreateWalletHolder(View itemView) {
            super(itemView);
            ConstraintLayout constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.wrapper);
            this.wrapper = constraintLayout;
            constraintLayout.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletsAdapter.this.listener != null) {
                WalletsAdapter.this.listener.onItemClick(view, getLayoutPosition(), 1);
            }
        }
    }

    /* loaded from: classes3.dex */
    private class WalletHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView walletAccountLabel;
        TextView walletAmountLabel;
        ImageView walletImage;
        ConstraintLayout walletWrapper;

        WalletHolder(View itemView) {
            super(itemView);
            this.walletWrapper = (ConstraintLayout) itemView.findViewById(R.id.walletWrapper);
            this.walletImage = (ImageView) itemView.findViewById(R.id.walletImage);
            this.walletAccountLabel = (TextView) itemView.findViewById(R.id.walletAccountLabel);
            this.walletAmountLabel = (TextView) itemView.findViewById(R.id.walletAmountLabel);
            this.walletWrapper.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletsAdapter.this.listener != null) {
                WalletsAdapter.this.listener.onItemClick(view, getLayoutPosition(), 5);
            }
        }
    }

    /* loaded from: classes3.dex */
    private class BudgetHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        ConstraintLayout budgetWrapper;
        TextView dateLabel;
        TextView nameLabel;
        ProgressBar progressBar;

        BudgetHolder(View itemView) {
            super(itemView);
            this.budgetWrapper = (ConstraintLayout) itemView.findViewById(R.id.budgetWrapper);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.dateLabel = (TextView) itemView.findViewById(R.id.dateLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            this.budgetWrapper.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletsAdapter.this.listener != null) {
                WalletsAdapter.this.listener.onItemClick(view, getLayoutPosition(), 6);
            }
        }
    }

    /* loaded from: classes3.dex */
    private class GoalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateLabel;
        TextView nameLabel;
        DonutProgress progressBar;

        GoalHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.dateLabel = (TextView) itemView.findViewById(R.id.dateLabel);
            this.progressBar = (DonutProgress) itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletsAdapter.this.listener != null) {
                WalletsAdapter.this.listener.onItemClick(view, getLayoutPosition(), 7);
            }
        }
    }

    /* loaded from: classes3.dex */
    private class DebtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        TextView circleLabel;
        ConstraintLayout circleWrapper;
        TextView detailLabel;
        TextView nameLabel;

        DebtHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.detailLabel = (TextView) itemView.findViewById(R.id.detailLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.circleLabel = (TextView) itemView.findViewById(R.id.circleLabel);
            this.circleWrapper = (ConstraintLayout) itemView.findViewById(R.id.circleWrapper);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletsAdapter.this.listener != null) {
                WalletsAdapter.this.listener.onItemClick(view, getLayoutPosition(), 8);
            }
        }
    }

    /* loaded from: classes3.dex */
    private class RecurringHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        ConstraintLayout colorView;
        TextView detailLabel;
        ImageView imageView;
        TextView nameLabel;

        RecurringHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.colorView = (ConstraintLayout) itemView.findViewById(R.id.colorView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.detailLabel = (TextView) itemView.findViewById(R.id.detailLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletsAdapter.this.listener != null) {
                WalletsAdapter.this.listener.onItemClick(view, getLayoutPosition(), 9);
            }
        }
    }

    /* loaded from: classes3.dex */
    private class CreateHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView createLabel;

        CreateHolder(View itemView) {
            super(itemView);
            this.createLabel = (TextView) itemView.findViewById(R.id.createLabel);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletsAdapter.this.listener != null) {
                int intValue = ((Integer) WalletsAdapter.this.list.get(getLayoutPosition())).intValue();
                if (intValue == 1) {
                    WalletsAdapter.this.listener.onItemClick(view, getLayoutPosition(), 2);
                } else if (intValue == 2) {
                    WalletsAdapter.this.listener.onItemClick(view, getLayoutPosition(), 3);
                } else if (intValue == 3) {
                    WalletsAdapter.this.listener.onItemClick(view, getLayoutPosition(), 4);
                } else if (intValue == 4) {
                    WalletsAdapter.this.listener.onItemClick(view, getLayoutPosition(), 10);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    private class EmptyHolder extends RecyclerView.ViewHolder {
        EmptyHolder(View itemView) {
            super(itemView);
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
