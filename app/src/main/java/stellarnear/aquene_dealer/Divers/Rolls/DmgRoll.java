package stellarnear.aquene_dealer.Divers.Rolls;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class DmgRoll {

    private Context mC;
    private Boolean manualDiceDmg;
    private Boolean amulette;
    private Boolean aldrassil;
    private SharedPreferences settings;
    private OnRefreshEventListener mListener;
    private Perso aquene = MainActivity.aquene;
    private Boolean critConfirmed;
    private Integer critMultiplier = 2;

    private int bonusDmg = 0;

    private DiceList allDiceList = new DiceList();

    private Tools tools = new Tools();

    public DmgRoll(Activity mA,Context mC, Boolean critConfirmed) {
        this.mC = mC;
        this.critConfirmed = critConfirmed;
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        if (aquene.mythicFeatIsActive("mythicfeat_crit_sup")) {
            critMultiplier += 1;
        }
        manualDiceDmg = settings.getBoolean("switch_manual_diceroll_damage", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_damage_DEF));
        aldrassil = settings.getBoolean("switch_aldrassil", mC.getResources().getBoolean(R.bool.switch_aldrassil_DEF));
        if (aldrassil) {
            allDiceList.add(new Dice(mA,mC, 8));
        }
        amulette = settings.getBoolean("switch_amulette", mC.getResources().getBoolean(R.bool.switch_amulette_DEF));
        if (amulette) {
            allDiceList.add(new Dice(mA,mC, 6,"fire"));
        }
        int nHandDices = tools.toInt(settings.getString("number_main_dice_dmg", String.valueOf(mC.getResources().getInteger(R.integer.number_main_dice_dmg_DEF))));
        int handDiceType = tools.toInt(settings.getString("main_dice_dmg_type", String.valueOf(mC.getResources().getInteger(R.integer.main_dice_dmg_type_DEF))));
        for (int i = 1; i <= nHandDices; i++) {
            Dice hand = new Dice(mA,mC, handDiceType);
            hand.makeCritable();
            allDiceList.add(hand);
        }

        bonusDmg = getBonusDmg();
    }

    public void setDmgRand() {
        for (Dice dice : allDiceList.getList()) {
            dice.rand(manualDiceDmg);
            if (manualDiceDmg) {
                dice.setRefreshEventListener(new Dice.OnRefreshEventListener() {
                    @Override
                    public void onEvent() {
                        mListener.onEvent();
                    }
                });
            }
        }
    }


    private int getBonusDmg() {
        int calcBonusDmg = 0;
        calcBonusDmg += tools.toInt(settings.getString("bonus_temp_jet_dmg", String.valueOf(mC.getResources().getInteger(R.integer.bonus_temp_jet_dmg_DEF))));
        calcBonusDmg += tools.toInt(settings.getString("attack_dmg_epic", String.valueOf(mC.getResources().getInteger(R.integer.attack_dmg_epic_DEF))));

        if (aquene.getAllStances().isActive("stance_lion")) {
            calcBonusDmg += (int) (1.5 * aquene.getAbilityMod(mC, "ability_force"));
        } else {
            calcBonusDmg += aquene.getAbilityMod(mC, "ability_force");
        }
        if (aldrassil) {
            calcBonusDmg += 2;
        }
        if (amulette) {
            calcBonusDmg += 5;
        }
        return calcBonusDmg;
    }

    public interface OnRefreshEventListener {
        void onEvent();
    }

    public void setRefreshEventListener(OnRefreshEventListener eventListener) {
        mListener = eventListener;
    }


    // Getters

    public DiceList getDmgDiceList() {
        return allDiceList;
    }

    public int getDmgBonus() {
        return getBonusDmg();
    }

    public int getSumDmg(String... elementArg) {
        String element = elementArg.length > 0 ? elementArg[0] : "";
        int sumDmg = 0;
        for (Dice dice : allDiceList.getList()) {
            if (dice.getElement().equalsIgnoreCase(element)) {
                if(dice.canCrit() && critConfirmed) {
                    sumDmg += dice.getRandValue() * critMultiplier;
                } else {
                    sumDmg += dice.getRandValue();
                }
            }
        }

        if (element.equalsIgnoreCase("") && critConfirmed) {
            sumDmg += bonusDmg * critMultiplier;
        } else if (element.equalsIgnoreCase("")) {
            sumDmg += bonusDmg;
        }
        return sumDmg;
    }

    public int getMaxDmg(String... elementArg) {
        String element = elementArg.length > 0 ? elementArg[0] : "";
        int maxDmg = 0;
        for (Dice dice : allDiceList.getList()) {
            if (dice.getElement().equalsIgnoreCase(element)) {
                if(dice.canCrit() && critConfirmed) {
                    maxDmg += dice.getnFace() * critMultiplier;
                } else {
                    maxDmg += dice.getnFace();
                }
            }
        }

        if (element.equalsIgnoreCase("") && critConfirmed) {
            maxDmg += bonusDmg * critMultiplier;
        } else if (element.equalsIgnoreCase("")) {
            maxDmg += bonusDmg;
        }
        return maxDmg;
    }

    public int getMinDmg(String... elementArg) {
        String element = elementArg.length > 0 ? elementArg[0] : "";
        int minDmg = 0;
        for (Dice dice : allDiceList.getList()) {
            if (dice.getElement().equalsIgnoreCase(element)) {
                if(dice.canCrit() && critConfirmed) {
                    minDmg += 1 * critMultiplier;
                } else {
                    minDmg += 1;
                }
            }
        }

        if (element.equalsIgnoreCase("") && critConfirmed) {
            minDmg += bonusDmg * critMultiplier;
        } else if (element.equalsIgnoreCase("")) {
            minDmg += bonusDmg;
        }
        return minDmg;
    }

    public Integer getCritMultiplier() {
        return critMultiplier;
    }
}
