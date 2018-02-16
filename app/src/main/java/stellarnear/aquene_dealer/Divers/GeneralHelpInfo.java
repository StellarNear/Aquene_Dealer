package stellarnear.aquene_dealer.Divers;

import android.content.Context;

/**
 * Created by jchatron on 16/02/2018.
 */

public class GeneralHelpInfo {
    private String name;
    private String descr;
    private String id;
    private Context mC;
    public GeneralHelpInfo(String name, String descr, String id, Context mC) {
        this.name = name;
        this.descr = descr;
        this.id = id;
        this.mC =  mC;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public String getId() {
        return id;
    }
}
