package stellarnear.aquene_dealer.Perso;

import android.content.Context;

/**
 * Created by jchatron on 04/01/2018.
 */

public class Attack {
    private String name;
    private String shortname;
    private String type;
    private String descr;
    private Boolean save;
    private String id;
    private Context mC;


    public Attack(String name,String shortname, String type, String descr,Boolean save, String id, Context mC){
        this.name=name;
        this.shortname=shortname;
        this.type=type;
        this.descr=descr;
        this.save=save;
        this.id=id;
        this.mC=mC;
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

    public Boolean hasSave(){
        return this.save;
    }
}

