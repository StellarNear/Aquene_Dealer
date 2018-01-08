package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;

import java.io.Serializable;

import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 27/12/2017.
 */

public class Stance {
    private String name;
    private String shortname;
    private String type;
    private String descr;
    private String id;
    private String featId;
    private String selector_path;
    private String selected_image_path;
    private Boolean active;
    private Drawable selector;
    private Context mC;
    public Stance(String name, String shortname, String type, String descr, String id,String featId,Context mC){
        this.active=false;
        this.name=name;
        this.shortname=shortname;
        this.type=type;
        this.descr=descr;
        this.id=id;
        this.featId=featId;
        this.selector_path=id+"_stance_selector";
        this.selected_image_path=id+"_select";
        this.mC=mC;
        this.selector = mC.getDrawable( mC.getResources().getIdentifier(this.selector_path, "drawable", mC.getPackageName()));
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
        return active;
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

    public String getFeatId() {return featId;}

    public Drawable getDrawableSelector() {
        return this.selector;
    }
}
