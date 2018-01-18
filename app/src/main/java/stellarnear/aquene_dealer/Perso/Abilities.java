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
            = Arrays.asList("FOR", "DEX", "CON","INT","SAG","CHA","LVL","MS","CA",                "HP","REF","VIG","VOL","BMO","DMD","INIT","HEROIC","RM","REDUC","REGEN");
    private List<String> allBaseAbilities
            = Arrays.asList("FOR", "DEX", "CON","INT","SAG","CHA","LVL","MS","CA_STUFF","CA_MONK","HP","REF","VIG","VOL","HEROIC","REDUC","REGEN");

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
            //(abiKey.equals("FOR") && allStances.getCurrentStance()!=null && allStances.getCurrentStance().getId().equals("bear") pour test les stance en meme temps
            int val=0;
            if (abiKey.equals("CA")) {
                val=mapBaseAbidVal.get("CA_STUFF")+mapBaseAbidVal.get("CA_MONK")+getMOD("DEX")+10;
            } else if (abiKey.equals("BMO")) {
                val = mapBaseAbidVal.get("LVL")+getMOD("FOR");
            } else if (abiKey.equals("DMD")) {
                val = mapBaseAbidVal.get("LVL")+getMOD("FOR")+10+getMOD("DEX");
            } else if (abiKey.equals("INIT")) {
                val=getMOD("DEX");
                if (allFeats.isActive("init")){val+=4;}
            } else if (abiKey.equals("RM")) {
                val = mapBaseAbidVal.get("LVL") + 10;
            } else {
                val = mapBaseAbidVal.get(abiKey);
            }
            mapAbidVal.put(abiKey,val);
        }
    }

    public void refreshAllAbilities(String mod) {
        int val=0;
        if( allStances.getCurrentStance()!=null && allStances.getCurrentStance().getId().equals("bear")) {
            val = mapBaseAbidVal.put("FOR",val)+4;
        }
        mapAbidVal.put("FOR",val);
    }

    public int getMOD(String key) {
        List<String> modOk
                = Arrays.asList("FOR", "DEX", "CON","INT","SAG","CHA");
        int mod;
        if (modOk.contains(key)){
            int val=0;
            try {
                val=mapAbidVal.get(key);
            } catch (Exception e){}

            float modFloat=(float) ((val-10)/2.0);
            if (modFloat>=0){
                mod=(int) modFloat;
            } else {
                mod=-1*Math.round(Math.abs(modFloat));
            }
        } else { mod=0;}

        return mod;
    }

    public int getScore(String key){
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
