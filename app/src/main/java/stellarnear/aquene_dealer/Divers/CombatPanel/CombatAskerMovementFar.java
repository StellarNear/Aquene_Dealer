package stellarnear.aquene_dealer.Divers.CombatPanel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Equipment;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 07/02/2018.
 */

public class CombatAskerMovementFar {
    Perso aquene= MainActivity.aquene;
    private LinearLayout lineStep;
    private RadioButton chargeRange,kiRange,out;
    private Context mC;
    public CombatAskerMovementFar(Context mC){
        this.mC=mC;
        lineStep= new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);


        TextView question = questionLayout("Précision de distance :");
        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout chargeRangeBox = box();
        LinearLayout kiRangeBox = box();
        LinearLayout outrangeBox = box();

        chargeRange = answerIcon(mC.getDrawable(R.drawable.chargerange_selector));
        kiRange = answerIcon(mC.getDrawable(R.drawable.kirange_selector));
        out = answerIcon(mC.getDrawable(R.drawable.kioutrange_selector));

        chargeRangeBox.addView(chargeRange);
        kiRangeBox.addView(kiRange);
        outrangeBox.addView(out);

        answers.addView(chargeRangeBox);
        answers.addView(kiRangeBox);
        answers.addView(outrangeBox);

        LinearLayout buttonTxt = new LinearLayout(mC);
        buttonTxt.setOrientation(LinearLayout.HORIZONTAL);
        buttonTxt.setPadding(0, 0, 0, (int) mC.getResources().getDimension(R.dimen.margin_combat_asker));

        int atkRange = aquene.getAllAttacks().getAtkRange();
        Equipment head = aquene.getInventory().getAllEquipments().getEquipmentsEquiped("helm_slot");
        int runMultiplier = (head!= null && head.getName().equalsIgnoreCase("oreilles de lapin") ? 8:4);
        int chargeRangeMeter = aquene.getAbilityScore(mC,"ability_ms")*runMultiplier + atkRange;
        int ms = aquene.getAbilityScore(mC,"ability_ms");
        if(aquene.featIsActive("feat_void_step")){
            chargeRangeMeter+=ms;
        }

        int tpKi = 120 + 12*aquene.getAbilityScore(mC,"ability_lvl");
        int sum =  atkRange + tpKi;

        TextView chargeRangeTxt = summaryText("Moins de " + chargeRangeMeter + "m");
        TextView kiRangeTxt = summaryText("Entre "+ chargeRangeMeter +"m et "+ sum + "m");
        TextView outTxt = summaryText("Plus de " + sum + "m");

        chargeRangeTxt.setSingleLine(true);
        kiRangeTxt.setSingleLine(true);
        outTxt.setSingleLine(true);

        LinearLayout chargeRangeBoxTxt = box();
        LinearLayout kiRangeBoxTxt = box();
        LinearLayout outrangeBoxTxt = box();

        chargeRangeBoxTxt.addView(chargeRangeTxt);
        kiRangeBoxTxt.addView(kiRangeTxt);
        outrangeBoxTxt.addView(outTxt);

        buttonTxt.addView(chargeRangeBoxTxt);
        buttonTxt.addView(kiRangeBoxTxt);
        buttonTxt.addView(outrangeBoxTxt);

        lineStep.addView(answers);
        lineStep.addView(buttonTxt);
    }
    private TextView summaryText(String s) {
        TextView sumamrTxt = new TextView(mC);
        sumamrTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sumamrTxt.setGravity(Gravity.CENTER);
        sumamrTxt.setText(s);
        return sumamrTxt;
    }

    private RadioButton answerIcon(Drawable drawable) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = (int) mC.getResources().getDimension(R.dimen.combat_answer_icons);
        params.height = (int) mC.getResources().getDimension(R.dimen.combat_answer_icons);

        RadioButton answerRadioButton = new RadioButton(mC);
        answerRadioButton.setButtonDrawable(null);
        answerRadioButton.setBackground(drawable);
        answerRadioButton.setLayoutParams(params);
        answerRadioButton.setGravity(Gravity.CENTER);
        return answerRadioButton;
    }

    private LinearLayout box() {
        LinearLayout box = new LinearLayout(mC);
        box.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        box.setGravity(Gravity.CENTER);
        return box;
    }

    private TextView questionLayout(String s) {
        TextView question = new TextView(mC);
        question.setGravity(Gravity.CENTER);
        question.setText(s);
        question.setTextSize(20);
        question.setBackgroundColor(mC.getColor(R.color.title_question_combat_back));
        question.setTextColor(mC.getColor(R.color.title_question_combat_text));
        return question;
    }

    public RadioButton getChargeButton(){
        return chargeRange;
    }

    public RadioButton getKiRangeButton(){
        return kiRange;
    }

    public RadioButton getOutrangeButton(){
        return out;
    }

    public LinearLayout getKiMovementLine() {
        return lineStep;
    }
}
