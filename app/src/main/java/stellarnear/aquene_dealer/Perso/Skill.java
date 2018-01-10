package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by jchatron on 10/01/2018.
 */

public class Skill {
    private String name;
    private String type;
    private String descr;
    private String id;
    private int val;
    private int bonus;
    private String imagePath;
    private Context mC;

    public Skill(String name, String type, String descr, String id, Context mC){
        this.name=name;
        this.type=type;
        this.descr=descr;
        this.id=id;
        this.imagePath =id+"_skill_img";
        this.mC=mC;
        refreshVals();
    }



    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    private Integer toInt(String key){
        Integer value=0;
        try {
            value = Integer.parseInt(key);
        } catch (Exception e){ }
        return value;
    }

    public void refreshVals() {
        refreshVal();
        refreshBonus();
    }

    private void refreshBonus() {
        int bonusTemp=0;
        try {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
            int bonusDefId = mC.getResources().getIdentifier(this.id+"_bonusDEF", "integer", mC.getPackageName());
            int bonusDef = mC.getResources().getInteger(bonusDefId);
            bonusTemp = settings.getInt(this.id+"_bonus", bonusDef);
        } catch ( Exception e) {}
        this.bonus= bonusTemp;
    }
    public int getBonus(){
        return this.bonus;
    }

    private void refreshVal() {
        int valTemp=0;
        try {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
            int valDefId = mC.getResources().getIdentifier(this.id+"_rankDEF", "integer", mC.getPackageName());
            int valDef = mC.getResources().getInteger(valDefId);
            valTemp = toInt(settings.getString(this.id+"_rank", String.valueOf(valDef)));
        } catch ( Exception e) {}
        this.val= valTemp;
    }

    public int getVal(){
        return this.val;
    }
}