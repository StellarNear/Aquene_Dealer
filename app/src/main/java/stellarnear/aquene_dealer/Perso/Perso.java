package stellarnear.aquene_dealer.Perso;

import android.content.Context;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Perso {
    private Carac baseCarac;
    private AllFeats allFeats;
    private Skills apt;
    private Attacks atq;
    private Ki ki;
    private AllStances allStances;

    private Context mC;

    public Perso(Context mC){
        this.mC=mC;
        allStances =new AllStances(mC);
        baseCarac=new Carac(mC);
        allFeats =new AllFeats(mC);
    }

    public AllStances getAllStances() {
        return allStances;
    }

    public CaracCalculator getCarac() {
        return new CaracCalculator(baseCarac,allStances,allFeats);
    }

    public AllFeats getAllFeats() {
        return allFeats;
    }

    public boolean featIsActive(String feat_id){
        Feat feat = allFeats.getFeat(feat_id);
        boolean active=feat.isActive();
        if (allStances.getCurrentStance()!=null && allStances.getCurrentStance().getId().equals(feat.getStanceId())){
            active=true;
        }
        return active;
    }


    public void refresh() {
        baseCarac.refreshAllcaracs();
        allFeats.refreshAllSwitch();
    }
}
