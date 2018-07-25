package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 07/02/2018.
 */

public class CombatAskerKiMovment {
    LinearLayout lineStep;
    Perso aquene= MainActivity.aquene;
    RadioButton contact,out;
    Context mC;
    public CombatAskerKiMovment(Context mC){
        this.mC=mC;
        lineStep= new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);


        TextView question = questionLayout("Précision de distance :");
        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout contactBox = box();
        LinearLayout outrangeBox = box();

        contact = answerIcon(mC.getDrawable(R.drawable.kirange_selector));
        out = answerIcon(mC.getDrawable(R.drawable.kioutrange_selector));

        contactBox.addView(contact);
        outrangeBox.addView(out);

        answers.addView(contactBox);
        answers.addView(outrangeBox);

        LinearLayout buttonTxt = new LinearLayout(mC);
        buttonTxt.setOrientation(LinearLayout.HORIZONTAL);
        buttonTxt.setPadding(0, 0, 0, (int) mC.getResources().getDimension(R.dimen.margin_combat_asker));

        int atkRange = aquene.getAllAttacks().getAtkRange();
        int tpKi = 120 + 12*aquene.getAbilityScore(mC,"ability_lvl");
        int sum =  atkRange + tpKi;
        TextView contactTxt = summaryText("Moins de " + sum + "m");
        TextView outTxt = summaryText("Plus de " + sum + "m");

        LinearLayout contactBoxTxt = box();
        LinearLayout outrangeBoxTxt = box();

        contactBoxTxt.addView(contactTxt);
        outrangeBoxTxt.addView(outTxt);

        buttonTxt.addView(contactBoxTxt);

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

    public RadioButton getContactButton(){
        return contact;
    }

    public RadioButton getOutButton(){
        return out;
    }

    public LinearLayout getKiMovementLine() {
        return lineStep;
    }
}
