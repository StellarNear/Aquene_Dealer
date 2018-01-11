package stellarnear.aquene_dealer.Perso;

import android.content.Context;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Perso {
    private Abilities baseAbilities;
    private AbilitiesCalculator actualAbilities;
    private AllStances allStances;
    private AllFeats allFeats;
    private AllSkills allSkills;
    private Attacks atq;
    private Ki ki;


    private Context mC;

    public Perso(Context mC){
        this.mC=mC;
        baseAbilities = new Abilities(mC);
        allStances = new AllStances(mC);
        allFeats = new AllFeats(mC);
        allSkills = new AllSkills(mC);
        actualAbilities = new AbilitiesCalculator(baseAbilities,allStances,allFeats,mC);
    }

    public AllStances getAllStances() {
        return allStances;
    }

    public void activateStance(String stanceId){
        Stance selectedStance=allStances.getStance(stanceId);
        if (selectedStance!=null){
            allStances.activateStance(selectedStance);
            actualAbilities = new AbilitiesCalculator(baseAbilities,allStances,allFeats,mC);
        }
    }

    public AbilitiesCalculator getAbilities() {
        return actualAbilities;
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
        baseAbilities.refreshAllAbilities();
        allFeats.refreshAllSwitch();
        allSkills.refreshAllVals();
        actualAbilities = new AbilitiesCalculator(baseAbilities,allStances,allFeats,mC);
    }
}
