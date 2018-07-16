package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.R;

public class CustomAlertDialog {
    private AlertDialog alert;
    private Context mC;
    private boolean permanent=false;
    public CustomAlertDialog(Activity mA, Context mC, View view) {
        // Set the toast and duration
        this.mC=mC;
        AlertDialog.Builder dialogBuilder  = new AlertDialog.Builder(mA,R.style.CustomDialog);
        dialogBuilder.setView(view);
        alert = dialogBuilder.create();
    }

    public void showAlert() {
        alert.show();
        setTimer();
    }

    private void setTimer() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        if (settings.getBoolean("switch_autoclose_dialog",mC.getResources().getBoolean(R.bool.switch_autoclose_dialog_DEF)) && !permanent){
            int duration = new Tools().toInt(settings.getString("custom_alert_long_duration",String.valueOf(mC.getResources().getInteger(R.integer.custom_alert_long_duration_DEF))));
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideToast();
                }
            }, duration);
        }
    }

    private void hideToast() {
        alert.dismiss();
    }

    public void clickToHide(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideToast();
            }
        });
    }

    public void setPermanent(boolean b) {
        this.permanent=b;
    }
}
