package stellarnear.aquene_dealer.Perso;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stellarnear.aquene_dealer.Divers.HallOfFame;
import stellarnear.aquene_dealer.Divers.PostData;
import stellarnear.aquene_dealer.Divers.PostDataElement;
import stellarnear.aquene_dealer.Divers.Stats.Stats;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 26/12/2017.
 */

public class Perso {
    private AllAbilities allAbilities;
    private AllStances allStances;
    private AllFeats allFeats;
    private AllMythicFeats allMythicFeats;
    private AllSkills allSkills;
    private AllAttacks allAttacks;
    private AllCapacities allCapacities;
    private AllKiCapacities allKiCapacities;
    private AllMythicCapacities allMythicCapacities;
    private Inventory inventory;
    private AllResources allResources;
    private Stats stats;
    private HallOfFame hallOfFame;
    private SharedPreferences preferences;
    private Tools tools =Tools.getTools();
    private Context mC;

    public Perso(Context mC) {
        this.mC=mC;
        this.preferences=PreferenceManager.getDefaultSharedPreferences(mC);
        this.allStances = new AllStances(mC);
        this.allFeats = new AllFeats(mC);
        this.allMythicFeats = new AllMythicFeats(mC);
        this.allCapacities = new AllCapacities(mC);
        this.allSkills = new AllSkills(mC);
        this.allAttacks = new AllAttacks(mC);
        this.allKiCapacities = new AllKiCapacities(mC);
        this.allMythicCapacities = new AllMythicCapacities(mC);

        this.inventory = new Inventory(mC);
        this.allAbilities = new AllAbilities(mC,this.inventory);
        computeCapacities(); // on a besoin de skill et abi pour les usages et valeur des capas
        this.allResources = new AllResources(mC,allFeats,allAbilities,allCapacities,allMythicCapacities,inventory);
        this.stats = new Stats(mC);
        this.hallOfFame=new HallOfFame(mC);
    }

    public void refresh() {
        allStances.checkPermaStance();
        allFeats.refreshAllSwitch();
        allMythicFeats.refreshAllSwitch();
        allSkills.refreshAllVals();
        allAttacks.refreshAllAttacks();
        allAbilities.refreshAllAbilities();
        computeCapacities();
        allResources.refresh();
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

    public Integer getAbilityScore(String abiId) {
        int abiScore = 0;
        if (allAbilities.getAbi(abiId) != null) {
            abiScore = allAbilities.getAbi(abiId).getValue();
            if (abiId.equalsIgnoreCase("ability_equipment")) {
                abiScore= inventory.getAllItemsCount();
            }

            if (abiId.equalsIgnoreCase("ability_ca")) {
                abiScore = 10+ ((int) (getAbilityScore("ability_lvl")/4.0))+ getAbilityMod("ability_sagesse");
                int abiMod = getAbilityMod("ability_dexterite");
                if (inventory.getAllEquipments().hasArmorDexLimitation() && inventory.getAllEquipments().getArmorDexLimitation() < abiMod) {
                    abiScore += inventory.getAllEquipments().getArmorDexLimitation();
                } else {
                    abiScore += abiMod;
                }
                abiScore += tools.toInt(preferences.getString("bonus_global_temp_ca",String.valueOf(0)));
                abiScore += tools.toInt(preferences.getString("bonus_temp_ca",String.valueOf(0)));
                if (allAttacks.getCombatMode().equals("mode_def")) {
                    abiScore += getCaBonusCombatMode("mode_def");
                } else if (allAttacks.getCombatMode().equals("mode_totaldef")) {
                    abiScore += getCaBonusCombatMode("mode_totaldef");
                }
                if(allStances.isActive("stance_bear")) {
                    abiScore += (int) (getAbilityScore("ability_lvl")/2.0);
                }
                if (preferences.getBoolean("switch_temp_rapid",mC.getResources().getBoolean(R.bool.switch_temp_rapid_DEF))|| preferences.getBoolean("switch_blinding_speed",mC.getResources().getBoolean(R.bool.switch_blinding_speed_DEF))) {
                    abiScore += 1;
                }
                abiScore+=inventory.getAllEquipments().getArmorBonus();
            } else if (abiId.equalsIgnoreCase("ability_dmd")) {
                abiScore = 10+  getAbilityScore("ability_lvl") + getAbilityMod("ability_force") +  getAbilityMod("ability_dexterite") + ((int) ( getAbilityScore("ability_lvl")/4.0))+ getAbilityMod("ability_sagesse") ;
            }

            if (abiId.equalsIgnoreCase("ability_rm")) {
                abiScore = 10+ getAbilityScore("ability_lvl");
                int bonusRm = tools.toInt(preferences.getString("bonus_temp_rm", String.valueOf(mC.getResources().getInteger(R.integer.bonus_temp_rm_DEF))));
                if (bonusRm>abiScore) { abiScore = bonusRm; }
            }

            if (abiId.equalsIgnoreCase("ability_reduc")) {
                int bonusReduc = tools.toInt(preferences.getString("mythiccapacity_absorption_value", String.valueOf(mC.getResources().getInteger(R.integer.mythiccapacity_absorption_value_DEF))));
                abiScore += (int)(bonusReduc/10);
                if(allCapacities.capacityIsActive("capacity_perfection")){
                    abiScore+=10;
                }
                if(allCapacities.capacityIsActive("capacity_insight_rd")){
                    abiScore+=2;
                }
            }
            if (abiId.equalsIgnoreCase("ability_reduc_elem") ) {
                int bonusReduc = tools.toInt(preferences.getString("mythiccapacity_absorption_value", String.valueOf(mC.getResources().getInteger(R.integer.mythiccapacity_absorption_value_DEF))));
                abiScore += 5*((int)(bonusReduc/10));
            }


            if (abiId.equalsIgnoreCase("ability_ms") && allStances.isActive("stance_unicorn")) {
                abiScore += 6;
            }
            if (abiId.equalsIgnoreCase("ability_ms") && (preferences.getBoolean("switch_temp_rapid",mC.getResources().getBoolean(R.bool.switch_temp_rapid_DEF))|| preferences.getBoolean("switch_blinding_speed",mC.getResources().getBoolean(R.bool.switch_blinding_speed_DEF)))) {
                abiScore += 9;
            }

            if (abiId.equalsIgnoreCase("ability_init")) {
                abiScore=getAbilityMod("ability_dexterite");
                if(getAllMythicCapacities().getMythiccapacity("mythiccapacity_init").isActive()) {
                    int currentTier = tools.toInt(preferences.getString("mythic_tier", String.valueOf(mC.getResources().getInteger(R.integer.mythic_tier_def))));
                    abiScore += currentTier;
                }
                if ( featIsActive("feat_init")) {
                    abiScore += 4;
                }
            }

            if (abiId.equalsIgnoreCase("ability_bmo") ) {
                abiScore = getAbilityScore("ability_lvl") + getAbilityMod("ability_force");
                if(inventory.getAllEquipments().testIfNameItemIsEquipped("Ceinture de Charge Tonitruante")){
                    abiScore+=2;
                }
                if(allCapacities.capacityIsActive("capacity_insight_bmo")){
                    abiScore+=4;
                }
                if(allCapacities.capacityIsActive("capacity_insight_bmo2")){
                    abiScore+=4;
                }
                if(allStances.isActive("stance_octopus")){
                    abiScore += getAbilityMod( "ability_sagesse");
                }
            }

            if (abiId.equalsIgnoreCase("ability_ref")||abiId.equalsIgnoreCase("ability_vig")||abiId.equalsIgnoreCase("ability_vol")) {
                abiScore += tools.toInt(preferences.getString("bonus_temp_save",String.valueOf(mC.getResources().getInteger(R.integer.bonus_temp_save_DEF))));
                abiScore += tools.toInt(preferences.getString("epic_save",String.valueOf(mC.getResources().getInteger(R.integer.epic_save_def))));
                if (abiId.equalsIgnoreCase("ability_ref")){abiScore+=getAbilityMod("ability_dexterite");}
                if (abiId.equalsIgnoreCase("ability_vig")){abiScore+=getAbilityMod("ability_constitution");}
                if (abiId.equalsIgnoreCase("ability_vol")){abiScore+=getAbilityMod("ability_sagesse");}
                if (preferences.getBoolean("switch_save_race",mC.getResources().getBoolean(R.bool.switch_save_race_DEF))) {
                    abiScore+=1;
                }
                if (preferences.getBoolean("switch_perma_resi",mC.getResources().getBoolean(R.bool.switch_perma_resi_DEF))) {
                    abiScore+=1;
                }
                if (abiId.equalsIgnoreCase("ability_ref") && (preferences.getBoolean("switch_temp_rapid",mC.getResources().getBoolean(R.bool.switch_temp_rapid_DEF))|| preferences.getBoolean("switch_blinding_speed",mC.getResources().getBoolean(R.bool.switch_blinding_speed_DEF)))) {
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

    // calculs
    public int getNMainDice() {
        String[] baseFirstDmg = mC.getResources().getStringArray(R.array.base_dmg_fist);
        int index = tools.toInt( preferences.getString("base_dmg_fist", mC.getResources().getString(R.string.base_dmg_fist_DEF)));
        if(inventory.getAllEquipments().testIfNameItemIsEquipped("Ceinture de Charge Tonitruante")){
            index++;
        }
        if(inventory.getAllEquipments().testIfNameItemIsEquipped("Anneau d'impact")){
            index++;
        }
        String currentBaseDices = baseFirstDmg[index];
        int baseN = tools.toInt(currentBaseDices.substring(0,currentBaseDices.indexOf("d")));
        return baseN;
    }

    public int getMainDiceType() {
        String[] baseFirstDmg = mC.getResources().getStringArray(R.array.base_dmg_fist);
        int index = tools.toInt( preferences.getString("base_dmg_fist", mC.getResources().getString(R.string.base_dmg_fist_DEF)));
        if(inventory.getAllEquipments().testIfNameItemIsEquipped("Ceinture de Charge Tonitruante")){
            index++;
        }
        if(inventory.getAllEquipments().testIfNameItemIsEquipped("Anneau d'impact")){
            index++;
        }
        String currentBaseDices = baseFirstDmg[index];
        int baseType = tools.toInt(currentBaseDices.substring(currentBaseDices.indexOf("d")+1));
        return baseType;
    }

    private void computeCapacities() {
        for(Capacity cap : allCapacities.getAllCapacitiesList()){
            if(!cap.isInfinite()){
                calculDailyUsage(cap);
            }
            calculValue(cap);
        }
    }

    private void calculDailyUsage(Capacity cap) {
        if(!cap.getDailyUseString().equalsIgnoreCase("")){
            int dailyUse=0;
            if(tools.toInt(cap.getDailyUseString())==0){
                int mainPJlvl=getAbilityScore("ability_lvl");
                switch (cap.getDailyUseString()){
                    case "lvl":
                        dailyUse =mainPJlvl;
                        break;
                    case "leg_points":
                        cap.setDailyUse(null);
                        cap.setJoinedResource(1,"resource_legendary_points");
                        break;
                    case "4ki_points":
                        cap.setDailyUse(null);
                        cap.setJoinedResource(4,"resource_ki");
                        break;
                }
            } else {
                dailyUse=tools.toInt(cap.getDailyUseString());
            }
            cap.setDailyUse(dailyUse);
        }
    }

    private void calculValue(Capacity cap) {
        if(!cap.getValueString().equalsIgnoreCase("")) {
            int value=0;
            if (tools.toInt(cap.getValueString()) == 0) {
                int mainPJlvl = getAbilityScore("ability_lvl");
                switch (cap.getValueString()) {
                    case "lvl":
                        value = mainPJlvl;
                        break;
                }
            } else {
                value = tools.toInt(cap.getValueString());
            }
            cap.setValue(value);
        }
    }

    public AllSkills getAllSkills() {
        return allSkills;
    }

    public Integer getSkillBonus(String skillId) {
        int bonusTemp = allSkills.getSkill(skillId).getBonus();
        bonusTemp+= inventory.getAllEquipments().getSkillBonus(skillId);
        if (skillId.equalsIgnoreCase("skill_acrob")) {
            bonusTemp += getAbilityScore("ability_lvl");//on ajoute le niveau de moine au jet d'acrob
        }

        if (skillId.equalsIgnoreCase("skill_stealth") && allStances.isActive("stance_bat")) {
            bonusTemp += getAbilityMod("ability_sagesse");
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

    public AllMythicFeats getAllMythicFeats() {
        return allMythicFeats;
    }

    public boolean mythicFeatIsActive(String mythicFeatId) {
        MythicFeat feat = allMythicFeats.getMythicFeat(mythicFeatId);
        return feat.isActive();
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
            } else if (atk.getId().equalsIgnoreCase("attack_charge")){
                if (allResources.getResource("resource_mythic_points").getCurrent()>0){
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

    public AllCapacities getAllCapacities() {
        return allCapacities;
    }

    public AllMythicCapacities getAllMythicCapacities(){
        return allMythicCapacities;
    }

    public AllResources getAllResources() {
        return this.allResources;
    }

    public Integer getCurrentResourceValue(String resId){
        Resource res = allResources.getResource(resId);
        Integer value=0;
        if(res!=null){
            value=res.getCurrent();
        }
        if(resId.equalsIgnoreCase("resource_regen") && allStances.isActive("stance_phenix")){
            Skill surv=allSkills.getSkill("skill_survival");
            int survScore= surv.getRank()+surv.getBonus()+getAbilityMod(surv.getAbilityDependence());
            Skill heal=allSkills.getSkill("skill_heal");
            int healScore= heal.getRank()+heal.getBonus()+getAbilityMod(heal.getAbilityDependence());
            value+=getAbilityMod("ability_sagesse")+ (int) ((survScore+healScore)/10.);
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

    public void resetTemp() {
        List<String> allTempList = Arrays.asList("bonus_temp_jet_att", "bonus_temp_jet_dmg", "bonus_temp_ca", "bonus_temp_save", "bonus_temp_rm", "bonus_ki_armor","mythiccapacity_absorption_value");
        for (String temp : allTempList) {
            preferences.edit().putString(temp, "0").apply();
        }
        preferences.edit().putBoolean("switch_temp_rapid", false).apply();
        preferences.edit().putBoolean("switch_blinding_speed", false).apply();
    }

    public void endRound() {
        if( preferences.getBoolean("switch_blinding_speed",mC.getResources().getBoolean(R.bool.switch_blinding_speed_DEF))) {
            new PostData(mC,new PostDataElement("Dépense d'un round de Vitesse aveuglante","-"));
            preferences.edit().putBoolean("switch_blinding_speed", false).apply();
            tools.customToast(mC,"Vitesse aveuglante désactivée","center");
        }
    }

    public Stats getStats() {
        return stats;
    }

    public HallOfFame getHallOfFame() {
        return hallOfFame;
    }

    public void reset() {
        this.allFeats.reset();
        this.allCapacities.reset();
        this.allMythicFeats.reset();
        this.allMythicCapacities.reset();
        this.allAbilities.reset();
        this.allResources.reset();
        this.allSkills.reset();
        resetTemp();
        refresh();
        sleep();
    }

    public void sleep() {
        allResources.resetCurrent();
        resetTemp();
        if(allMythicCapacities!=null && allMythicCapacities.mythicCapacityIsActive("mythiccapacity_recover")){
            allResources.getResource("resource_hp").fullHeal();
        }
        refresh();
    }

    public void hardReset() {
        this.stats.reset();
        this.hallOfFame.reset();
        this.inventory.reset();
        reset();
    }

    public void loadFromSave() {
        inventory.loadFromSave();
        stats.loadFromSave();
        hallOfFame.loadFromSave();
        refresh();
    }
}
