package stellarnear.aquene_dealer.Perso;

import android.content.Context;

/**
 * Created by jchatron on 26/12/2017.
 */

public class MythicCapacity {
    private String name;
    private String descr;
    private String id;
    public MythicCapacity(String name, String descr, String id)
    {
        this.name=name;
        this.descr=descr;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public String getId(){return id;}
}
