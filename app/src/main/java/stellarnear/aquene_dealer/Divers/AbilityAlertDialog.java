package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import stellarnear.aquene_dealer.Perso.Ability;
import stellarnear.aquene_dealer.Perso.Skill;
import stellarnear.aquene_dealer.R;


public class AbilityAlertDialog {
    private Activity mA;
    private Context mC;
    private AlertDialog alertDialog;
    private AlertDialog alertDialogWheelPicker;
    private WheelDicePicker wheelPicker;
    private Ability abi;
    private View dialogView;
    private View dialogViewWheelPicker;

    public AbilityAlertDialog(Activity mA, Context mC, Ability abi) {
        this.mA=mA;
        this.mC=mC;
        this.abi=abi;
        buildAlertDialogSkill();
    }

    private void buildAlertDialogSkill() {
        LayoutInflater inflater = mA.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.custom_dialog_skill, null);
        ImageView icon = dialogView.findViewById(R.id.dialogSkillTestSkillIcon);
        int imgId = mC.getResources().getIdentifier(abi.getId(), "drawable", mC.getPackageName());
        icon.setImageDrawable(mC.getDrawable(imgId));
        String titleTxt = "Test de la compétence :\n"+abi.getName();
        SpannableString titleSpan = new SpannableString(titleTxt);
        titleSpan.setSpan(new RelativeSizeSpan(2.0f)  ,titleTxt.length()-abi.getName().length(),titleTxt.length(),0);
        TextView title = dialogView.findViewById(R.id.dialogSkillTestSkillTitle);
        title.setSingleLine(false);
        title.setText(titleSpan);

        TextView summary = dialogView.findViewById(R.id.dialogSkillTestSkillSummary);
        String abScore;
        /*
        if(modBonus>=0){
            abScore = "+"+modBonus;
        } else {
            abScore = "-"+modBonus;
        }

        String summaryTxt="Abilité ("+abi.getAbilityDependence()+") : "+abScore+",  Maîtrise : "+skill.getRank()+",  Bonus : "+skill.getBonus();
        summary.setText(summaryTxt);*/

        Button diceroll = dialogView.findViewById(R.id.button_skill_test_diceroll);
        diceroll.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
                if (settings.getBoolean("switch_manual_diceroll",mC.getResources().getBoolean(R.bool.switch_manual_dicerollDEF))) {
                    buildAlertDialogWheelPicker();
                    showAlertDialogWheelPicker();
                } else {
                    Random rand = new Random();
                    endSkillCalculation(1+rand.nextInt(20));
                }
            }
        });

        Button passive = dialogView.findViewById(R.id.button_skill_test_passive);
        passive.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSkillCalculation(10);
            }
        });

        Button focus = dialogView.findViewById(R.id.button_skill_test_focus);
        focus.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSkillCalculation(20);
            }
        });


        AlertDialog.Builder dialogBuilder  = new AlertDialog.Builder(mA, R.style.CustomDialog);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked cancel button
            }
        });
        alertDialog = dialogBuilder.create();
    }

    public void showAlertDialog(){
        alertDialog.show();
        Display display = mA.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Float factor = mC.getResources().getInteger(R.integer.percent_fullscreen_customdialog)/100f;
        alertDialog.getWindow().setLayout((int) (factor*size.x), (int)(factor*size.y));
        Button onlyButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams onlyButtonLL = (LinearLayout.LayoutParams) onlyButton.getLayoutParams();
        onlyButtonLL.width=ViewGroup.LayoutParams.WRAP_CONTENT;
        onlyButton.setLayoutParams(onlyButtonLL);
        onlyButton.setTextColor(mC.getColor(R.color.colorBackground));
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_cancel_gradient));
    }

    private void buildAlertDialogWheelPicker() {
        LayoutInflater inflater = mA.getLayoutInflater();
        dialogViewWheelPicker = inflater.inflate(R.layout.custom_dialog_wheel_picker, null);
        RelativeLayout relativeCenter =  dialogViewWheelPicker.findViewById(R.id.relative_custom_dialog_center);
        wheelPicker = new WheelDicePicker(relativeCenter,20,mC);
        AlertDialog.Builder dialogBuilder  = new AlertDialog.Builder(mA, R.style.CustomDialog);
        dialogBuilder.setView(dialogViewWheelPicker);
        dialogBuilder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                alertDialog.show();
                endSkillCalculation(wheelPicker.getValue_selected());
            }
        });
        dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                alertDialog.show();
            }
        });
        alertDialogWheelPicker = dialogBuilder.create();

    }



    private void showAlertDialogWheelPicker(){
        alertDialog.hide();
        alertDialogWheelPicker.show();
        Display display = mA.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Float factor = mC.getResources().getInteger(R.integer.percent_fullscreen_customdialog)/100f;
        alertDialogWheelPicker.getWindow().setLayout((int) (factor*size.x), (int)(factor*size.y));

        Button positiveButton = alertDialogWheelPicker.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.width=ViewGroup.LayoutParams.WRAP_CONTENT;
        positiveButtonLL.setMargins(mC.getResources().getDimensionPixelSize(R.dimen.general_margin),0,0,0);
        positiveButton.setLayoutParams(positiveButtonLL);
        positiveButton.setTextColor(mC.getColor(R.color.colorBackground));
        positiveButton.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));

        Button negativeButton = alertDialogWheelPicker.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        negativeButtonLL.width=ViewGroup.LayoutParams.WRAP_CONTENT;
        negativeButton.setLayoutParams(negativeButtonLL);
        negativeButton.setTextColor(mC.getColor(R.color.colorBackground));
        negativeButton.setBackground(mC.getDrawable(R.drawable.button_cancel_gradient));

    }


    private void endSkillCalculation(int value_selected) {
        ImageView resultDice= dialogView.findViewById(R.id.dialogSkillTestResultDice);
        int idResultDice = mC.getResources().getIdentifier("d20_"+value_selected, "drawable", mC.getPackageName());
        resultDice.setImageDrawable(mC.getDrawable(idResultDice));

        TextView resultTitle = dialogView.findViewById(R.id.dialogSkillTitleResult);
        resultTitle.setText("Résultat du test de compétence :");

        //int sumResult=value_selected+skill.getRank()+skill.getBonus()+ modBonus;

        TextView result = dialogView.findViewById(R.id.dialogSkillTestResult);
        //result.setText(String.valueOf(sumResult));

        TextView callToAction = dialogView.findViewById(R.id.dialogSkillTestCallToAction);
        callToAction.setText("Fin du test de compétence");
        callToAction.setTextColor(mC.getColor(R.color.secondaryTextSkill));

        Button onlyButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        onlyButton.setText("Ok");
        onlyButton.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));

    }
}