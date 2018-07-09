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

public class CombatAskerRangeLine  {
    private LinearLayout lineStep;
    private Context mC;
    private RadioButton contact;
    private RadioButton mid;
    private RadioButton out;
    Perso aquene = MainActivity.aquene;
    public CombatAskerRangeLine(Context mC){
        this.mC=mC;
        lineStep= new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = questionLayout("À quelle distance est l'ennemi ?");
        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout contactBox = box();
        LinearLayout midBox = box();
        LinearLayout outrangeBox = box();

        contact = answerIcon(mC.getDrawable(R.drawable.contact_selector));
        mid = answerIcon(mC.getDrawable(R.drawable.mid_range_selector));
        out = answerIcon(mC.getDrawable(R.drawable.out_range_selector));

        contactBox.addView(contact);
        midBox.addView(mid);
        outrangeBox.addView(out);

        answers.addView(contactBox);
        answers.addView(midBox);
        answers.addView(outrangeBox);

        LinearLayout buttonTxt = new LinearLayout(mC);
        buttonTxt.setOrientation(LinearLayout.HORIZONTAL);
        buttonTxt.setPadding(0, 0, 0, (int) mC.getResources().getDimension(R.dimen.margin_combat_asker));

        int atkRange = aquene.getAllAttacks().getAtkRange();
        TextView contactTxt = summaryText("Moins de " + atkRange + "m");
        int ms = aquene.getAbilityScore("ability_ms");
        int sum = ms + atkRange;
        TextView midTxt = summaryText("Entre " + atkRange + "m et " + sum + "m");
        TextView outTxt = summaryText("Plus de " + sum + "m");

        LinearLayout contactBoxTxt = box();
        LinearLayout midBoxTxt = box();
        LinearLayout outrangeBoxTxt = box();

        contactBoxTxt.addView(contactTxt);
        midBoxTxt.addView(midTxt);
        outrangeBoxTxt.addView(outTxt);

        buttonTxt.addView(contactBoxTxt);
        buttonTxt.addView(midBoxTxt);
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

    public LinearLayout getRangeLine() {
        return lineStep;
    }

    public RadioButton getContactButton() {
        return contact;
    }

    public RadioButton getMidButton() {
        return mid;
    }

    public RadioButton getOutButton() {
        return out;
    }
}
