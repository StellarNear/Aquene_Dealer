package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 04/01/2018.
 */

public class Feat {
    private String name;
    private String type;
    private String descr;
    private String id;
    private String stance_id;
    private String image_path;
    private Context mC;
    private Boolean active;

    public Feat(String name, String type, String descr, String id, String stance_id, Context mC){
        this.name=name;
        this.type=type;
        this.descr=descr;
        this.id=id;
        this.stance_id =stance_id;
        this.image_path=id+"_feat_img";
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public boolean isActive(){
       return active;
    }

    public String getStanceId() {
        return stance_id;
    }

    public void refreshSwitch() {
        boolean val = false;
        try {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
            int switch_def_id = mC.getResources().getIdentifier(this.id, "bool", mC.getPackageName());
            boolean switch_def = mC.getResources().getBoolean(switch_def_id);
            val = settings.getBoolean(this.id, switch_def);
        } catch ( Exception e) {}
        this.active= val;
    }
}

