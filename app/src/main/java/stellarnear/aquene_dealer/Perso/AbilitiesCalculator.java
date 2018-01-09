package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by jchatron on 05/01/2018.
 */

public class AbilitiesCalculator {
    private int FOR;
    private int DEX;
    private int CON;
    private int INT;
    private int SAG;
    private int CHA;
    private int MS;
    private int CA;
    private int HP;
    private int BMO;
    private int DMD;
    private int REF;
    private int VIG;
    private int VOL;
    private int INIT;

    private Abilities baseAbilities;
    private AllStances allStances;
    private AllFeats allFeats;
    private Context mC;
    public AbilitiesCalculator(Abilities baseAbilities, AllStances allStances, AllFeats allFeats, Context mC) {
        this.baseAbilities = baseAbilities;
        this.allStances=allStances;
        this.allFeats=allFeats;
        setAll();


    }

    private void setAll() {
        setFOR();
    }


    public int getFOR() {
        return FOR;
    }

    public void setFOR() {
        int val= baseAbilities.getFOR();
        if (allStances.isActive("bear")) {
            val +=4;
        }
        this.FOR = val;
    }

    public int getDEX() {
        return DEX;
    }

    public void setDEX(int DEX) {
        this.DEX = DEX;
    }

    public int getCON() {
        return CON;
    }

    public void setCON(int CON) {
        this.CON = CON;
    }

    public int getINT() {
        return INT;
    }

    public void setINT(int INT) {
        this.INT = INT;
    }

    public int getSAG() {
        return SAG;
    }

    public void setSAG(int SAG) {
        this.SAG = SAG;
    }

    public int getCHA() {
        return CHA;
    }

    public void setCHA(int CHA) {
        this.CHA = CHA;
    }

    public int getCA() {
        return CA;
    }

    public void setCA(int CA) {
        this.CA = CA;
    }

    public int getBMO() {
        return BMO;
    }

    public void setBMO(int BMO) {
        this.BMO = BMO;
    }

    public int getDMD() {
        return DMD;
    }

    public void setDMD(int DMD) {
        this.DMD = DMD;
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


}
