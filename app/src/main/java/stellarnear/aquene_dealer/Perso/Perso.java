package stellarnear.aquene_dealer.Perso;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Perso implements Serializable {
    Carac cara;
    Feats dons;
    Skills apt;
    Attacks atq;
    Ki ki;
    AllStances stances;
    transient Context mC;
    public Perso(Context mC){
        this.mC=mC;
        stances=new AllStances(mC);
    }

    public AllStances getStances() {
        return stances;
    }
}
