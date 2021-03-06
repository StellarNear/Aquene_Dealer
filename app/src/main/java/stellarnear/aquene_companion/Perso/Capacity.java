package stellarnear.aquene_companion.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Capacity {
    private String name;
    private String shortname;
    private String type;
    private String descr;
    private String id;
    private Context mC;
    private String dailyUseString;
    private Integer dailyUse=0;
    private boolean infinite=false;
    private String valueString;
    private Integer value=0;
    private String joinedResourceId;
    private Integer nSpendJoinedResource;


    public Capacity(String name,String shortname, String type, String descr, String id,String dailyUse,String valueString,Context mC){
        this.name=name;
        this.shortname=shortname;
        this.type=type;
        this.descr=descr;
        this.valueString=valueString;
        this.id=id;
        this.mC=mC;
        this.dailyUseString = dailyUse;
        if(dailyUse.equalsIgnoreCase("infinite")){
            this.infinite=true;
        }
    }

    public void setDailyUse(Integer dailyUse) {
        this.dailyUse = dailyUse;
    }

    public String getDailyUseString() {
        return dailyUseString;
    }

    public String getJoinedResourceId() {
        return joinedResourceId;
    }

    public Integer getnSpendJoinedResource() {
        return nSpendJoinedResource;
    }

    public String getValueString() {
        return valueString;
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

    public boolean isInfinite() {
        return infinite;
    }

    public int getDailyUse() {
        return dailyUse;
    }

    public boolean isActive(){
        boolean active = false;
        try {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
            active = settings.getBoolean("switch_"+this.id, true);
        } catch ( Exception e) {}
        return active;
    }

    public int getValue() {
        return value;
    }

    public void setValue(Integer val) {
        this.value=val;
    }

    public void setJoinedResource(Integer nSpendJoinedResource, String resId) {
        this.nSpendJoinedResource=nSpendJoinedResource;
        this.joinedResourceId=resId;
    }
}
