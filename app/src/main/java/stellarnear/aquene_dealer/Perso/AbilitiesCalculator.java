package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.util.Log;
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

    public int getMOD(String key) {
        int val=0;
        switch (key){
            case "FOR":
                val=this.FOR;
                break;
            case "DEX":
                val=this.DEX;
                break;
            case "CON":
                val=this.CON;
                break;
            case "INT":
                val=this.INT;
                break;
            case "SAG":
                val=this.SAG;
                break;
            case "CHA":
                val=this.CHA;
                break;
        }
        
        float modFloat=(float) ((val-10)/2.0);
        int mod=0;
        if (modFloat>=0){
            mod=(int) modFloat;
        } else {
            mod=-1*Math.round(Math.abs(modFloat));
        }
        return mod;
    }

    private void setAll() {
        setFOR();
        setDEX();
        setCON();
        setINT();
        setSAG();
        setCHA();
    }




    public void setFOR() {
        int val = baseAbilities.getFOR();
        if (allStances.isActive("bear")) {
            val +=4;
        }
        this.FOR = val;
    }

    public void setDEX() {
        this.DEX = baseAbilities.getDEX();
    }

    public void setCON() {
        this.CON = baseAbilities.getCON();
    }

    public void setINT() {
        this.INT = baseAbilities.getINT();
    }

    public void setSAG() {
        this.SAG = baseAbilities.getSAG();
    }

    public void setCHA() {
        this.CHA = baseAbilities.getCHA();
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
