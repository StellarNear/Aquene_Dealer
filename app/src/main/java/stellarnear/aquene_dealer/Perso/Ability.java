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
    private String imagePath;
    private Context mC;
    private String shortname;
    private int value=0;
    private String valueTxt;

    public Ability(String name, String shortname, String type, String descr, String id, Context mC) {
        this.name = name;
        this.type = type;
        this.descr = descr;
        this.id = id;
        this.shortname = shortname;
        this.imagePath = id + "_abi_img";
        this.mC = mC;
    }

    public String getName() {
        return name;
    }

    public String getShortname() {
        return shortname;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}

