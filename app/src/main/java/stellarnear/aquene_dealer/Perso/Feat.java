package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jchatron on 04/01/2018.
 */

public class Feat {
    private String name;
    private String type;
    private String descr;
    private String id;
    private String imagePath;
    private Context mC;
    private Boolean active;

    public Feat(String name, String type, String descr, String id, Context mC){
        this.name=name;
        this.type=type;
        this.descr=descr;
        this.id=id;
        this.imagePath =id+"_feat_img";
        this.mC=mC;
        refreshSwitch();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isActive(){
       return active;
    }

    public void refreshSwitch() {
        boolean val = false;
        try {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
            int switchDefId = mC.getResources().getIdentifier(this.id, "bool", mC.getPackageName());
            boolean switchDef = mC.getResources().getBoolean(switchDefId);
            val = settings.getBoolean(this.id, switchDef);
        } catch ( Exception e) {}
        this.active= val;
    }
}

