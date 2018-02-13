package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 13/02/2018.
 */

public class CombatLauncherManualDamage {
    private List<Roll> rollsToDisplay;
    private AlertDialog dialog;
    private Context mC;
    private Activity mA;
    private View dialogManualDamage;

    public CombatLauncherManualDamage(Activity mA, Context mC, List<Roll> rollsToDisplay) {
        this.rollsToDisplay = rollsToDisplay;
        this.mA = mA;
        this.mC = mC;
        buildDialog();
        onChangeDiceListner();
    }

    private void onChangeDiceListner() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        Boolean manualDiceDmg = settings.getBoolean("switch_manual_diceroll_damage", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_damage_DEF));
        for (Roll roll : rollsToDisplay) {
            if (manualDiceDmg) {
                roll.setRefreshEventListener(new Roll.OnRefreshEventListener() {
                    public void onEvent() {
                        //TODO
                    }
                });
            } else {
                roll.setRefreshEventListener(null);
            }
        }
    }

    private void buildDialog() {
        LayoutInflater inflater = mA.getLayoutInflater();
        dialogManualDamage = inflater.inflate(R.layout.custom_dialog_manual_damage, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mA, R.style.CustomDialog);
        dialogBuilder.setView(dialogManualDamage);

        dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        dialog = dialogBuilder.create();

        putDicesImgs();
    }

    private void putDicesImgs() {
        LinearLayout linear = dialogManualDamage.findViewById(R.id.custom_dialog_manual_damage_ScrollLinear);
        linear.removeAllViews();
        for (Roll roll : rollsToDisplay) {
            LinearLayout atkLine = new LinearLayout(mC);
            atkLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            for (ImageView img : roll.getDmgDiceImgList(10)) {
                LinearLayout box = box();
                box.addView(img);
                atkLine.addView(box);
            }
            for (ImageView img : roll.getDmgDiceImgList(8)) {
                LinearLayout box = box();
                box.addView(img);
                atkLine.addView(box);
            }
            for (ImageView img : roll.getDmgDiceImgList(6)) {
                LinearLayout box = box();
                box.addView(img);
                atkLine.addView(box);
            }
            linear.addView(atkLine);
        }
    }

    private LinearLayout box() {
        LinearLayout box = new LinearLayout(mC);
        box.removeAllViews();
        box.setBackgroundColor(Color.GREEN);
        box.setGravity(Gravity.CENTER);
        box.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        return box;
    }

    public void showDialog() {
        dialog.show();
        Display display = mA.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Float factor = (mC.getResources().getInteger(R.integer.percent_fullscreen_combat_launcher_dialog) - 5) / 100f;
        dialog.getWindow().setLayout((int) (factor * size.x), (int) (factor * size.y));

        Button onlyButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams onlyButtonLL = (LinearLayout.LayoutParams) onlyButton.getLayoutParams();
        onlyButtonLL.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        onlyButton.setLayoutParams(onlyButtonLL);
        onlyButton.setTextColor(mC.getColor(R.color.colorBackground));
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_cancel_gradient));
    }

    private void changeCancelButtonToOk() {
        Button onlyButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        onlyButton.setText("Ok");
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));
    }

}
