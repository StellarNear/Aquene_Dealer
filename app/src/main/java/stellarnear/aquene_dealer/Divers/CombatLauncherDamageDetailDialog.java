package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.List;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 13/02/2018.
 */

public class CombatLauncherDamageDetailDialog {
    private List<Roll> rollsToDisplay;
    private AlertDialog dialog;
    private Context mC;
    private Activity mA;
    private View dialogDamageDetail;
    private boolean constructed = false;
    private Tools tools = new Tools();
    Perso aquene = MainActivity.aquene;

    public CombatLauncherDamageDetailDialog(Activity mA, Context mC, List<Roll> rollsToDisplay) {
        this.rollsToDisplay = rollsToDisplay;
        this.mA = mA;
        this.mC = mC;
        buildDialog();
    }

    private void buildDialog() {
        LayoutInflater inflater = mA.getLayoutInflater();
        dialogDamageDetail = inflater.inflate(R.layout.custom_dialog_damage_detail, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mA, R.style.CustomDialog);
        dialogBuilder.setView(dialogDamageDetail);
        dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        ImageButton summary = dialogDamageDetail.findViewById(R.id.fab_damage_detail_info_summary);
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int critMutliplier=2;
                if (aquene.mythicFeatIsActive("mythicfeat_crit_sup")){  critMutliplier+=1;  }
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
                String nDice = settings.getString("number_main_dice_dmg", String.valueOf(mC.getResources().getInteger(R.integer.number_main_dice_dmg_DEF)));
                int bonusDamage=0;
                if (!rollsToDisplay.isEmpty()){ bonusDamage=rollsToDisplay.get(0).getDmgBonus(); }
                tools.customToast(mC, "Les lancées surlignés en rouge correpondent à des coups critiques.\nLes dégats des coups critiques voient leurs dégats physique de base (dégats des poings["+String.valueOf(nDice)+"d10] + bonus dégats["+String.valueOf(bonusDamage)+"]) multiplié par "+String.valueOf(critMutliplier)+".", "center");
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
            if (roll.isCritConfirmed()) {
                atkLine.setBackground(mC.getDrawable(R.drawable.dice_detail_crit_gradient));
            }
            atkLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            for (ImageView img : roll.getDmgDiceImgList(10)) {
                LinearLayout box = box();
                box.addView(img);
                atkLine.addView(box);
            }
            TextView bonus = new TextView(mC);
            bonus.setTextSize(20);
            bonus.setGravity(Gravity.CENTER);
            bonus.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            bonus.setText("+" + roll.getDmgBonus());
            LinearLayout boxBonus = box();
            boxBonus.addView(bonus);
            atkLine.addView(boxBonus);

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

    public void showDialogManual() {
        TextView title = dialogDamageDetail.findViewById(R.id.custom_dialog_damage_detail_title);
        title.setText("Lancement manuel des dès");
        showDialog();
    }

    public void showDialogDetail() {
        TextView title = dialogDamageDetail.findViewById(R.id.custom_dialog_damage_detail_title);
        title.setText("Détail des dès");

        showDialog();
        changeCancelButtonToOk();
    }

    private void showDialog() {
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
        onlyButton.setText("Annuler");
        onlyButton.setTextColor(mC.getColor(R.color.colorBackground));
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_cancel_gradient));
    }
}
