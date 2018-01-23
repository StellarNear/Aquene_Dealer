package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jchatron on 04/01/2018.
 */

public class Ability {
    private String name;
    private String type;
    private String descr;
    private String id;
    private Context mC;
    private String shortname;
    private int value=0;
    private boolean testable;
    private boolean focusable;

    public Ability(String name, String shortname, String type, String descr,Boolean testable,Boolean focusable, String id, Context mC) {
        this.name = name;
        this.type = type;
        this.descr = descr;
        this.testable=testable;
        this.focusable=focusable;
        this.id = id;
        this.shortname = shortname;
        this.mC = mC;
    }

    public String getName() {
        return name;
    }

    public String getShortname() {
        String returnVal="";
        if (this.shortname.equalsIgnoreCase("")){
            returnVal=this.name;
        } else {
            returnVal=this.shortname;
        }
        return returnVal;
    }

    public String getType() {
        return type;
    }

    public String getDescr() {
        return descr;
    }

    public String getId() {
        return id;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isTestable(){
        return this.testable;
    }

    public boolean isFocusable(){
        return this.focusable;
    }

}

