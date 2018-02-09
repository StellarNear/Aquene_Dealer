package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.BoringLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 07/02/2018.
 */

public class Roll {
    private Integer base = 0;
    private Integer rand = 0;
    private Integer preRandValue = 0;
    private Integer atk = 0;
    private int[] dmg10=null;
    private int[] dmg8=null;
    private int[] dmg6=null;
    private Boolean hitConfirmed = false;
    private Boolean crit = false;
    private Boolean critConfirmed = false;
    private Boolean fail = false;
    private Boolean invalid = false;
    private Context mC;
    private Boolean unset = true;
    private SharedPreferences settings;
    private Perso aquene = MainActivity.aquene;
    private CheckBox hitCheckbox;
    private CheckBox critCheckbox;

    public Roll(Context mC, Integer base) {
        this.mC = mC;
        this.base = base;
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        this.preRandValue = base + getBonusAtk();

        constructCheckboxes();
    }

    private void constructCheckboxes() {
        hitCheckbox = new CheckBox(mC);
        hitCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                hitConfirmed=false;
                if ( isChecked )
                {
                    hitConfirmed=true;
                }
            }
        });

        critCheckbox = new CheckBox(mC);
        critCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                critConfirmed=false;
                if ( isChecked )
                {
                    hitCheckbox.setChecked(true);
                    critConfirmed=true;
                }
            }
        });
    }

    public void setAtkRand(Integer rand) {
        this.rand = rand;
        this.unset = false;
        calcul();
    }

    public Integer getRand() {
        return this.rand;
    }

    private void calcul() {
        this.atk = base + getBonusAtk() + rand;

        if (rand == 1) {
            this.fail = true;
        }
        int critSup = (aquene.featIsActive("feat_crit_sup")) ? 1 : 0;
        int critKeen = (aquene.featIsActive("feat_keen_strike")) ? 1 : 0;
        int critMin;
        if (aquene.featIsActive("feat_crit_sup") && aquene.featIsActive("feat_keen_strike")) {
            critMin = 21 - critSup * 2 * critKeen * 3;
        } else if (aquene.featIsActive("feat_crit_sup") || aquene.featIsActive("feat_keen_strike")) {
            critMin = 21 - critSup * 2 - critKeen * 3;
        } else {
            critMin = 20;
        }

        if (rand >= critMin) {
            this.crit = true;
        }
    }

    private int getBonusAtk() {
        int bonusAtk = 0;

        bonusAtk += settings.getInt("bonus_jet_att", mC.getResources().getInteger(R.integer.bonus_jet_att_DEF));

        if (aquene.getAllStances().isActive("stance_lion")) {
            bonusAtk += (int) (1.5 * aquene.getAbilityMod("ability_force"));
        } else {
            bonusAtk += aquene.getAbilityMod("ability_force");
        }
        if (settings.getBoolean("switch_aldrassil", mC.getResources().getBoolean(R.bool.switch_aldrassil_DEF))) {
            bonusAtk += 2;
        }
        if (settings.getBoolean("switch_amulette", mC.getResources().getBoolean(R.bool.switch_amulette_DEF))) {
            bonusAtk += 5;
        }

        return bonusAtk;
    }

    public Integer getPreRandValue() {
        return preRandValue;
    }

    public Integer getValue() {
        return atk;
    }

    public Boolean isInvalid() {
        return invalid;
    }

    public void invalidated() {
        this.invalid = true;
    }

    public boolean isFailed() {
        return this.fail;
    }

    public boolean isUnset() {
        return this.unset;
    }

    public boolean isCrit() {
        return this.crit;
    }

    public boolean isHitConfirmed(){
        return this.hitConfirmed;
    }
    public boolean isCritConfirmed(){
        return this.critConfirmed;
    }

    public CheckBox getHitCheckbox() {
        return this.hitCheckbox;
    }

    public CheckBox getCritCheckbox() {
        return this.critCheckbox;
    }

    public int[] getDmgDiceRand(int dice) {
        int[] listDmgRand=null;
        if(dice==10){
            listDmgRand=dmg10;
        }
        if(dice==8){
            listDmgRand=dmg8;
        }
        if(dice==6){
            listDmgRand=dmg6;
        }
        return listDmgRand;
    }

    public void setDmgRand(int dice, int[] ints) {
        if(dice==10){
            dmg10=ints;
        }
        if(dice==8){
            dmg8=ints;
        }
        if(dice==6){
            dmg6=ints;
        }
    }


    public void isDelt() {
        invalid=true;
        hitCheckbox.setEnabled(false);
        critCheckbox.setEnabled(false);
    }
}
