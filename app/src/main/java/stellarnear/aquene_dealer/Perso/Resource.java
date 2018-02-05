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
    private String descr;
    private String id;
    private Context mC;
    private int max;
    private int current;
    private boolean testable;
    private Drawable img;

    public Resource(String name, String descr,Boolean testable, String id, Context mC){
        this.name=name;
        this.testable=testable;
        this.descr=descr;
        this.id=id;
        this.mC=mC;
        int imgId=mC.getResources().getIdentifier(id, "drawable", mC.getPackageName());
        this.img = mC.getDrawable(imgId);
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
        if (this.current-cost <=0){
            this.current=0;
        } else {
            this.current-=cost;
        }
    }

    public void resetCurrent() {
        this.current=this.max;
    }

    public void earn(int amount) {
        if (this.current+amount >= this.max){
            this.current=this.max;
        } else {
            this.current+= amount;
        }
    }
}

