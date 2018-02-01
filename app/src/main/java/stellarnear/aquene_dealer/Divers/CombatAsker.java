package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Attack;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class CombatAsker {
    private Perso aquene = MainActivity.aquene;
    private Context mC;
    private int lastStep;
    private LinearLayout layout;
    ArrayList<View> stepsView = new ArrayList<View>();

    private boolean moved;
    private boolean range;
    private boolean outrange;

    public CombatAsker(Context mC, LinearLayout layout) {
        this.mC = mC;
        this.layout = layout;
        buildMovedLine();
    }

    private void buildMovedLine() {

        LinearLayout lineStep = new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = new TextView(mC);
        question.setGravity(Gravity.CENTER);
        question.setText("T'es tu déplacé au cours de ce round ?");
        question.setTextSize(20);
        question.setBackgroundColor(mC.getColor(R.color.title_question_combat_back));
        question.setTextColor(mC.getColor(R.color.title_question_combat_text));

        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setWeightSum(2);
        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout yesBox = new LinearLayout(mC);
        LinearLayout noBox = new LinearLayout(mC);
        yesBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        noBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        yesBox.setGravity(Gravity.CENTER);
        noBox.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = (int) mC.getResources().getDimension(R.dimen.combat_answer_icons);
        params.height = (int) mC.getResources().getDimension(R.dimen.combat_answer_icons);

        RadioButton yes = new RadioButton(mC);
        yes.setButtonDrawable(null);
        yes.setBackground(mC.getDrawable(R.drawable.moving_selector));
        yes.setLayoutParams(params);

        RadioButton no = new RadioButton(mC);
        no.setButtonDrawable(null);
        no.setBackground(mC.getDrawable(R.drawable.notmoving_selector));
        no.setLayoutParams(params);

        setRadioButtonListnerMoved(yes, no);
        yesBox.addView(yes);
        noBox.addView(no);

        answers.addView(yesBox);
        answers.addView(noBox);

        LinearLayout buttonTxt = new LinearLayout(mC);
        buttonTxt.setOrientation(LinearLayout.HORIZONTAL);
        buttonTxt.setPadding(0, 0, 0, (int) mC.getResources().getDimension(R.dimen.margin_combat_asker));

        TextView yesTxt = new TextView(mC);
        yesTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        yesTxt.setGravity(Gravity.CENTER);
        yesTxt.setText("Oui j'ai bougé");
        TextView noTxt = new TextView(mC);
        noTxt.setText("Non je n'ai rien fais");
        noTxt.setGravity(Gravity.CENTER);
        noTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        buttonTxt.addView(yesTxt);
        buttonTxt.addView(noTxt);

        lineStep.addView(answers);
        lineStep.addView(buttonTxt);

        stepsView.add(lineStep);
        getLayout();
    }

    private void setRadioButtonListnerMoved(final RadioButton yes, final RadioButton no) {
        final List<RadioButton> listRadio = Arrays.asList(yes, no);
        for (final RadioButton check : listRadio) {
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (RadioButton checkToUnselect : listRadio) {
                            if (!checkToUnselect.equals(check)) {
                                checkToUnselect.setChecked(false);
                            }
                        }
                        if (check.equals(yes)) {
                            moved = true;
                        } else {
                            moved = false;
                        }
                        buildRangeLine();
                    }
                }
            });
        }
    }

    private void clearStep(int rankStep) {

        while (stepsView.size() > rankStep) {
            stepsView.remove(stepsView.size() - 1);
        }
    }

    private void buildRangeLine() {
        clearStep(1);
        LinearLayout lineStep = new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = new TextView(mC);
        question.setGravity(Gravity.CENTER);
        question.setText("À quelle distance est l'ennemi ?");
        question.setTextSize(20);
        question.setBackgroundColor(mC.getColor(R.color.title_question_combat_back));
        question.setTextColor(mC.getColor(R.color.title_question_combat_text));
        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setWeightSum(3);
        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout contactBox = new LinearLayout(mC);
        LinearLayout midBox = new LinearLayout(mC);
        LinearLayout outrangeBox = new LinearLayout(mC);
        contactBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        midBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        outrangeBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        contactBox.setGravity(Gravity.CENTER);
        midBox.setGravity(Gravity.CENTER);
        outrangeBox.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = (int) mC.getResources().getDimension(R.dimen.combat_answer_icons);
        params.height = (int) mC.getResources().getDimension(R.dimen.combat_answer_icons);

        RadioButton contact = new RadioButton(mC);
        contact.setButtonDrawable(null);
        contact.setBackground(mC.getDrawable(R.drawable.contact_selector));
        contact.setLayoutParams(params);

        RadioButton mid = new RadioButton(mC);
        mid.setButtonDrawable(null);
        mid.setBackground(mC.getDrawable(R.drawable.mid_range_selector));
        mid.setLayoutParams(params);

        RadioButton out = new RadioButton(mC);
        out.setButtonDrawable(null);
        out.setBackground(mC.getDrawable(R.drawable.out_range_selector));
        out.setLayoutParams(params);

        setRadioButtonListnerRange(contact, mid, out);
        contactBox.addView(contact);
        midBox.addView(mid);
        outrangeBox.addView(out);

        answers.addView(contactBox);
        answers.addView(midBox);
        answers.addView(outrangeBox);

        LinearLayout buttonTxt = new LinearLayout(mC);
        buttonTxt.setOrientation(LinearLayout.HORIZONTAL);
        buttonTxt.setPadding(0, 0, 0, (int) mC.getResources().getDimension(R.dimen.margin_combat_asker));

        TextView contactTxt = new TextView(mC);
        contactTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        contactTxt.setGravity(Gravity.CENTER);
        int atkRange = aquene.getAllAttacks().getAtkRange();
        contactTxt.setText("Moins de " + atkRange + "m");
        TextView midTxt = new TextView(mC);
        midTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        midTxt.setGravity(Gravity.CENTER);
        int ms = aquene.getAbilityScore("ms");
        int sum = ms + atkRange;
        midTxt.setText("Entre " + atkRange + "m et " + sum + "m");
        TextView outTxt = new TextView(mC);
        outTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        outTxt.setGravity(Gravity.CENTER);
        outTxt.setText("Plus de " + sum + "m");

        buttonTxt.addView(contactTxt);
        buttonTxt.addView(midTxt);
        buttonTxt.addView(outTxt);

        lineStep.addView(answers);
        lineStep.addView(buttonTxt);

        stepsView.add(lineStep);
        getLayout();
    }

    private void setRadioButtonListnerRange(final RadioButton contact, final RadioButton mid, final RadioButton out) {
        final List<RadioButton> listRadio = Arrays.asList(contact, mid, out);
        for (final RadioButton check : listRadio) {
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (RadioButton checkToUnselect : listRadio) {
                            if (!checkToUnselect.equals(check)) {
                                checkToUnselect.setChecked(false);
                            }
                        }
                        if (check.equals(contact)) {
                            range = true;
                            outrange = false;
                        } else if (check.equals(mid)) {
                            range = false;
                            outrange = false;
                        } else {
                            range = false;
                            outrange = true;
                        }
                        buildResultLine();
                    }
                }
            });
        }
    }

    private void buildResultLine() {
        clearStep(2);
        LinearLayout lineStep = new LinearLayout(mC);
        lineStep.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = new TextView(mC);
        question.setGravity(Gravity.CENTER);
        question.setText("Action(s) possible(s) :");
        question.setTextSize(20);
        question.setBackgroundColor(mC.getColor(R.color.title_question_combat_back));
        question.setTextColor(mC.getColor(R.color.title_question_combat_text));
        lineStep.addView(question);

        TextView result = new TextView(mC);
        result.setGravity(Gravity.CENTER);
        String resultTxt = "";

        List<Attack> possibleAttacks = null;
        int ms = aquene.getAbilityScore("ms");
        if (outrange && moved) {
            resultTxt = "Il est trop loin, il va falloir attendre le prochain round.";
        } else if (outrange && !moved) {
            resultTxt = "Il est trop loin, tu peux te deplacer en marchant (" + ms + "m) et faire autre chose qu'une attaque, ou courir (" + ms * 4 + "m) vers lui.";
        } else if (!moved && range && !outrange) {
            if (aquene.getAllAttacks().getCombatMode().equalsIgnoreCase("totaldef")) {
                possibleAttacks = aquene.getAllAttacks().getAttacksForType("simple");
            } else {
                possibleAttacks = aquene.getAllAttacks().getAttacksForType("complex");
            }
        } else if (!moved && !range && !outrange) {
            resultTxt = "Déplace toi puis :";
            possibleAttacks = aquene.getAllAttacks().getAttacksForType("simple");
        } else if (moved && range && !outrange) {
            possibleAttacks = aquene.getAllAttacks().getAttacksForType("simple");
        } else if (moved && !range && !outrange) {
            resultTxt = "Prochain round tu peux le toucher. En attendant fais autre chose qu'une attaque.";
        }

        result.setText(resultTxt);
        if (!resultTxt.equalsIgnoreCase("")) {
            result.setPadding(0, (int) mC.getResources().getDimension(R.dimen.margin_combat_asker), 0, 0);
            lineStep.addView(result);
        }

        if (possibleAttacks != null) {
            LinearLayout scrollAtkLin = new LinearLayout(mC);
            scrollAtkLin.setPadding(0, (int) mC.getResources().getDimension(R.dimen.margin_combat_asker), 0, 0);
            scrollAtkLin.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout scrollAtkLinTxt = new LinearLayout(mC);
            scrollAtkLinTxt.setOrientation(LinearLayout.HORIZONTAL);

            final List<RadioButton> listRadioAtk = new ArrayList<RadioButton>();
            for (Attack atk : possibleAttacks) {
                LinearLayout box = new LinearLayout(mC);
                box.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                box.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.width = (int) mC.getResources().getDimension(R.dimen.combat_attack_icons);
                params.height = (int) mC.getResources().getDimension(R.dimen.combat_attack_icons);

                RadioButton atkButton = new RadioButton(mC);
                atkButton.setButtonDrawable(null);
                atkButton.setBackground(convertToGrayscale().mutate()); //le mutate c'est pour que le filtre ne s'applique pas sur tout les drawable identique (mire_test)

                atkButton.setLayoutParams(params);
                listRadioAtk.add(atkButton);

                box.addView(atkButton);

                scrollAtkLin.addView(box);

                TextView txt = new TextView(mC);
                txt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                txt.setGravity(Gravity.CENTER);
                txt.setText(atk.getName());
                scrollAtkLinTxt.addView(txt);
            }

            setRadioButtonListnerResult(listRadioAtk);
            lineStep.addView(scrollAtkLin);
            lineStep.addView(scrollAtkLinTxt);
            stepsView.add(lineStep);
            getLayout();
        } else {
            stepsView.add(lineStep);
            getLayout();
            addConfirmationLine();
        }

    }

    private void setRadioButtonListnerResult(final List<RadioButton> listRadioAtk) {
        for (final RadioButton atkButton : listRadioAtk) {
            atkButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (atkButton.isChecked()) {
                        for (RadioButton checkToUnselect : listRadioAtk) {
                            if (!checkToUnselect.equals(atkButton)) {
                                checkToUnselect.setChecked(false);
                            }
                        }
                        selColors(listRadioAtk);
                        addConfirmationLine();
                        getLayout();
                    }

                }
            });
        }
    }

    private void selColors(List<RadioButton> listRadioAtk) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        for (RadioButton rad : listRadioAtk) {
            if (rad.isChecked()) {
                rad.getBackground().clearColorFilter();
            } else {
                rad.getBackground().setColorFilter(filter);
            }
        }
    }

    protected Drawable convertToGrayscale() {
        Drawable draw=mC.getDrawable(R.drawable.mire_test);
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        draw.setColorFilter(filter);

        return draw;
    }

    private void addConfirmationLine() {
        clearStep(3);
        LinearLayout lineStep = new LinearLayout(mC);
        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); //le weight est là pour que ca remplisse le restant du layout
        lineStep.setLayoutParams(para);
        lineStep.setGravity(Gravity.CENTER);
        Button valid = new Button(mC);
        valid.setText("Confirmation");
        valid.setTextColor(mC.getColor(R.color.colorBackground));
        valid.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));
        lineStep.addView(valid);
        stepsView.add(lineStep);
        getLayout();
    }

    public void getLayout() {
        layout.removeAllViews();
        for (View view : stepsView) {
            layout.addView(view);
        }
    }

}
