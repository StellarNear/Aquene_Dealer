package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jchatron on 05/01/2018.
 */

public class Abilities {
    private List<String> allAbilities
            = Arrays.asList("FOR", "DEX", "CON","INT","SAG","CHA","MS","CA",                "HP","REF","VIG","VOL","BMO","DMD","INIT");
    private List<String> allBaseAbilities
            = Arrays.asList("FOR", "DEX", "CON","INT","SAG","CHA","MS","CA_STUFF","CA_MONK","HP","REF","VIG","VOL");

    private Map<String,Integer> mapAbidVal=new HashMap<>();
    private Map<String,Integer> mapBaseAbidVal=new HashMap<>();

    private AllStances allStances;
    private AllFeats allFeats;
    private Context mC;
    public Abilities(AllStances allStances, AllFeats allFeats, Context mC) {
        this.allStances=allStances;
        this.allFeats=allFeats;
        this.mC=mC;
        setAllBaseAbilities();
    }

    public void setAllBaseAbilities() {
        for (String abiKey : allBaseAbilities){
            mapBaseAbidVal.put(abiKey,readAbility(abiKey));
        }
        refreshAllAbilities();
    }

    private int readAbility(String key){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        int resId = mC.getResources().getIdentifier("carac_base"+key+"_DEF", "integer", mC.getPackageName());
        return toInt(settings.getString("carac_base"+key,String.valueOf(mC.getResources().getInteger(resId))));
    }

    public void refreshAllAbilities() {
        for (String abiKey : allAbilities) {
            int val=0;
            if(abiKey.equals("FOR") && allStances.getCurrentStance()!=null && allStances.getCurrentStance().getId().equals("bear")) {
                val = mapBaseAbidVal.put(abiKey,val)+4;
            } else if (abiKey.equals("CA")) {
                val=mapBaseAbidVal.get("CA_STUFF")+mapBaseAbidVal.get("CA_MONK");
            } else if (abiKey.equals("BMO")) {
                val=mapBaseAbidVal.get("DEX")+10;
            } else if (abiKey.equals("DMD")) {
                val=mapBaseAbidVal.get("DEX")+20;
            } else if (abiKey.equals("INIT")) {
                val=mapBaseAbidVal.get("DEX");
            } else {
                val = mapBaseAbidVal.get(abiKey);
            }
            mapAbidVal.put(abiKey,val);
        }
    }

    public int getMOD(String key) {
        int val=0;
        try {
            val=mapAbidVal.get(key);
        } catch (Exception e){}

        float modFloat=(float) ((val-10)/2.0);
        int mod;
        if (modFloat>=0){
            mod=(int) modFloat;
        } else {
            mod=-1*Math.round(Math.abs(modFloat));
        }
        return mod;
    }

    public int getAbilityScore(String key){
        int val=0;
        try {
            val=mapAbidVal.get(key);
        } catch (Exception e){}
        return val;
    }

    private Integer toInt(String key){
        Integer value=0;
        try {
            value = Integer.parseInt(key);
        } catch (Exception e){  }
        return value;
    }


}
