package stellarnear.aquene_companion.Divers.CombatPanel;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stellarnear.aquene_companion.Activities.MainActivity;
import stellarnear.aquene_companion.Perso.Attack;
import stellarnear.aquene_companion.Perso.Equipment;
import stellarnear.aquene_companion.Perso.Perso;
import stellarnear.aquene_companion.R;

/**
 * Created by jchatron on 07/02/2018.
 */

public class CombatAskerResultLine {
    private Perso aquene = MainActivity.aquene;
    private List<RadioButton> listRadioAtk;
    private List<Attack> possibleAttacks;
    private LinearLayout lineStep;
    private Context mC;
    private Map<RadioButton, Attack> mapRadioAtkAtk = new HashMap<>();
    private boolean kistep;

    public CombatAskerResultLine(Activity mA, Context mC, Boolean moved, Boolean range, Boolean farRange, Boolean chargeRange, Boolean canCharge, Boolean outrange) {
        this.mC = mC;
        this.kistep = false;
        lineStep = new LinearLayout(mC);
        lineStep.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lineStep.setOrientation(LinearLayout.VERTICAL);

        TextView question = questionLayout("Action(s) possible(s) :");
        lineStep.addView(question);

        TextView result = new TextView(mC);
        result.setGravity(Gravity.CENTER);
        String resultTxt = "";

        possibleAttacks = new ArrayList<>();
        int ms = aquene.getAbilityScore( "ability_ms");
        if(aquene.featIsActive("feat_void_step")){
            resultTxt+="Pas du vide et ";
        }
        if (aquene.getAllAttacks().getCombatMode().equalsIgnoreCase("mode_totaldef")) {
            resultTxt += "tu es en mode défénse total.\nIl ne te reste qu'une action de mouvement par round.\nTu peux te deplacer en marchant (" + ms + "m).";
        } else if (outrange) {
            if (moved) {
                resultTxt += "il est trop loin, il va falloir attendre le prochain round.";
            } else {
                Equipment head = aquene.getInventory().getAllEquipments().getEquipmentsEquiped("helm_slot");
                int runMultiplier = (head!= null && head.getName().equalsIgnoreCase("oreilles de lapin") ? 8:4);
                resultTxt += "il est trop loin, tu peux te deplacer en marchant (" + ms + "m).\n Puis faire autre chose qu'une attaque.\nOu bien courir (" + ms * runMultiplier + "m) vers lui.";
            }
        } else if (farRange) {
            if (moved) {
                resultTxt += "il est trop loin, il va falloir attendre le prochain round.";
            } else {
                if (chargeRange) {
                    if (canCharge) {
                        if (aquene.getAllStances().getCurrentStance() != null && (aquene.getAllStances().isActive("stance_bear") || aquene.getAllStances().isActive("stance_phenix"))) {
                            resultTxt += "ta posture ne te permet qu'une attaque simple.";
                            possibleAttacks = aquene.getAttacksForType("simple");
                        } else {
                            resultTxt += "Charge ! et :";
                            if (aquene.featIsActive("feat_dire_charge")) {
                                possibleAttacks = aquene.getAttacksForType("complex");
                            } else {
                                possibleAttacks = aquene.getAttacksForType("simple");
                            }
                        }
                    } else {
                        resultTxt += "utilises pas chassé (2 pts Ki), et :";
                        kistep = true;
                        possibleAttacks = aquene.getAttacksForType("simple");
                    }
                } else {
                    resultTxt += "utilises pas chassé (2 pts Ki), et :";
                    kistep = true;
                    possibleAttacks = aquene.getAttacksForType("simple");
                }
            }
        } else if (!range) {
            if (moved) {
                resultTxt += "prochain round tu peux le toucher.\nEn attendant fais autre chose qu'une attaque.";
            } else {
                if (canCharge) {
                    if (aquene.getAllStances().getCurrentStance() != null && (aquene.getAllStances().isActive("stance_bear") || aquene.getAllStances().isActive("stance_phenix"))) {
                        resultTxt += "ta posture ne te permet qu'une attaque simple.";
                        possibleAttacks = aquene.getAttacksForType("simple");
                    } else {
                        resultTxt += "charge ! et :";
                        if (aquene.featIsActive("feat_dire_charge")) {
                            possibleAttacks = aquene.getAttacksForType("complex");
                        } else {
                            possibleAttacks = aquene.getAttacksForType("simple");
                        }
                    }
                } else {
                    resultTxt += "déplace toi et :";
                    possibleAttacks = aquene.getAttacksForType("simple");
                }
            }
        } else {
            if (moved) {
                possibleAttacks = aquene.getAttacksForType("simple");
            } else if (aquene.getAllStances().getCurrentStance() != null && (aquene.getAllStances().isActive("stance_bear") || aquene.getAllStances().isActive("stance_phenix"))) {
                resultTxt += "ta posture ne te permet qu'une attaque simple.";
                possibleAttacks = aquene.getAttacksForType("simple");
            } else {
                possibleAttacks = aquene.getAttacksForType("complex");
            }
        }

        if (canCharge) {
            for (Attack atk : possibleAttacks) {
                atk.setFromCharge(true);
            }
        } else {
            for (Attack atk : possibleAttacks) {
                atk.setFromCharge(false);
            }
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

            listRadioAtk = new ArrayList<RadioButton>();
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
                TextView txt = summaryText(atk.getShortname());
                boxTxt.addView(txt);
                scrollAtkLinTxt.addView(boxTxt);
            }
            lineStep.addView(scrollAtkLin);
            lineStep.addView(scrollAtkLinTxt);
        }
    }

    private LinearLayout box() {
        LinearLayout box = new LinearLayout(mC);
        box.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        box.setGravity(Gravity.CENTER);
        return box;
    }

    private TextView summaryText(String s) {
        TextView sumamrTxt = new TextView(mC);
        sumamrTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sumamrTxt.setGravity(Gravity.CENTER);
        sumamrTxt.setText(s);
        return sumamrTxt;
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

    protected Drawable convertToGrayscale(Drawable inputDraw) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        Drawable newDraw = inputDraw.mutate();
        newDraw.setColorFilter(filter);
        return newDraw;
    }

    public Map<RadioButton, Attack> getMapRadioAtkAtk() {
        return mapRadioAtkAtk;
    }

    public List<RadioButton> getListRadioAtk() {
        return listRadioAtk;
    }

    public boolean attackToLaunch() {
        boolean value = false;
        if (possibleAttacks != null && possibleAttacks.size() > 0) {
            value = true;
        }
        return value;
    }

    public LinearLayout getResultLine() {
        return lineStep;
    }

    public Boolean getKistep() {
        return this.kistep;
    }
}
