package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
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
    private Activity mA;
    private Boolean manualDiceDmg;
    private Boolean amulette;
    private Boolean aldrassil;
    private SharedPreferences settings;
    private OnRefreshEventListener mListener;
    private Perso aquene= MainActivity.aquene;
    private Boolean critConfirmed=false;

    private int bonusDmg=0;
    private List<Integer> randD6=new ArrayList<>();
    private int nD6=0;
    private List<Integer> randD8=new ArrayList<>();
    private int nD8=0;
    private List<Integer> randD10=new ArrayList<>();
    private int nD10=0;
    private List<ImageView> listImgD6=new ArrayList<>();
    private List<ImageView> listImgD8=new ArrayList<>();
    private List<ImageView> listImgD10=new ArrayList<>();
    private Tools tools=new Tools();

    public DmgRoll(Activity mA, Context mC,Boolean critConfirmed) {
        this.mA = mA;
        this.mC = mC;
        this.critConfirmed=critConfirmed;
        settings = PreferenceManager.getDefaultSharedPreferences(mC);
        manualDiceDmg = settings.getBoolean("switch_manual_diceroll_damage", mC.getResources().getBoolean(R.bool.switch_manual_diceroll_damage_DEF));
        aldrassil = settings.getBoolean("switch_aldrassil", mC.getResources().getBoolean(R.bool.switch_aldrassil_DEF));
        amulette = settings.getBoolean("switch_amulette", mC.getResources().getBoolean(R.bool.switch_amulette_DEF));
        nD10= tools.toInt(settings.getString("number_main_dice_dmg", String.valueOf(mC.getResources().getInteger(R.integer.number_main_dice_dmg_DEF))));
        nD8=1; //pour les bandage
        nD6=1; //pour l'amulette
        bonusDmg=getBonusDmg();
    }

    public void setDmgRand() {
        if (manualDiceDmg) {
            putManualImage(10);
            if (aldrassil) { putManualImage(8);  }
            if (amulette) {  putManualImage(6);  }
        } else {
            putImage(10);
            if (aldrassil) {  putImage(8);   }
            if (amulette) {   putImage(6);   }
        }
    }

    private void putManualImage(int dice) {
        int nDice=0;
        List<ImageView> listImg=new ArrayList<>();
        if(dice==10){  nDice=nD10; listImg=listImgD10;  }
        if(dice==8){   nDice=nD8;  listImg=listImgD8;   }
        if(dice==6){   nDice=nD6;  listImg=listImgD6;   }

        for (int i=0;i<nDice;i++){
            ImageView img = new ImageView(mC);
            int drawableId = mC.getResources().getIdentifier("d" + dice + "_main", "drawable", mC.getPackageName());
            img.setImageDrawable( tools.resize(mC,drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
            listImg.add(img);
            setDiceImgListner(img,dice);
        }
    }

    private void putImage(int dice) {
        int nDice=0;
        List<ImageView> listImg=new ArrayList<>();
        List<Integer> listRand=new ArrayList<>();
        if(dice==10){ nDice=nD10; listImg=listImgD10; listRand=randD10;  }
        if(dice==8){  nDice=nD8;  listImg=listImgD8;  listRand=randD8;   }
        if(dice==6){  nDice=nD6;  listImg=listImgD6;  listRand=randD6;   }

        for (int i=0;i<nDice;i++){
            Random rand = new Random();
            int valRand = 1 + rand.nextInt(dice);
            listRand.add(valRand);
            ImageView img = new ImageView(mC);
            int drawableId = mC.getResources().getIdentifier("d" + dice + "_"+String.valueOf(valRand), "drawable", mC.getPackageName());
            img.setImageDrawable( tools.resize(mC,drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
            listImg.add(img);
        }
    }

    private int getBonusDmg() {
        int calcBonusDmg = 0;
        calcBonusDmg +=  tools.toInt(settings.getString("bonus_temp_jet_dmg", String.valueOf(mC.getResources().getInteger(R.integer.bonus_temp_jet_dmg_DEF))));
        calcBonusDmg +=  tools.toInt(settings.getString("attack_dmg_epic", String.valueOf(mC.getResources().getInteger(R.integer.attack_dmg_epic_DEF))));

        if (aquene.getAllStances().isActive("stance_lion")) {
            calcBonusDmg += (int) (1.5 * aquene.getAbilityMod("ability_force"));
        } else {
            calcBonusDmg += aquene.getAbilityMod("ability_force");
        }
        if (aldrassil) {   calcBonusDmg += 2;  }
        if (amulette) {    calcBonusDmg += 5;  }
        return calcBonusDmg;
    }

    private void setDiceImgListner(final ImageView imgDice, final int dice) {
        imgDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DiceDealerDialog(mA, mC, DmgRoll.this, imgDice, dice);
            }
        });
    }

    public void setDmgRand(ImageView img,int dice,int randFromWheel) { //retour du wheelpicker
        List<Integer> listRand=new ArrayList<>();
        if(dice==10){ listRand=randD10; }
        if(dice==8){ listRand=randD8; }
        if(dice==6){ listRand=randD6; }
        listRand.add(randFromWheel);
        setDmgDiceRandImg(img, dice, randFromWheel);
        mListener.onEvent(); //on a refresh une valeur de dès
    }



    public interface OnRefreshEventListener {
        void onEvent();
    }

    public void setRefreshEventListener(OnRefreshEventListener eventListener) {
        mListener = eventListener;
    }

    private void setDmgDiceRandImg(ImageView diceImg, int dice, int randFromWheel) {
        int drawableId = mC.getResources().getIdentifier("d" + dice + "_" + String.valueOf(randFromWheel), "drawable", mC.getPackageName());
        diceImg.setImageDrawable( tools.resize(mC,drawableId, mC.getResources().getDimensionPixelSize(R.dimen.icon_main_dices_combat_launcher_size)));
        diceImg.setOnClickListener(null);
    }

    // Getters

    public List<ImageView> getDmgDiceImgList(int dice) {
        List<ImageView> list=null;
        if (dice == 10) {  list=listImgD10;    }
        if (dice == 8) {   list=listImgD8;     }
        if (dice == 6) {   list=listImgD6;     }
        return list;
    }

    public List<Integer> getDmgDiceValue(int dice) {
        List<Integer> list=null;
        if (dice == 10) {  list=randD10;    }
        if (dice == 8) {   list=randD8;     }
        if (dice == 6) {   list=randD6;     }
        return list;
    }

    public Integer getNDmgDice(int dice) {
        Integer nDice=0;
        if (dice == 10) {  nDice=nD10;    }
        if (dice == 8) {   nDice=nD8;     }
        if (dice == 6) {   nDice=nD6;     }
        return nDice;
    }

    public int getDmgBonus() {
        return getBonusDmg();
    }

    public int getSumPhy() {
        int sumPhy=0;
        for(int i : randD10){
            if(critConfirmed){sumPhy+=i*2;}else{sumPhy+=i;}
        }
        if(critConfirmed){sumPhy+=bonusDmg*2;}else{sumPhy+=bonusDmg;}
        for(int i : randD8){sumPhy+=i;}
        return sumPhy;
    }

    public int getSumFire() {
        int sumFire=0;
        for(int i : randD6){sumFire+=i;}
        return sumFire;
    }
}
