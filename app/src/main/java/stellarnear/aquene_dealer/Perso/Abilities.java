package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Abilities {
    private int FOR;
    private int DEX;
    private int CON;
    private int INT;
    private int SAG;
    private int CHA;
    private int MS;
    private int CASTUFF;
    private int CAMONK;
    private int HP;
    private int REF;
    private int VIG;
    private int VOL;
    private Context mC;
    public Abilities(Context mC) {
        this.mC = mC;
       refreshAllAbilities();
    }

    public void refreshAllAbilities() {
        setFOR();
        setDEX();
        setCON();
        setINT();
        setSAG();
        setCHA();
        setMS();
        setCASTUFF();
        setCAMONK();
        setHP();
        setREF();
        setVIG();
        setVOL();
    }

    private int readAbility(String key){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        int resId = mC.getResources().getIdentifier(key+"_DEF", "integer", mC.getPackageName());
        return toInt(settings.getString(key,String.valueOf(mC.getResources().getInteger(resId))),key);
    }

    private Integer toInt(String key,String field){
        Integer value;
        try {
            value = Integer.parseInt(key);
        } catch (Exception e){
            Toast toast = Toast.makeText(mC, "Attention la valeur : "+key+"\nDu champ : "+field+"\nEst incorrecte, valeur mise à 0.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
            toast.show();
            value=0;
        }
        return value;
    }

    public int getFOR() {
            return FOR;
    }

    private void setFOR() {

        this.FOR = readAbility("carac_baseFOR");
    }

    public int getDEX() {
        return DEX;
    }

    private void setDEX() {
        this.DEX = readAbility("carac_baseDEX");
    }

    public int getCON() {
        return CON;
    }

    private void setCON() {
        this.CON = readAbility("carac_baseCON");
    }

    public int getINT() {
        return INT;
    }

    public void setINT() {
        this.INT = readAbility("carac_baseINT");
    }

    public int getSAG() {
        return SAG;
    }

    public void setSAG() {
        this.SAG = readAbility("carac_baseSAG");
    }

    public int getCHA() {
        return CHA;
    }

    public void setCHA() {
        this.CHA = readAbility("carac_baseCHA");
    }

    public int getCASTUFF() {
        return CASTUFF;
    }

    public void setCASTUFF() {
        this.CASTUFF = readAbility("carac_baseCA_STUFF");
    }

    public int getCAMONK() {
        return CASTUFF;
    }

    public void setCAMONK() {
        this.CASTUFF = readAbility("carac_baseCA_MONK");
    }

    public int getMS() {
        return MS;
    }

    public void setMS() {
        this.MS = readAbility("carac_baseMS");
    }

    public int getHP() {
        return HP;
    }

    public void setHP() {
        this.HP = readAbility("carac_baseHP");
    }


    public int getREF() {
        return REF;
    }

    public void setREF() {
        this.REF = readAbility("carac_baseREF");
    }

    public int getVIG() {
        return VIG;
    }

    public void setVIG() {
        this.VIG = readAbility("carac_baseVIG");
    }

    public int getVOL() {
        return VOL;
    }

    public void setVOL() {
        this.VOL = readAbility("carac_baseVOL");
    }





}
