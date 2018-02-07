package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.BoringLayout;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 07/02/2018.
 */

public class Roll {
    private Integer base=0;
    private Integer rand=0;
    private Integer preRandValue=0;
    private Integer atk=0;
    private Boolean crit=false;
    private Boolean critConfirmed=false;
    private Boolean fail=false;
    private Boolean invalid=false;
    private Context mC;
    private SharedPreferences settings;
    private Perso aquene= MainActivity.aquene;
    public Roll(Context mC,Integer base){
        this.mC=mC;
        this.base=base;
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        this.preRandValue=base+getBonusAtk();
    }

    public void setRand(Integer rand){
        this.rand = rand;
        calcul();
    }

    private void calcul() {
        this.atk=base+getBonusAtk()+rand;

        if (rand==1){
            this.fail=true;
        }
        int critSup=(aquene.featIsActive("feat_crit_sup")) ? 1:0;
        int critKeen=(aquene.featIsActive("feat_keen_strike")) ? 1:0;
        int critMin = 20;
        if (aquene.featIsActive("feat_crit_sup") && aquene.featIsActive("feat_keen_strike"))
        { critMin -= critSup*2*critKeen*3;} else {critMin -= critSup*2 - critKeen*3;}

        if (rand>=critMin){
            crit=true;
        }
    }

    private int getBonusAtk() {
        int bonusAtk=0;

        bonusAtk+=settings.getInt("bonus_jet_att",mC.getResources().getInteger(R.integer.bonus_jet_att_DEF));

        if(aquene.getAllStances().isActive("stance_lion")){
            bonusAtk+= (int) (1.5 * aquene.getAbilityMod("ability_force"));
        } else {
            bonusAtk+= aquene.getAbilityMod("ability_force");
        }
        if (settings.getBoolean("switch_aldrassil",mC.getResources().getBoolean(R.bool.switch_aldrassil_DEF))){
            bonusAtk+=2;
        }
        if (settings.getBoolean("switch_amulette",mC.getResources().getBoolean(R.bool.switch_amulette_DEF))){
            bonusAtk+=5;
        }

        return bonusAtk;
    }

    public Integer getPreRandValue() {
        return preRandValue;
    }

    public Integer getValue(){
        return atk;
    }

    public Boolean isInvalid(){
        return invalid;
    }

    public void invalidated(){
        this.invalid=true;
    }

    public boolean isFailed() {
        return this.fail;
    }
}
