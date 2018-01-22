package stellarnear.aquene_dealer.Perso;

import android.content.Context;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Perso {
    private AllAbilities allAbilities;
    private AllStances allStances;
    private AllFeats allFeats;
    private AllSkills allSkills;
    private Attacks atq;
    private Ki ki;
    private Context mC;

    public Perso(Context mC){
        this.mC=mC;
        allStances = new AllStances(mC);
        allFeats = new AllFeats(mC);
        allAbilities = new AllAbilities(allStances,allFeats,mC);
        allSkills = new AllSkills(allAbilities,mC);
    }

    public AllStances getAllStances() {
        return allStances;
    }

    public void activateStance(String stanceId){
        Stance selectedStance=allStances.getStance(stanceId);
        if (selectedStance!=null){
            allStances.activateStance(selectedStance);
        }
    }

    public AllAbilities getAllAbilities() {
        return allAbilities;
    }

    public AllFeats getAllFeats() {
        return allFeats;
    }

    public AllSkills getAllSkills() {
        return allSkills;
    }


    public boolean featIsActive(String featId){
        Feat feat = allFeats.getFeat(featId);
        boolean active=feat.isActive();
        if (allStances.getCurrentStance()!=null && allStances.getCurrentStance().getFeatId().contains(featId)){
            active=true;
        }
        return active;
    }

    public void refresh() {
        allFeats.refreshAllSwitch();
        allSkills.refreshAllVals();
        allAbilities.refreshAllAbilities();
    }
}
