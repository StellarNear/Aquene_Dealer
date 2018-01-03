package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Carac {
    private int FOR;
    private int DEX;
    private int CON;
    private int INT;
    private int SAG;
    private int CHA;
    private int CA;
    private int BMO;
    private int DMD;
    private Context mC;
    public Carac(Context mC) {
        this.mC = mC;
       refreshAllcaracs();
    }

    public void refreshAllcaracs() {
        setFOR();
        setDEX();
        setCON();
        setINT();
        setSAG();
        setCHA();
        setCA();
        setBMO();
        setDMD();
    }

    public int getFOR() {
            return FOR;
    }

    public void setFOR() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        int FOR_val = toInt(settings.getString("carac_baseFOR",String.valueOf(mC.getResources().getInteger(R.integer.carac_baseFOR_DEF))),"Force de base");
        this.FOR = FOR_val;
    }

    public int getDEX() {
        return DEX;
    }

    public void setDEX() {
        this.DEX = DEX;
    }

    public int getCON() {
        return CON;
    }

    public void setCON() {
        this.CON = CON;
    }

    public int getINT() {
        return INT;
    }

    public void setINT() {
        this.INT = INT;
    }

    public int getSAG() {
        return SAG;
    }

    public void setSAG() {
        this.SAG = SAG;
    }

    public int getCHA() {
        return CHA;
    }

    public void setCHA() {
        this.CHA = CHA;
    }

    public int getCA() {
        return CA;
    }

    public void setCA() {
        this.CA = CA;
    }

    public int getBMO() {
        return BMO;
    }

    public void setBMO() {
        this.BMO = BMO;
    }

    public int getDMD() {
        return DMD;
    }

    public void setDMD() {
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
