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
    private AllKiCapacities allKiCapacities;
    private AllMythicCapacities allMythicCapacities;
    private Inventory inventory;
    private AllResources allResources;
    private Stats stats;
    private HallOfFame hallOfFame;
    private SharedPreferences preferences;
    private Tools tools =new Tools();
    private Context mC;

    public Perso(Context mC) {
        this.allStances = new AllStances(mC);
        this.allFeats = new AllFeats(mC);
        this.allMythicFeats = new AllMythicFeats(mC);
        this.allAbilities = new AllAbilities(mC);
        this.allSkills = new AllSkills(mC);
        this.allAttacks = new AllAttacks(mC);
        this.allKiCapacities = new AllKiCapacities(mC);
        this.allMythicCapacities = new AllMythicCapacities(mC);
        this.inventory = new Inventory(mC);
        this.allResources = new AllResources(mC,allFeats,allAbilities);
        this.stats = new Stats(mC);
        this.hallOfFame=new HallOfFame(mC);
        this.preferences=PreferenceManager.getDefaultSharedPreferences(mC);
        this.mC=mC;
    }

    public void refresh() {
        allFeats.refreshAllSwitch();
        allMythicFeats.refreshAllSwitch();
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

            if (abiId.equalsIgnoreCase("ability_reduc")) {
                int bonusReduc = tools.toInt(settings.getString("mythiccapacity_absorption_value", String.valueOf(mC.getResources().getInteger(R.integer.mythiccapacity_absorption_value_DEF))));
                abiScore += (int)(bonusReduc/10);
            }
            if (abiId.equalsIgnoreCase("ability_reduc_elem") ) {
                int bonusReduc = tools.toInt(settings.getString("mythiccapacity_absorption_value", String.valueOf(mC.getResources().getInteger(R.integer.mythiccapacity_absorption_value_DEF))));
                abiScore += 5*((int)(bonusReduc/10));
            }


            if (abiId.equalsIgnoreCase("ability_ms") && allStances.isActive("stance_unicorn")) {
                abiScore += 6;
            }
            if (abiId.equalsIgnoreCase("ability_ms") && (settings.getBoolean("switch_temp_rapid",mC.getResources().getBoolean(R.bool.switch_temp_rapid_DEF))|| settings.getBoolean("switch_blinding_speed",mC.getResources().getBoolean(R.bool.switch_blinding_speed_DEF)))) {
                abiScore += 9;
            }

            if (abiId.equalsIgnoreCase("ability_init")) {
                int currentTier = tools.toInt(settings.getString("mythic_tier", String.valueOf(mC.getResources().getInteger(R.integer.mythic_tier_def))));
                abiScore += currentTier;
                if ( featIsActive("feat_init")) {
                    abiScore += 4;
                }
            }

            if (abiId.equalsIgnoreCase("ability_bmo") ) {
                abiScore+=4;  //epic insight niveau 25
                if(allStances.isActive("stance_octopus")){
                    abiScore += getAbilityMod(mC, "ability_sagesse");
                }
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
                if (settings.getBoolean("switch_temp_rapid",mC.getResources().getBoolean(R.bool.switch_temp_rapid_DEF))|| settings.getBoolean("switch_blinding_speed",mC.getResources().getBoolean(R.bool.switch_blinding_speed_DEF))) {
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
                if (settings.getBoolean("switch_perma_resi",mC.getResources().getBoolean(R.bool.switch_perma_resi_DEF))) {
                    abiScore+=1;
                }
                if (settings.getBoolean("switch_save_coat",mC.getResources().getBoolean(R.bool.switch_save_coat_DEF))) {
                    abiScore+=5;
                }
                if (abiId.equalsIgnoreCase("ability_ref") && settings.getBoolean("switch_save_ref_boot",mC.getResources().getBoolean(R.bool.switch_save_ref_boot_DEF))) {
                    abiScore+=1;
                }
                if (abiId.equalsIgnoreCase("ability_ref") && (settings.getBoolean("switch_temp_rapid",mC.getResources().getBoolean(R.bool.switch_temp_rapid_DEF))|| settings.getBoolean("switch_blinding_speed",mC.getResources().getBoolean(R.bool.switch_blinding_speed_DEF)))) {
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
            bonusTemp += getAbilityScore(mC,"ability_lvl");//on ajoute le niveau de moine au jet d'acrob

            Equipment head = this.inventory.getAllEquipments().getEquipmentsEquiped("helm_slot");
            if(head!= null && head.getName().equalsIgnoreCase("oreilles de lapin")){
                bonusTemp+=30;
            }
        }

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

    public AllMythicCapacities getAllMythicCapacities(){
        return allMythicCapacities;
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
            getAllResources().getResource("resource_blinding_speed").spend(1);
            new PostData(mC,new PostDataElement("Dépense d'un round de Vitesse aveuglante","-"));
            preferences.edit().putBoolean("switch_blinding_speed", false).apply();
            preferences.edit().putString("blinding_speed_current_temp", String.valueOf(getResourceValue(mC,"resource_blinding_speed"))).apply();
            tools.customToast(mC,"Vitesse aveuglante désactivée","center");
        }
    }

    public Stats getStats() {
        return stats;
    }

    public HallOfFame getHallOfFame() {
        return hallOfFame;
    }
}
