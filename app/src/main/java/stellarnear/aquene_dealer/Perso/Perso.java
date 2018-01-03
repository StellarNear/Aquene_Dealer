package stellarnear.aquene_dealer.Perso;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Perso {
    private Carac carac;
    private Feats dons;
    private Skills apt;
    private Attacks atq;
    private Ki ki;
    private AllStances stances;
    private Context mC;

    public Perso(Context mC){
        this.mC=mC;
        stances=new AllStances(mC);
        carac=new Carac(mC);
    }

    public AllStances getStances() {
        return stances;
    }

    public Carac getCarac() {
        return carac;
    }


    public int getCurrentFor(){
        if (getStances().isActive("bear")) {
            return carac.getFOR() +4;
        }else{
            return carac.getFOR();
        }
    }

}
