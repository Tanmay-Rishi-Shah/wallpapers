package com.expance.merapaisa.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;

import com.expance.merapaisa.CreateDebt;
import com.expance.merapaisa.CreateDebtTrans;
import com.expance.merapaisa.EditTransaction;
import com.expance.merapaisa.Model.Trans;
import com.expance.merapaisa.R;

import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.lang.reflect.Field;

/* loaded from: classes3.dex */
public class TransactionHelper {
    public static void showPopupMenu(final Context context, final Trans trans, View view) {
        PopupMenu popupMenu = new PopupMenu(context, view, GravityCompat.END);
        popupMenu.inflate(R.menu.menu_detail);
        forcePopupIcon(context, popupMenu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.ktwapps.walletmanager.Utility.TransactionHelper.1
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_action_delete) {
                    TransactionHelper.delete(context, trans);
                    return false;
                } else if (item.getItemId() == R.id.menu_action_edit) {
                    TransactionHelper.edit(context, trans);
                    return false;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void delete(final Context context, final Trans trans) {
        if (trans.getDebtId() == 0) {
            Helper.showDialog(context, "", context.getResources().getString(R.string.transaction_delete_message), context.getResources().getString(R.string.delete_positive), context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.Utility.TransactionHelper.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    DatabaseTaskHelper.deleteTransaction(context, trans);
                }
            });
        } else if (trans.getDebtTransId() != 0) {
            Helper.showDialog(context, "", context.getResources().getString(R.string.transaction_delete_message), context.getResources().getString(R.string.delete_positive), context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.Utility.TransactionHelper.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    DatabaseTaskHelper.deleteDebtTrans(context, trans.getId(), trans.getDebtId(), trans.getDebtTransId());
                }
            });
        } else {
            Helper.showDialog(context, "", context.getResources().getString(R.string.debt_delete_message), context.getResources().getString(R.string.delete_positive), context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.Utility.TransactionHelper.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    DatabaseTaskHelper.deleteDebt(context, trans.getDebtId());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void edit(Context context, Trans trans) {
        if (trans.getDebtId() == 0) {
            Intent intent = new Intent(context, EditTransaction.class);
            intent.putExtra("transId", trans.getId());
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else if (trans.getDebtTransId() != 0) {
            Intent intent2 = new Intent(context, CreateDebtTrans.class);
            intent2.putExtra("debtId", trans.getDebtId());
            intent2.putExtra("debtType", trans.getDebtType());
            intent2.putExtra("debtTransId", trans.getDebtTransId());
            intent2.putExtra("isFromTransaction", true);
            intent2.putExtra(JamXmlElements.TYPE, 1);
            context.startActivity(intent2);
            ((Activity) context).overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else {
            Intent intent3 = new Intent(context, CreateDebt.class);
            intent3.putExtra(JamXmlElements.TYPE, -2);
            intent3.putExtra("debtId", trans.getDebtId());
            context.startActivity(intent3);
            ((Activity) context).overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        }
    }

    private static void forcePopupIcon(Context var0, PopupMenu var1) {
        byte var3 = 0;

        int var2;
        label58:
        {
            Exception var10000;
            label63:
            {
                int var4;
                Field[] var5;
                boolean var10001;
                try {
                    var5 = var1.getClass().getDeclaredFields();
                    var4 = var5.length;
                } catch (Exception var8) {
                    var10000 = var8;
                    var10001 = false;
                    break label63;
                }

                var2 = 0;

                while (true) {
                    if (var2 >= var4) {
                        break label58;
                    }

                    Field var6 = var5[var2];

                    Object var10;
                    label53:
                    {
                        try {
                            if ("mPopup".equals(var6.getName())) {
                                var6.setAccessible(true);
                                var10 = var6.get(var1);
                                break label53;
                            }
                        } catch (Exception var9) {
                            var10000 = var9;
                            var10001 = false;
                            break;
                        }

                        ++var2;
                        continue;
                    }

                    var2 = var3;
                    if (var10 == null) {
                        break label58;
                    }

                    try {
                        Class.forName(var10.getClass().getName()).getMethod("setForceShowIcon", Boolean.TYPE).invoke(var10, Boolean.TRUE);
                    } catch (Exception var7) {
                        var10000 = var7;
                        var10001 = false;
                        break;
                    }

                    var2 = var3;
                    break label58;
                }
            }

            Exception var11 = var10000;
            var11.printStackTrace();
            var2 = var3;
        }

        for (var2 = var3; var2 < var1.getMenu().size(); ++var2) {
            Drawable var12 = var1.getMenu().getItem(var2).getIcon();
            if (var12 != null) {
                var12.mutate();
                var12.setColorFilter(Helper.getAttributeColor(var0, 2130903968), PorterDuff.Mode.SRC_ATOP);
            }
        }

    }
}
