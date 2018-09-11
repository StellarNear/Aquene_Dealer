package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 13/02/2018.
 */

public class CombatLauncherDamageDetailDialog {
    private List<Roll> rollsToDisplay;
    private AlertDialog dialog;
    private Context mC;
    private View dialogDamageDetail;

    public CombatLauncherDamageDetailDialog(Context mC, List<Roll> rollsToDisplay) {
        this.rollsToDisplay = rollsToDisplay;
        this.mC = mC;
        buildDialog();
    }

    private void buildDialog() {
        LayoutInflater inflater = LayoutInflater.from(mC);
        dialogDamageDetail = inflater.inflate(R.layout.custom_dialog_damage_detail, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mC, R.style.CustomDialog);
        dialogBuilder.setView(dialogDamageDetail);
        dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        dialog = dialogBuilder.create();
        putDicesImgs();
    }

    private void putDicesImgs() {
        LinearLayout linear = dialogDamageDetail.findViewById(R.id.custom_dialog_damage_detail_ScrollLinear);
        linear.removeAllViews();
        for (Roll roll : rollsToDisplay) {
            LinearLayout atkLine = new LinearLayout(mC);
            atkLine.setGravity(Gravity.CENTER);
            if(roll.isCritConfirmed()){atkLine.setBackground(mC.getDrawable(R.drawable.dice_detail_crit_gradient));}
            atkLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
            for (Dice dice : roll.getDmgDiceList(10)) {
                LinearLayout box = box();
                box.addView(dice.getImg());
                atkLine.addView(box);
            }
            TextView bonus = new TextView(mC);
            bonus.setTextSize(20);
            bonus.setGravity(Gravity.CENTER);
            bonus.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            bonus.setText("+"+roll.getDmgBonus());
            LinearLayout boxBonus = box();
            boxBonus.addView(bonus);
            atkLine.addView(boxBonus);

            for (Dice dice : roll.getDmgDiceList(8)) {
                LinearLayout box = box();
                box.addView(dice.getImg());
                atkLine.addView(box);
            }

            for (Dice dice : roll.getDmgDiceList(6)) {
                LinearLayout box = box();
                box.addView(dice.getImg());
                atkLine.addView(box);
            }
            linear.addView(atkLine);
        }
    }
    public void changeCancelButtonToOk() {
        Button onlyButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        onlyButton.setText("Ok");
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));
    }

    private LinearLayout box() {
        LinearLayout box = new LinearLayout(mC);
        box.removeAllViews();
        box.setGravity(Gravity.CENTER);
        box.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        return box;
    }
    public void showDialogManual(){
        TextView title = dialogDamageDetail.findViewById(R.id.custom_dialog_damage_detail_title);
        title.setText("Lancement manuel des dès");
        showDialog();
    }

    public void showDialogDetail(){
        TextView title = dialogDamageDetail.findViewById(R.id.custom_dialog_damage_detail_title);
        title.setText("Détail des dès");

        showDialog();
        changeCancelButtonToOk();
    }

    private void showDialog() {
        dialog.show();
        WindowManager windowManager = (WindowManager) mC.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Float factor = (mC.getResources().getInteger(R.integer.percent_fullscreen_combat_launcher_dialog) - 5) / 100f;
        dialog.getWindow().setLayout((int) (factor * size.x), (int) (factor * size.y));

        Button onlyButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams onlyButtonLL = (LinearLayout.LayoutParams) onlyButton.getLayoutParams();
        onlyButtonLL.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        onlyButton.setLayoutParams(onlyButtonLL);
        onlyButton.setText("Annuler");
        onlyButton.setTextColor(mC.getColor(R.color.colorBackground));
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_cancel_gradient));
    }
}
