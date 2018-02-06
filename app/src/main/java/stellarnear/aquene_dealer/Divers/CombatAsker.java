package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

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
    private Activity mA;
    private Context mC;
    private int lastStep;
    private LinearLayout layout;
    private ArrayList<View> stepsView = new ArrayList<View>();
    private Map<RadioButton, Attack> mapRadioAtkAtk = new HashMap<>();
    private Attack selectedAttack = null;
    private boolean moved;
    private boolean range;
    private boolean kistep;
    private boolean outrange;
    Button valid;
    View.OnClickListener backToMainListner;

    public CombatAsker(Activity mA,Context mC, LinearLayout layout, View.OnClickListener backToMainListner) {
        this.mA=mA;
        this.mC = mC;
        this.layout = layout;
        this.backToMainListner = backToMainListner;

        buildMovedLine();
    }

    public void reset() {
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

        RadioButton yes = answerIcon(mC.getDrawable(R.drawable.moving_selector));
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
        TextView noTxt = summaryText("Non je n'ai rien fais");

        LinearLayout yesBoxTxt = box();
        LinearLayout noBoxTxt = box();

        yesBoxTxt.addView(yesTxt);
        noBoxTxt.addView(noTxt);

        buttonTxt.addView(yesBoxTxt);
        buttonTxt.addView(noBoxTxt);

        lineStep.addView(answers);
        lineStep.addView(buttonTxt);

        stepsView.add(lineStep);
        getLayout();
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
        getLayout();
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
                            kistep=false;
                            outrange = false;
                            buildResultLine();
                        } else if (check.equals(mid)) {
                            range = false;
                            kistep=false;
                            outrange = false;
                            buildResultLine();
                        } else {
                            if (!moved && aquene.getAllResources().getResource("resource_ki").getCurrent()>=aquene.getAllKiCapacities().getKicapacity("kicapacity_step").getCost()) {
                                range = false;
                                askForKiMovement();
                            } else {
                                range = false;
                                kistep=false;
                                outrange = true;
                                buildResultLine();
                            }
                        }
                    }
                }
            });
        }
    }

    private void askForKiMovement() {
        clearStep(2);
        final LinearLayout lineStep = new LinearLayout(mC);
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = questionLayout("Précision de distance :");
        lineStep.addView(question);

        LinearLayout answers = new LinearLayout(mC);
        answers.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        answers.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout contactBox = box();
        LinearLayout outrangeBox = box();

        RadioButton contact = answerIcon(mC.getDrawable(R.drawable.kirange_selector));
        RadioButton out = answerIcon(mC.getDrawable(R.drawable.kioutrange_selector));

        setRadioButtonListnerKiRange(contact, out);
        contactBox.addView(contact);
        outrangeBox.addView(out);

        answers.addView(contactBox);
        answers.addView(outrangeBox);

        LinearLayout buttonTxt = new LinearLayout(mC);
        buttonTxt.setOrientation(LinearLayout.HORIZONTAL);
        buttonTxt.setPadding(0, 0, 0, (int) mC.getResources().getDimension(R.dimen.margin_combat_asker));

        int atkRange = aquene.getAllAttacks().getAtkRange();
        int tpKi = 120 + 12*aquene.getAbilityScore("ability_lvl");
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

        Animation outLeft = AnimationUtils.loadAnimation(mC,R.anim.outtoleft);
        final Animation inRight = AnimationUtils.loadAnimation(mC,R.anim.infromright);
        inRight.setFillAfter(true);
        outLeft.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearStep(1);
                stepsView.add(1, lineStep);
                stepsView.get(1).startAnimation(inRight);
                getLayout();
            }
        });
        stepsView.get(1).startAnimation(outLeft);
    }

    private void setRadioButtonListnerKiRange(final RadioButton contact, final RadioButton out) {
        final List<RadioButton> listRadio = Arrays.asList(contact, out);
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
                            kistep=true;
                            outrange=false;
                        } else {
                            kistep=false;
                            outrange=true;
                        }
                        buildResultLine();
                    }
                }
            });
        }
    }

    private void buildResultLine() {
        clearStep(2);
        selectedAttack = null;
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
        if (aquene.getAllAttacks().getCombatMode().equalsIgnoreCase("mode_totaldef")) {
            resultTxt = "Tu es en mode défénse total.\nIl ne te reste qu'une action de mouvement par round.\nTu peux te deplacer en marchant (" + ms + "m).";
        } else if (outrange && moved) {
            resultTxt = "Il est trop loin, il va falloir attendre le prochain round.";
        } else if (outrange && !moved) {
            resultTxt = "Il est trop loin, tu peux te deplacer en marchant (" + ms + "m).\n Puis faire autre chose qu'une attaque.\nOu bien courir (" + ms * 4 + "m) vers lui.";
        } else if (!moved && range && !outrange) {
            possibleAttacks = aquene.getAttacksForType("complex");
        } else if (!moved && kistep && !range && !outrange) {
            resultTxt = "Déplace toi avec pas chassé (2 points de Ki), puis :";
            possibleAttacks = aquene.getAttacksForType("simple");
        } else if (!moved && !range && !outrange) {
            resultTxt = "Déplace toi puis :";
            possibleAttacks = aquene.getAttacksForType("simple");
        } else if (moved && range && !outrange) {
            possibleAttacks = aquene.getAttacksForType("simple");
        } else if (moved && !range && !outrange) {
            resultTxt = "Prochain round tu peux le toucher.\nEn attendant fais autre chose qu'une attaque.";
        }

        result.setText(resultTxt);
        if (!resultTxt.equalsIgnoreCase("")) {
            result.setPadding(0, (int) mC.getResources().getDimension(R.dimen.margin_combat_asker), 0, 0);
            lineStep.addView(result);
        }

        if (possibleAttacks != null && possibleAttacks.size() > 0) {
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
                atkButton.setBackground(convertToGrayscale(mC.getDrawable(imgId))); //le mutate c'est pour que le filtre ne s'applique pas au drawable source
                atkButton.setLayoutParams(params);
                atkButton.setGravity(Gravity.CENTER);
                listRadioAtk.add(atkButton);
                mapRadioAtkAtk.put(atkButton, atk);

                box.addView(atkButton);
                scrollAtkLin.addView(box);

                LinearLayout boxTxt;
                boxTxt = box();
                TextView txt = summaryText(atk.getName());
                boxTxt.addView(txt);
                scrollAtkLinTxt.addView(boxTxt);
            }

            setRadioButtonListnerResult(listRadioAtk);
            lineStep.addView(scrollAtkLin);
            lineStep.addView(scrollAtkLinTxt);
            stepsView.add(lineStep);
            getLayout();
        } else {
            stepsView.add(lineStep);
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
                        selectedAttack = mapRadioAtkAtk.get(atkButton);
                        addConfirmationLine();
                    }

                }
            });
        }
    }

    protected Drawable convertToGrayscale(Drawable inputDraw) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        Drawable newDraw = inputDraw.mutate();
        newDraw.setColorFilter(filter);
        return newDraw;
    }

    private void addConfirmationLine() {
        clearStep(3);
        LinearLayout lineStep = new LinearLayout(mC);
        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); //le weight est là pour que ca remplisse le restant du layout
        lineStep.setLayoutParams(para);
        lineStep.setGravity(Gravity.CENTER);
        valid = new Button(mC);
        if (selectedAttack == null) {
            valid.setText("Quitter");
            valid.setTextColor(mC.getColor(R.color.colorBackground));
            valid.setBackground(mC.getDrawable(R.drawable.button_cancel_gradient));
        } else {
            valid.setText("Confirmation");
            valid.setTextColor(mC.getColor(R.color.colorBackground));
            valid.setBackground(mC.getDrawable(R.drawable.button_ok_gradient));

        }
        CompositeListner compositeListner = new CompositeListner();
        compositeListner.addOnclickListener(backToMainListner);
        View.OnClickListener randomToast = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt;
                if (selectedAttack == null) {
                    txt = "Retour au menu principal";
                } else {
                    txt = "Lancement de : " + selectedAttack.getName();
                    if (aquene.getAllResources().getResource(selectedAttack.getId().replace("attack", "resource")) != null) {
                        aquene.getAllResources().getResource(selectedAttack.getId().replace("attack", "resource")).spend(1);
                    }
                    if (kistep){aquene.getAllResources().getResource("resource_ki").spend(aquene.getAllKiCapacities().getKicapacity("kicapacity_step").getCost());}
                    CombatLauncher combatLauncher = new CombatLauncher(mA,mC,selectedAttack);
                    combatLauncher.showAlertDialog();
                }
                Snackbar snackbar = Snackbar.make(view, txt, Snackbar.LENGTH_LONG);
                snackbar.show();

            }
        };
        compositeListner.addOnclickListener(randomToast);
        valid.setOnClickListener(compositeListner);
        lineStep.addView(valid);
        stepsView.add(lineStep);
        getLayout();
    }


    public void getLayout() {
        for (int i=0;i <layout.getChildCount();i++){
            View viewToRemove=layout.getChildAt(i);
            if (!stepsView.contains(viewToRemove)){
                layout.removeView(viewToRemove);
            }
        }

        for (View view : stepsView) {
            if (view.getParent()==null){layout.addView(view);}
        }
    }
}
