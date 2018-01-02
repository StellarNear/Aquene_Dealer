package stellarnear.aquene_dealer.Perso;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;

import java.io.Serializable;

import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 27/12/2017.
 */

public class Stance implements Serializable {
    String name;
    String shortname;
    String type;
    String descr;
    String selector_path;
    Boolean active;
    public Stance(String name, String shortname, String type, String descr, String selector_path){
        this.active=false;
        this.name=name;
        this.shortname=shortname;
        this.type=type;
        this.descr=descr;
        this.selector_path=selector_path;
    }

    public String getSelector_path(){
        return selector_path;
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

}
