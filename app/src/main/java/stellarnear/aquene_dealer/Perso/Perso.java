package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.R;

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
    private Inventory inventory;
    private AllResources allResources;
    private Tools tools =new Tools();

    public Perso(Context mC) {
        allStances = new AllStances(mC);
        allFeats = new AllFeats(mC);
        allAbilities = new AllAbilities(mC);
        allSkills = new AllSkills(mC);
        allAttacks = new AllAttacks(mC);
        allKiCapacities = new AllKiCapacities(mC);
        inventory = new Inventory(mC);
        allResources = new AllResources(mC,allFeats,allAbilities);
    }

    public void refresh() {
        allFeats.refreshAllSwitch();
        allSkills.refreshAllVals();
        allAbilities.refreshAllAbilities();
        allAttacks.refreshAllAttacks();
        allResources.refreshMaxs();
        allStances.checkPermaStance();
    }

    public AllStances getAllStances() {
        return allStances;
    }

    public void activateStance(String stanceId) {
        Stance selectedStance = allStances.getStance(stanceId);
        if (selectedStance != null) {
            allStances.activateStance(selectedStance);
        } else {
            allStances.desactivateAllStances();
        }
    }

    public AllAbilities getAllAbilities() {
        return allAbilities;
    }

    public Integer getAbilityScore(Context mC,String abiId) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mC);
        int abiScore = 0;
        if (allAbilities.getAbi(abiId) != null) {
            abiScore = allAbilities.getAbi(abiId).getValue();
            if (abiId.equalsIgnoreCase("ability_equipment")) {
                abiScore= inventory.getAllItemsCount();
            }

            if (abiId.equalsIgnoreCase("ability_rm")) {
                int bonusRm = tools.toInt(settings.getString("bonus_temp_rm", String.valueOf(mC.getResources().getInteger(R.integer.bonus_temp_rm_DEF))));
                if (bonusRm>abiScore) { abiScore = bonusRm; }
            }
            if (abiId.equalsIgnoreCase("ability_ms") && allStances.isActive("stance_unicorn")) {
                abiScore += 6;
            }
            if (abiId.equalsIgnoreCase("ability_ms") && settings.getBoolean("switch_temp_rapid",mC.getResources().getBoolean(R.bool.switch_temp_rapid_DEF))) {
                abiScore += 9;
            }

            if (abiId.equalsIgnoreCase("ability_init") && featIsActive("feat_init")) {
                abiScore += 4;
            }

            if (abiId.equalsIgnoreCase("ability_bmo") && allStances.isActive("stance_octopus")) {
                abiScore += getAbilityMod(mC,"ability_sagesse");
            }

            if (abiId.equalsIgnoreCase("ability_ca")) {
                abiScore += tools.toInt(settings.getString("bonus_temp_ca",String.valueOf(mC.getResources().getInteger(R.integer.bonus_temp_ca_DEF))));
                abiScore += tools.toInt(settings.getString("bonus_ki_armor",String.valueOf(mC.getResources().getInteger(R.integer.bonus_ki_armor_DEF))));
                if (allAttacks.getCombatMode().equals("mode_def")) {
                    abiScore += getCaBonusCombatMode("mode_def");
                } else if (allAttacks.getCombatMode().equals("mode_totaldef")) {
                    abiScore += getCaBonusCombatMode("mode_totaldef");
                }
                if(allStances.isActive("stance_bear")) {
                    abiScore += (int) (getAbilityScore(mC,"ability_lvl")/2.0);
                }
                if (settings.getBoolean("switch_temp_rapid",mC.getResources().getBoolean(R.bool.switch_temp_rapid_DEF))) {
                    abiScore += 1;
                }
            }

            if (abiId.equalsIgnoreCase("ability_ref")||abiId.equalsIgnoreCase("ability_vig")||abiId.equalsIgnoreCase("ability_vol")) {
                abiScore += tools.toInt(settings.getString("bonus_temp_save",String.valueOf(mC.getResources().getInteger(R.integer.bonus_temp_save_DEF))));
                abiScore += tools.toInt(settings.getString("epic_save",String.valueOf(mC.getResources().getInteger(R.integer.epic_save_def))));
                if (abiId.equalsIgnoreCase("ability_ref")){abiScore+=getAbilityMod(mC,"ability_dexterite");}
                if (abiId.equalsIgnoreCase("ability_vig")){abiScore+=getAbilityMod(mC,"ability_constitution");}
                if (abiId.equalsIgnoreCase("ability_vol")){abiScore+=getAbilityMod(mC,"ability_sagesse");}
                if (settings.getBoolean("switch_save_race",mC.getResources().getBoolean(R.bool.switch_save_race_DEF))) {
                    abiScore+=1;
                }
                if (settings.getBoolean("switch_perma_resi",mC.getResources().getBoolean(R.bool.switch_save_race_DEF))) {
                    abiScore+=1;
                }
                if (settings.getBoolean("switch_save_coat",mC.getResources().getBoolean(R.bool.switch_save_coat_DEF))) {
                    abiScore+=5;
                }
                if (abiId.equalsIgnoreCase("ability_ref") && settings.getBoolean("switch_save_ref_boot",mC.getResources().getBoolean(R.bool.switch_save_ref_boot_DEF))) {
                    abiScore+=1;
                }
                if (abiId.equalsIgnoreCase("ability_ref") && settings.getBoolean("switch_temp_rapid",mC.getResources().getBoolean(R.bool.switch_temp_rapid_DEF))) {
                    abiScore+=1;
                }
                if (abiId.equalsIgnoreCase("ability_vol") && featIsActive("feat_iron_will")) {
                    abiScore+=2;
                }
                if (abiId.equalsIgnoreCase("ability_vig") && featIsActive("feat_inhuman_stamina") ) {
                    abiScore+=2;
                }
            }
        }
        return abiScore;
    }

    private int getCaBonusCombatMode(String mode) {
        int caBonus = 0;
        if (mode.equalsIgnoreCase("mode_def")) {
            if (getAllSkills().getSkill("skill_acrob").getRank() >= 23) {
                caBonus += 5;
            } else if (getAllSkills().getSkill("skill_acrob").getRank() >= 3) {
                caBonus += 3;
            } else {
                caBonus += 2;
            }
        } else if (mode.equalsIgnoreCase("mode_totaldef")) {
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

    public Integer getAbilityMod(Context mC,String abiId) {
        int abiMod = 0;
        Ability abi = allAbilities.getAbi(abiId);

        if (abi != null && abi.getType().equalsIgnoreCase("base")) {
            int abiScore = getAbilityScore(mC,abiId);

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

    public Integer getSkillBonus(Context mC,String skillId) {
        int bonusTemp = allSkills.getSkill(skillId).getBonus();
        if (skillId.equalsIgnoreCase("skill_acrob")) {
            bonusTemp += getAbilityScore(mC,"ability_lvl");
        }   //on ajoute le niveau de moine au jet d'acrob

        if (skillId.equalsIgnoreCase("skill_stealth") && allStances.isActive("stance_bat")) {
            bonusTemp += getAbilityMod(mC,"ability_sagesse");
        }
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

        for (Stance stance : allStances.getPermasList()){
            if (stance.getFeatId().contains(featId)) {
                active = true;
            }
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
                if (featIsActive("feat_stun") && allResources.getResource(atk.getId().replace("attack","resource")).getCurrent()>0){
                    selectedAttack.add(atk);
                }
            } else if (atk.getId().equalsIgnoreCase("attack_palm")){
                if (allResources.getResource(atk.getId().replace("attack","resource")).getCurrent()>0){
                    selectedAttack.add(atk);
                }
            } else {
                selectedAttack.add(atk);
            }
        }
        return selectedAttack;
    }

    public void setCombatMode(Context mC,String mode){
        allAttacks.setCombatMode(mode);
        String modeTxt="";
        String summary="";
        if (mode.equalsIgnoreCase("mode_normal")){
            modeTxt="normal";
        }
        if (mode.equalsIgnoreCase("mode_def")){
            modeTxt="défensif";
            summary="\n(+"+getCaBonusCombatMode(mode)+"CA/-4 jets d'attaque)";
        }
        if (mode.equalsIgnoreCase("mode_totaldef")){
            modeTxt="défense totale";
            summary="\n(+"+getCaBonusCombatMode(mode)+"CA/-une action simple par round)";
        }
        tools.customToast(mC,"Mode "+modeTxt+" activé."+summary,"center");
    }

    public AllKiCapacities getAllKiCapacities() {
        return this.allKiCapacities;
    }

    public AllResources getAllResources() {
        return this.allResources;
    }

    public Integer getResourceValue(Context mC,String resId){
        Resource res = allResources.getResource(resId);
        Integer value=0;
        if(res!=null){
            value=res.getCurrent();
        }
        if(resId.equalsIgnoreCase("resource_regen") && allStances.isActive("stance_phenix")){
            Skill surv=allSkills.getSkill("skill_survival");
            int survScore= surv.getRank()+surv.getBonus()+getAbilityMod(mC,surv.getAbilityDependence());
            Skill heal=allSkills.getSkill("skill_heal");
            int healScore= heal.getRank()+heal.getBonus()+getAbilityMod(mC,heal.getAbilityDependence());
            value+=getAbilityMod(mC,"ability_sagesse")+ (int) ((survScore+healScore)/10.);
        }
        return value;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<KiCapacity> getAllKiCapacitiesList() {
        List<KiCapacity> allList = getAllKiCapacities().getAllKiCapacitiesList();
        List<KiCapacity> availableList = new ArrayList<>();
        for (KiCapacity kiCap : allList){
            if (kiCap.getFeat().equalsIgnoreCase("") || (featIsActive(kiCap.getFeat()))){
                availableList.add(kiCap);
            }
        }
        return availableList;
    }
}
