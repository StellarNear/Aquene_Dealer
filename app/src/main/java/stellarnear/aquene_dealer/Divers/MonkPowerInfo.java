package stellarnear.aquene_dealer.Divers;

import android.content.Context;

/**
 * Created by jchatron on 16/02/2018.
 */

public class MonkPowerInfo {
    private String name;
    private String descr;
    private int level;
    private String id;
    public MonkPowerInfo(String name, String descr,int level, String id) {
        this.name = name;
        this.descr = descr;
        this.level=level;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public int getLevel() { return level;}

    public String getId() {
        return id;
    }
}
