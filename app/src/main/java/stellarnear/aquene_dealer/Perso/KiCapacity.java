package stellarnear.aquene_dealer.Perso;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jchatron on 26/12/2017.
 */

public class KiCapacity {
    private Context mC;
    private String name;
    private int cost;
    private String descr;
    private String id;
    public KiCapacity(String name, int cost,String descr,String id,Context mC)
    {
        this.mC = mC;
        this.name=name;
        this.cost=cost;
        this.descr=descr;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getDescr() {
        return descr;
    }

    public String getId(){return id;}
}
