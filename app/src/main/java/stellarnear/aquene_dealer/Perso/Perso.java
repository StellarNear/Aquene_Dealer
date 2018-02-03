package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Perso {
    private AllAbilities allAbilities;
    private AllStances allStances;
    private AllFeats allFeats;
    private AllSkills allSkills;
    private AllAttacks allAttacks;
    private AllKiCapacities allKiCapacities;
    private Ressources ressources;
    private Context mC;

    public Perso(Context mC) {
        this.mC = mC;
        allStances = new AllStances(mC);
        allFeats = new AllFeats(mC);
        allAbilities = new AllAbilities(mC);
        allSkills = new AllSkills(mC);
        allAttacks = new AllAttacks(mC);
        allKiCapacities = new AllKiCapacities(mC);
        ressources = new Ressources(mC);
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
        int abiScore = 0;
        if (allAbilities.getAbi(abiId) != null) {
            abiScore = allAbilities.getAbi(abiId).getValue();

            if (abiId.equalsIgnoreCase("init") && featIsActive("init")) {
                abiScore += 4;
            }

            if (abiId.equalsIgnoreCase("force") && allStances.getCurrentStance() != null && allStances.getStance("bear").isActive()) {
                abiScore += 7;
            }

            if (abiId.equalsIgnoreCase("ca")) {
                if (allAttacks.getCombatMode().equals("def")) {
                    abiScore += getCaBonusCombatMode("def");
                } else if (allAttacks.getCombatMode().equals("totaldef")) {
                    abiScore += getCaBonusCombatMode("totaldef");
                }

            }
        }
        return abiScore;
    }

    private int getCaBonusCombatMode(String mode) {
        int caBonus = 0;
        if (mode.equalsIgnoreCase("def")) {
            if (getAllSkills().getSkill("skill_acrob").getRank() >= 23) {
                caBonus += 5;
            } else if (getAllSkills().getSkill("skill_acrob").getRank() >= 3) {
                caBonus += 3;
            } else {
                caBonus += 2;
            }
        } else if (mode.equalsIgnoreCase("totaldef")) {
            if (getAllSkills().getSkill("skill_acrob").getRank() >= 23) {
                caBonus += 8;
            } else if (getAllSkills().getSkill("skill_acrob").getRank() >= 3) {
                caBonus += 6;
            } else {
                caBonus += 4;
            }
        }
        return caBonus;
    }

    public Integer getAbilityMod(String abiId) {
        int abiMod = 0;
        Ability abi = allAbilities.getAbi(abiId);

        if (abi != null && abi.getType().equalsIgnoreCase("base")) {
            int abiScore = getAbilityScore(abiId);

            float modFloat = (float) ((abiScore - 10.) / 2.0);
            if (modFloat >= 0) {
                abiMod = (int) modFloat;
            } else {
                abiMod = -1 * Math.round(Math.abs(modFloat));
            }
        }
        return abiMod;
    }

    public AllSkills getAllSkills() {
        return allSkills;
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

    public boolean featIsActive(String featId) {
        Feat feat = allFeats.getFeat(featId);
        boolean active = feat.isActive();
        if (allStances.getCurrentStance() != null && allStances.getCurrentStance().getFeatId().contains(featId)) {
            active = true;
        }
        return active;
    }


    public AllAttacks getAllAttacks() {
        return this.allAttacks;
    }

    public List<Attack> getAttacksForType(String type) {
        List<Attack> selectedAttack=new ArrayList<>();
        for (Attack atk : allAttacks.getAttacksForType(type)){
            if (atk.getId().equalsIgnoreCase("attack_stun")){
                if (ressources.getRemaningAttack(atk.getId())>0){
                    selectedAttack.add(atk);
                } else {
                    selectedAttack.add(allAttacks.getAttack("attack_normal"));
                }
            } else if (atk.getId().equalsIgnoreCase("attack_palm")){
                if (ressources.getRemaningAttack(atk.getId())>0){
                    selectedAttack.add(atk);
                }
            } else {
                if(!atk.getId().equalsIgnoreCase("attack_normal")){selectedAttack.add(atk);} //les attaque normal ne sont ajoutées que si plus de stun
            }
        }


        return selectedAttack;
    }

    public void setCombatMode(String mode){
        allAttacks.setCombatMode(mode);
        String modeTxt="";
        String summary="";
        if (mode.equalsIgnoreCase("normal")){
            modeTxt="normal";
        }
        if (mode.equalsIgnoreCase("def")){
            modeTxt="défensif";
            summary="\n(+"+getCaBonusCombatMode(mode)+"CA/-4 jets d'attaque)";
        }
        if (mode.equalsIgnoreCase("totaldef")){
            modeTxt="défense totale";
            summary="\n(+"+getCaBonusCombatMode(mode)+"CA/-une action simple par round)";
        }
        toastIt("Mode "+modeTxt+" activé."+summary);
    }

    private void toastIt(String s) {
        Toast toast = Toast.makeText(mC, s, Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public AllKiCapacities getAllKiCapacities() {
        return this.allKiCapacities;
    }

    public Ressources getRessources() {
        return this.ressources;
    }


    public void refresh() {
        allFeats.refreshAllSwitch();
        allSkills.refreshAllVals();
        allAbilities.refreshAllAbilities();
        allAttacks.refreshAllAttacks();
    }



}
