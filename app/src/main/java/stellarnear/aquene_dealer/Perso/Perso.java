package stellarnear.aquene_dealer.Perso;

import android.content.Context;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Perso {
    Caracteristiques cara;
    Dons dons;
    Aptitudes apt;
    Attaques atq;
    Ki ki;
    AllPostures stances;
    Context mC;
    public Perso(Context mC){
        this.mC=mC;
        stances=new AllPostures(mC);
    }

    public AllPostures getStances() {
        return stances;
    }
}
