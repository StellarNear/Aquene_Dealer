package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;


/**
 * Created by jchatron on 04/01/2018.
 */

public class Resource {
    private String name;
    private String shortname;
    private String id;
    private Context mC;
    private int max = 0;
    private int current;
    private int shield=0;//pour les hps
    private boolean testable;
    private boolean hide;
    private Drawable img;
    private SharedPreferences settings;

    public Resource(String name, String shortname, Boolean testable, Boolean hide, String id, Context mC) {
        this.name = name;
        this.shortname = shortname;
        this.testable = testable;
        this.hide = hide;
        this.id = id;
        this.mC = mC;
        this.settings = PreferenceManager.getDefaultSharedPreferences(mC);
        int imgId = mC.getResources().getIdentifier(id, "drawable", mC.getPackageName());
        if (imgId != 0) {
            this.img = mC.getDrawable(imgId);
        } else {
            this.img = null;
        }
    }

    public String getName() {
        return this.name;
    }

    public Boolean isTestable() {
        return this.testable;
    }

    public String getId() {
        return this.id;
    }

    public Drawable getImg() {
        return this.img;
    }

    public void setMax(int max) {
        this.max = max;
        if(this.current>this.max){this.current=this.max;}
    }

    public Integer getMax() {
        return this.max;
    }

    public Integer getCurrent() {
        return this.current;
    }

    public void spend(Integer cost) {
        if (this.id.equalsIgnoreCase("resource_hp")) {
            if (this.shield <= cost) {
                this.current -= (cost - this.shield);
                this.shield = 0;
            } else {
                this.shield -= cost;
            }
        } else{
            if (this.current - cost <= 0) {
                this.current = 0;
            } else {
                this.current -= cost;
            }
        }
        saveCurrentToSettings();
    }

    public void resetCurrent() {
        this.current = this.max;
        saveCurrentToSettings();
    }

    public void loadCurrentFromSettings() {
        int currentVal = settings.getInt(this.id + "_current", -99);
        if (currentVal != -99) {
            this.current = currentVal;
        } else {
            resetCurrent();
        }
    }

    public void saveCurrentToSettings() {
        settings.edit().putInt(this.id + "_current", this.current).apply();
    }

    public void earn(int amount) {
        if (this.current + amount >= this.max) {
            this.current = this.max;
        } else {
            this.current += amount;
        }
        saveCurrentToSettings();
    }

    public void shield(int amount){
        if (this.id.equalsIgnoreCase("resource_hp")){
            this.shield+=amount;
        }
    }

    public int getShield(){
        int val = 0;
        if (this.id.equalsIgnoreCase("resource_hp")){
            val=this.shield;
        }
        return val;
    }

    public String getShortname() {
        String shortReturn;
        if (this.shortname != "") {
            shortReturn = this.shortname;
        } else {
            shortReturn = this.name;
        }
        return shortReturn;
    }

    public boolean isHidden() {
        return this.hide;
    }


}

