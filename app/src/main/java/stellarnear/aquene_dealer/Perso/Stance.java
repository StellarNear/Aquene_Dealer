package stellarnear.aquene_dealer.Perso;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;

import java.io.Serializable;

import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 27/12/2017.
 */

public class Stance {
    String name;
    String shortname;
    String type;
    String descr;
    String id;
    String selector_path;
    String selected_image_path;
    Boolean active;
    public Stance(String name, String shortname, String type, String descr, String id){
        this.active=false;
        this.name=name;
        this.shortname=shortname;
        this.type=type;
        this.descr=descr;
        this.id=id;
        this.selector_path=id+"_stance_selector";
        this.selected_image_path=id+"_select";
    }

    public String getSelector_path(){
        return selector_path;
    }
    public String getSelected_image_path(){
        return selected_image_path;
    }
    public String getDescr(){
        return descr;
    }
    public String getName(){
        return name;
    }
    public String getShortName(){
        return shortname;
    }
    public String getType(){
        return type;
    }

    public boolean isActive(){
        if (active) {return true;} else {return false;}
    }

    public void activate() {
        this.active=true;
    }

    public void desactivate() {
        this.active=false;
    }

    public String getId() {
        return id;
    }
}
