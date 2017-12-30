package stellarnear.aquene_dealer.Perso;

import android.content.Context;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Perso {
    Carac cara;
    Feats dons;
    Skills apt;
    Attacks atq;
    Ki ki;
    AllStances stances;
    Context mC;
    public Perso(Context mC){
        this.mC=mC;
        stances=new AllStances(mC);
    }

    public AllStances getStances() {
        return stances;
    }
}
