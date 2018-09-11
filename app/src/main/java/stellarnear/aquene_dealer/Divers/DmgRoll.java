package stellarnear.aquene_dealer.Divers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class DmgRoll {

    private Context mC;
    private Boolean manualDiceDmg;
    private Boolean amulette;
    private Boolean aldrassil;
    private SharedPreferences settings;
    private OnRefreshEventListener mListener;
    private Perso aquene= MainActivity.aquene;
    private Boolean critConfirmed;
    private Integer critMultipler=2;

    private int bonusDmg=0;

    private List<Dice> randRegularPhyDiceList =new ArrayList<>();
    private List<Dice> randAddPhyDiceList =new ArrayList<>();
    private List<Dice> randFireDiceList =new ArrayList<>();
    private List<Dice> allDiceList =new ArrayList<>();

    private Tools tools=new Tools();

    public DmgRoll(Context mC,Boolean critConfirmed) {
        this.mC = mC;
        this.critConfirmed=critConfirmed;
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        manualDiceDmg = settings.getBoolean("switch_manual_diceroll_damage", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_damage_DEF));
        aldrassil = settings.getBoolean("switch_aldrassil", mC.getResources().getBoolean(R.bool.switch_aldrassil_DEF));
        if(aldrassil){ randAddPhyDiceList.add(new Dice(mC,8));}
        amulette = settings.getBoolean("switch_amulette", mC.getResources().getBoolean(R.bool.switch_amulette_DEF));
        if(amulette){ randFireDiceList.add(new Dice(mC,6));}
        int nHandDices = tools.toInt(settings.getString("number_main_dice_dmg", String.valueOf(mC.getResources().getInteger(R.integer.number_main_dice_dmg_DEF))));
        int handDiceType = tools.toInt(settings.getString("main_dice_dmg_type", String.valueOf(mC.getResources().getInteger(R.integer.main_dice_dmg_type_DEF))));
        for (int i=1;i<=nHandDices;i++){
            randRegularPhyDiceList.add(new Dice(mC,handDiceType));
        }

        allDiceList.addAll(randFireDiceList);
        allDiceList.addAll(randRegularPhyDiceList);
        allDiceList.addAll(randAddPhyDiceList);

        bonusDmg=getBonusDmg();
    }

    public void setDmgRand() {
        for (Dice dice : allDiceList) {
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
        calcBonusDmg +=  tools.toInt(settings.getString("bonus_temp_jet_dmg", String.valueOf(mC.getResources().getInteger(R.integer.bonus_temp_jet_dmg_DEF))));
        calcBonusDmg +=  tools.toInt(settings.getString("attack_dmg_epic", String.valueOf(mC.getResources().getInteger(R.integer.attack_dmg_epic_DEF))));

        if (aquene.getAllStances().isActive("stance_lion")) {
            calcBonusDmg += (int) (1.5 * aquene.getAbilityMod(mC,"ability_force"));
        } else {
            calcBonusDmg += aquene.getAbilityMod(mC,"ability_force");
        }
        if (aldrassil) {   calcBonusDmg += 2;  }
        if (amulette) {    calcBonusDmg += 5;  }
        return calcBonusDmg;
    }

    public interface OnRefreshEventListener {
        void onEvent();
    }

    public void setRefreshEventListener(OnRefreshEventListener eventListener) {
        mListener = eventListener;
    }


    // Getters

    public List<Dice> getDmgDiceList() {
        return allDiceList;
    }

    public int getDmgBonus() {
        return getBonusDmg();
    }

    public int getSumPhy() {
        int sumPhy=0;
        for(Dice dice : randRegularPhyDiceList){
            if(critConfirmed){sumPhy+=dice.getRandValue()*critMultipler;}else{sumPhy+=dice.getRandValue();}
        }
        for(Dice dice : randAddPhyDiceList){
            sumPhy+=dice.getRandValue();
        }
        if(critConfirmed){sumPhy+=bonusDmg*critMultipler;}else{sumPhy+=bonusDmg;}

        return sumPhy;
    }

    public int getSumFire() {
        int sumFire=0;
        for(Dice dice : randFireDiceList){
            sumFire+=dice.getRandValue();
        }
        return sumFire;
    }
}
