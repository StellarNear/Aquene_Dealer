package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jchatron on 10/01/2018.
 */

public class Skill {
    private String name;
    private String abilityDependence;
    private String descr;
    private String id;
    private int rank;
    private int bonus;
    private String imagePath;
    private Abilities abilities;
    private Context mC;

    public Skill(String name, String abilityDependence, String descr, String id,Abilities abilities, Context mC){
        this.name=name;
        this.abilityDependence = abilityDependence;
        this.descr=descr;
        this.id=id;
        this.imagePath =id+"_skill_img";
        this.abilities=abilities;
        this.mC=mC;
        refreshVals();
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getAbilityDependence() {
        return this.abilityDependence;
    }

    private Integer toInt(String key){
        Integer value=0;
        try {
            value = Integer.parseInt(key);
        } catch (Exception e){ }
        return value;
    }

    public void refreshVals() {
        refreshRank();
        refreshBonus();
    }



    private void refreshRank() {
        int valTemp=0;
        try {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
            int valDefId = mC.getResources().getIdentifier(this.id+"_rankDEF", "integer", mC.getPackageName());
            int valDef = mC.getResources().getInteger(valDefId);
            valTemp = toInt(settings.getString(this.id+"_rank", String.valueOf(valDef)));
        } catch ( Exception e) {}
        this.rank = valTemp;
    }

    public int getRank(){
        return this.rank;
    }


    private void refreshBonus() {
        int bonusTemp=0;
        try {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
            int bonusDefId = mC.getResources().getIdentifier(this.id+"_bonusDEF", "integer", mC.getPackageName());
            int bonusDef = mC.getResources().getInteger(bonusDefId);
            bonusTemp = settings.getInt(this.id+"_bonus", bonusDef);
            if(this.id.equals("acrob")){bonusTemp+= abilities.getScore("LVL");}   //on ajoute le niveau de moine au jet d'acrob
        } catch ( Exception e) {}
        this.bonus= bonusTemp;
    }
    public int getBonus(){
        return this.bonus;
    }
}