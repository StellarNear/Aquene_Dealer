package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 04/01/2018.
 */

public class Resource {
    private String name;
    private String shortname;
    private String descr;
    private String id;
    private Context mC;
    private int max;
    private int current;
    private boolean testable;
    private boolean hide;
    private Drawable img;
    private Tools tools=new Tools();

    public Resource(String name,String shortname, String descr,Boolean testable,Boolean hide, String id, Context mC){
        this.name=name;
        this.shortname=shortname;
        this.testable=testable;
        this.hide=hide;
        this.descr=descr;
        this.id=id;
        this.mC=mC;
        int imgId=mC.getResources().getIdentifier(id, "drawable", mC.getPackageName());
        if(imgId!=0){
            this.img = mC.getDrawable(imgId);
        } else {
            this.img=null;
        }
    }

    public String getName() {
        return this.name;
    }

    public Boolean isTestable(){return this.testable;}

    public String getDescr() {
        return this.descr;
    }

    public String getId() {
        return this.id;
    }

    public Drawable getImg(){return this.img;}

    public void setMax(int max) {
        this.max=max;
    }

    public Integer getMax(){return this.max;}

    public Integer getCurrent(){
        return this.current;
    }

    public void spend(Integer cost){
        if ( (this.current-cost <=0) && (!this.id.equalsIgnoreCase("resource_hp")) ){
            this.current=0;
        } else {
            this.current-=cost;
        }
    }

    public void resetCurrent() {
        this.current=this.max;
    }

    public void earn(int amount) {
        if(this.id.equalsIgnoreCase("resource_hp")){
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
            int shield = tools.toInt(settings.getString( "resource_hp_shield", String.valueOf(mC.getResources().getInteger(R.integer.resource_hp_shield_def))));
            if (this.current+amount >= this.max + shield){
                this.current=this.max+shield;
            } else {
                this.current+=amount;
            }
        } else {
            if (this.current+amount >= this.max){
                this.current=this.max;
            } else {
                this.current+= amount;
            }
        }
    }

    public String getShortname() {
        String shortReturn;
        if (this.shortname!="")
        {
            shortReturn=this.shortname;
        } else {
            shortReturn=this.name;
        }
        return shortReturn;
    }

    public boolean isHidden() {
        return this.hide;
    }
}

