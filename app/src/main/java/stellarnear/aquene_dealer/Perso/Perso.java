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
    private AllAttacks allAttacks;
    private Ki ki;
    private Context mC;

    public Perso(Context mC) {
        this.mC = mC;
        allStances = new AllStances(mC);
        allFeats = new AllFeats(mC);
        allAbilities = new AllAbilities(mC);
        allSkills = new AllSkills(mC);
        allAttacks = new AllAttacks(mC);
    }

    public AllStances getAllStances() {
        return allStances;
    }

    public void activateStance(String stanceId) {
        Stance selectedStance = allStances.getStance(stanceId);
        if (selectedStance != null) {
            allStances.activateStance(selectedStance);
        }
    }

    public AllAbilities getAllAbilities() {
        return allAbilities;
    }

    public Integer getAbilityScore(String abiId) {
        int abiScore =0;
        if (allAbilities.getAbi(abiId)!=null){
            abiScore=allAbilities.getAbi(abiId).getValue();

            if (abiId.equalsIgnoreCase("init") && featIsActive("init")) {
                abiScore += 4;
            }

            if (abiId.equalsIgnoreCase("force") && allStances.getCurrentStance() != null && allStances.getStance("bear").isActive()) {
                abiScore += 7;
            }

            if (abiId.equalsIgnoreCase("ca")) {
                if (allAttacks.getCombatMode().equals("def")){
                    if(getAllSkills().getSkill("acrob").getRank()>=23){
                        abiScore += 5;
                    } else if(getAllSkills().getSkill("acrob").getRank()>=3){
                        abiScore += 3;
                    } else {
                        abiScore += 2;
                    }
                }
                if (allAttacks.getCombatMode().equals("totaldef")){
                    if(getAllSkills().getSkill("acrob").getRank()>=23){
                        abiScore += 8;
                    } else if(getAllSkills().getSkill("acrob").getRank()>=3){
                        abiScore += 6;
                    } else {
                        abiScore += 4;
                    }
                }

            }
        }
        return abiScore;
    }

    public Integer getAbilityMod(String abiId) {
        int abiMod = 0;
        Ability abi = allAbilities.getAbi(abiId);

        if (abi!=null && abi.getType().equalsIgnoreCase("base")) {
            int abiScore=getAbilityScore(abiId);

            float modFloat = (float) ((abiScore - 10.) / 2.0);
            if (modFloat >= 0) {
                abiMod = (int) modFloat;
            } else {
                abiMod = -1 * Math.round(Math.abs(modFloat));
            }
        }
        return abiMod;
    }

    public Integer getSkillBonus(String skillId) {
        int bonusTemp = allSkills.getSkill(skillId).getBonus();
        if (skillId.equalsIgnoreCase("skill_acrob")) {
            bonusTemp += allAbilities.getAbi("ability_lvl").getValue();
        }   //on ajoute le niveau de moine au jet d'acrob
        return bonusTemp;
    }

    public AllFeats getAllFeats() {
        return allFeats;
    }

    public AllSkills getAllSkills() {
        return allSkills;
    }

    public AllAttacks getAllAttacks(){return this.allAttacks;}


    public boolean featIsActive(String featId) {
        Feat feat = allFeats.getFeat(featId);
        boolean active = feat.isActive();
        if (allStances.getCurrentStance() != null && allStances.getCurrentStance().getFeatId().contains(featId)) {
            active = true;
        }
        return active;
    }

    public void refresh() {
        allFeats.refreshAllSwitch();
        allSkills.refreshAllVals();
        allAbilities.refreshAllAbilities();
        allAttacks.refreshAllAttacks();
    }


}
