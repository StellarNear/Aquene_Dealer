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
    private Boolean active;
    private Context mC;
    public Stance(String name, String shortname, String type, String descr, String id,String featId,Context mC){
        this.active=false;
        this.name=name;
        this.shortname=shortname;
        this.type=type;
        this.descr=descr;
        this.id=id;
        this.featId=featId;
        this.mC=mC;
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
}
