package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Attack;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class CombatAsker {
    private Perso aquene = MainActivity.aquene;
    private Context mC;
    private int lastStep;
    private LinearLayout layout;
    private ArrayList<View> stepsView = new ArrayList<View>();
    private Map<RadioButton,Attack> mapRadioAtkAtk=new HashMap<>();
    private Attack selectedAttack=null;
    private boolean moved;
    private boolean range;
    private boolean outrange;
    Button valid;
    View.OnClickListener backToMainListner;

    public CombatAsker(Context mC, LinearLayout layout, View.OnClickListener backToMainListner) {
        this.mC = mC;
        this.layout = layout;
        this.backToMainListner=backToMainListner;

        buildMovedLine();
    }

    public void reset(){
        buildMovedLine();
    }

    private void buildMovedLine() {
        clearStep(0);
        LinearLayout lineStep = new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = questionLayout("T'es tu déplacé au cours de ce round ?");
        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout yesBox = box();
        LinearLayout noBox = box();

        RadioButton yes=answerIcon(mC.getDrawable(R.drawable.moving_selector));
        RadioButton no = answerIcon(mC.getDrawable(R.drawable.notmoving_selector));

        setRadioButtonListnerMoved(yes, no);
        yesBox.addView(yes);
        noBox.addView(no);

        answers.addView(yesBox);
        answers.addView(noBox);

        LinearLayout buttonTxt = new LinearLayout(mC);
        buttonTxt.setOrientation(LinearLayout.HORIZONTAL);
        buttonTxt.setPadding(0, 0, 0, (int) mC.getResources().getDimension(R.dimen.margin_combat_asker));

        TextView yesTxt = summaryText("Oui j'ai bougé");
        TextView noTxt= summaryText("Non je n'ai rien fais");
        buttonTxt.addView(yesTxt);
        buttonTxt.addView(noTxt);

        lineStep.addView(answers);
        lineStep.addView(buttonTxt);

        stepsView.add(lineStep);
        getLayout();
    }

    private TextView summaryText(String s) {
        TextView sumamrTxt=new TextView(mC);
        sumamrTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
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
        box.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        box.setGravity(Gravity.CENTER);
        return box;
    }

    private TextView questionLayout(String s) {
        TextView question=new TextView(mC);
        question.setGravity(Gravity.CENTER);
        question.setText(s);
        question.setTextSize(20);
        question.setBackgroundColor(mC.getColor(R.color.title_question_combat_back));
        question.setTextColor(mC.getColor(R.color.title_question_combat_text));
        return question;
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

        TextView question = questionLayout("À quelle distance est l'ennemi ?");
        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout contactBox = box();
        LinearLayout midBox = box();
        LinearLayout outrangeBox = box();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = (int) mC.getResources().getDimension(R.dimen.combat_answer_icons);
        params.height = (int) mC.getResources().getDimension(R.dimen.combat_answer_icons);

        RadioButton contact = answerIcon(mC.getDrawable(R.drawable.contact_selector));
        RadioButton mid = answerIcon(mC.getDrawable(R.drawable.mid_range_selector));
        RadioButton out = answerIcon(mC.getDrawable(R.drawable.out_range_selector));

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

        int atkRange = aquene.getAllAttacks().getAtkRange();
        TextView contactTxt = summaryText("Moins de " + atkRange + "m");
        int ms = aquene.getAbilityScore("ability_ms");
        int sum = ms + atkRange;
        TextView midTxt = summaryText("Entre " + atkRange + "m et " + sum + "m");
        TextView outTxt =summaryText("Plus de " + sum + "m");

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

        TextView question = questionLayout("Action(s) possible(s) :");
        lineStep.addView(question);

        TextView result = new TextView(mC);
        result.setGravity(Gravity.CENTER);
        String resultTxt = "";

        List<Attack> possibleAttacks = new ArrayList<>();
        int ms = aquene.getAbilityScore("ability_ms");
        if(aquene.getAllAttacks().getCombatMode().equalsIgnoreCase("totaldef")){
            resultTxt = "Tu es en mode défénse total. Il ne te reste qu'une action de mouvement par round. Tu peux te deplacer en marchant (" + ms + "m).";
        } else if (outrange && moved) {
            resultTxt = "Il est trop loin, il va falloir attendre le prochain round.";
        } else if (outrange && !moved) {
            resultTxt = "Il est trop loin, tu peux te deplacer en marchant (" + ms + "m) et faire autre chose qu'une attaque, ou courir (" + ms * 4 + "m) vers lui.";
        } else if (!moved && range && !outrange) {
            if (aquene.getAllAttacks().getCombatMode().equalsIgnoreCase("totaldef")) {
                possibleAttacks = aquene.getAttacksForType("simple");
            } else {
                possibleAttacks = aquene.getAttacksForType("complex");
            }
        } else if (!moved && !range && !outrange) {
            resultTxt = "Déplace toi puis :";
            possibleAttacks = aquene.getAttacksForType("simple");
        } else if (moved && range && !outrange) {
            possibleAttacks = aquene.getAttacksForType("simple");
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
            scrollAtkLin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout scrollAtkLinTxt = new LinearLayout(mC);
            scrollAtkLinTxt.setOrientation(LinearLayout.HORIZONTAL);
            scrollAtkLinTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            final List<RadioButton> listRadioAtk = new ArrayList<RadioButton>();
            for (Attack atk : possibleAttacks) {
                LinearLayout box = box();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.width = (int) mC.getResources().getDimension(R.dimen.combat_attack_icons);
                params.height = (int) mC.getResources().getDimension(R.dimen.combat_attack_icons);

                RadioButton atkButton = new RadioButton(mC);
                atkButton.setButtonDrawable(null);
                int imgId = mC.getResources().getIdentifier(atk.getId(), "drawable", mC.getPackageName());
                atkButton.setBackground(convertToGrayscale(mC.getDrawable(imgId)).mutate()); //le mutate c'est pour que le filtre ne s'applique pas au drawable source
                atkButton.setLayoutParams(params);
                atkButton.setGravity(Gravity.CENTER);
                listRadioAtk.add(atkButton);
                mapRadioAtkAtk.put(atkButton,atk);

                box.addView(atkButton);
                scrollAtkLin.addView(box);

                TextView txt = summaryText(atk.getName());
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
                        atkButton.getBackground().clearColorFilter();
                        for (RadioButton checkToUnselect : listRadioAtk) {
                            if (!checkToUnselect.equals(atkButton)) {
                                checkToUnselect.setChecked(false);
                                ColorMatrix matrix = new ColorMatrix();
                                matrix.setSaturation(0);
                                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                                checkToUnselect.getBackground().setColorFilter(filter);
                            }
                        }
                        selectedAttack=mapRadioAtkAtk.get(atkButton);
                        addConfirmationLine();
                        getLayout();
                    }

                }
            });
        }
    }

    protected Drawable convertToGrayscale(Drawable inputDraw) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        inputDraw.setColorFilter(filter);
        return inputDraw;
    }

    private void addConfirmationLine() {
        clearStep(3);
        LinearLayout lineStep = new LinearLayout(mC);
        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); //le weight est là pour que ca remplisse le restant du layout
        lineStep.setLayoutParams(para);
        lineStep.setGravity(Gravity.CENTER);
        valid = new Button(mC);
        valid.setText("Confirmation");
        valid.setTextColor(mC.getColor(R.color.colorBackground));
        valid.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));
        CompositeListner compositeListner=new CompositeListner();
        compositeListner.addOnclickListener(backToMainListner);
        View.OnClickListener randomToast = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt;
                if(selectedAttack==null){
                    txt="Retour au menu principal";
                } else {
                    txt="Lancement de : "+selectedAttack.getName();
                    aquene.getRessources().spendAttack(selectedAttack.getId());
                }

                Snackbar snackbar = Snackbar.make(view, txt, Snackbar.LENGTH_LONG);
                snackbar.show();
                //TODO lancement d'n dialog avec l'attaque lancée
            }
        };
        compositeListner.addOnclickListener(randomToast);
        valid.setOnClickListener(compositeListner);

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
