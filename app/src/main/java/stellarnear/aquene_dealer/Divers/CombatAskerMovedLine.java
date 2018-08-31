package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 07/02/2018.
 */

public class CombatAskerMovedLine {
    private Context mC;
    private LinearLayout lineStep;
    private RadioButton charge;
    private RadioButton walk;
    private RadioButton no;

    public CombatAskerMovedLine(Context mC, Boolean chargerange) {
        this.mC = mC;
        lineStep = new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = questionLayout("Es-ce que tu peux te déplacer ?");
        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout walkBox = box();
        LinearLayout noBox = box();

        walk = answerIcon(mC.getDrawable(R.drawable.walking_selector));
        no = answerIcon(mC.getDrawable(R.drawable.notmoving_selector));

        walkBox.addView(walk);
        noBox.addView(no);

        LinearLayout chargeBox = box();
        charge = answerIcon(mC.getDrawable(R.drawable.charging_selector));
        if (chargerange) {
            chargeBox.addView(charge);
            answers.addView(chargeBox);
        }

        answers.addView(walkBox);
        answers.addView(noBox);

        LinearLayout buttonTxt = new LinearLayout(mC);
        buttonTxt.setOrientation(LinearLayout.HORIZONTAL);
        buttonTxt.setPadding(0, 0, 0, (int) mC.getResources().getDimension(R.dimen.margin_combat_asker));

        TextView walkTxt = summaryText("Je peux marcher");
        TextView noTxt = summaryText("Je peux plus...");


        LinearLayout walkBoxTxt = box();
        LinearLayout noBoxTxt = box();


        walkBoxTxt.addView(walkTxt);
        noBoxTxt.addView(noTxt);


        TextView chargeTxt = summaryText("Je peux charger !");
        LinearLayout chargeBoxTxt = box();
        if (chargerange) {
            chargeBoxTxt.addView(chargeTxt);
            buttonTxt.addView(chargeBoxTxt);
        }

        buttonTxt.addView(walkBoxTxt);
        buttonTxt.addView(noBoxTxt);

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

    public RadioButton getChargeButton() {
        return charge;
    }

    public RadioButton getWalkButton() {
        return walk;
    }

    public RadioButton getNoButton() {
        return no;
    }

    public LinearLayout getMovedLine() {
        return lineStep;
    }
}
