package stellarnear.aquene_dealer.Divers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Divers.SettingsFragments.PrefAllInventoryFragment;
import stellarnear.aquene_dealer.Divers.SettingsFragments.PrefFeatFragment;
import stellarnear.aquene_dealer.Divers.SettingsFragments.PrefInfoScreenFragment;
import stellarnear.aquene_dealer.Divers.SettingsFragments.PrefMythicFragment;
import stellarnear.aquene_dealer.Divers.SettingsFragments.PrefResetScreenFragment;
import stellarnear.aquene_dealer.Divers.SettingsFragments.PrefSkillFragment;
import stellarnear.aquene_dealer.Divers.SettingsFragments.PrefSleepScreenFragment;
import stellarnear.aquene_dealer.Divers.SettingsFragments.PrefXpFragment;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class SettingsFragment extends PreferenceFragment {
    private Activity mA;
    private Context mC;
    private List<String> histoPrefKeys = new ArrayList<>();
    private List<String> histoTitle = new ArrayList<>();

    private String currentPageKey;
    private String currentPageTitle;

    private Tools tools = new Tools();
    private SharedPreferences settings;
    private PrefAllInventoryFragment prefAllInventoryFragment;
    private PrefXpFragment prefXpFragment;
    private PrefFeatFragment prefFeatFragment;
    private PrefMythicFragment prefMythicFragment;
    private PrefSkillFragment prefSkillFragment;
    private PrefResetScreenFragment prefResetScreenFragment;
    private PrefSleepScreenFragment prefSleepScreenFragment;
    private PrefInfoScreenFragment prefInfoScreenFragment;

    private Perso aquene = MainActivity.aquene;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        this.mA=getActivity();
        this.mC=getContext();
        addPreferencesFromResource(R.xml.pref);
        this.histoPrefKeys.add("pref");
        this.histoTitle.add(getResources().getString(R.string.setting_activity));
        this.prefAllInventoryFragment =new PrefAllInventoryFragment(mA,mC);
        this.prefAllInventoryFragment.setRefreshEventListener(new PrefAllInventoryFragment.OnRefreshEventListener() {
            @Override
            public void onEvent() {
                navigate();
            }
        });
        this.prefMythicFragment=new PrefMythicFragment(mA,mC);
        this.prefXpFragment = new PrefXpFragment(mA,mC);
        this.prefXpFragment.checkLevel(tools.toBigInt(settings.getString("current_xp", String.valueOf(getContext().getResources().getInteger(R.integer.current_xp_def)))));
        this.prefFeatFragment=new PrefFeatFragment(mA,mC);
        this.prefSkillFragment=new PrefSkillFragment(mA,mC);
        this.prefResetScreenFragment = new PrefResetScreenFragment(mA,mC);
        this.prefSleepScreenFragment = new PrefSleepScreenFragment(mA,mC);
        this.prefInfoScreenFragment=new PrefInfoScreenFragment(mA,mC);
    }

    // will be called by SettingsActivity (Host Activity)
    public void onUpButton() {
        if (histoPrefKeys.get(histoPrefKeys.size() - 1).equalsIgnoreCase("pref") || histoPrefKeys.size() <= 1) // in top-level
        {
            Intent intent = new Intent(mA, MainActivity.class);// Switch to MainActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            mA.startActivity(intent);
        } else // in sub-level
        {
            currentPageKey=histoPrefKeys.get(histoPrefKeys.size() - 2);
            currentPageTitle=histoTitle.get(histoTitle.size() - 2);
            navigate();
            histoPrefKeys.remove(histoPrefKeys.size() - 1);
            histoTitle.remove(histoTitle.size() - 1);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().contains("pref_")) {
            histoPrefKeys.add(preference.getKey());
            histoTitle.add(preference.getTitle().toString());
        }

        if (preference.getKey().startsWith("pref")) {
            this.currentPageKey =preference.getKey();
            this.currentPageTitle =preference.getTitle().toString();
            navigate();
        } else {
            action(preference);
        }
        /*
        // Top level PreferenceScreen
        if (key.equals("top_key_0")) {         changePrefScreen(R.xml.pref_general, preference.getTitle().toString()); // descend into second level    }

        // Second level PreferenceScreens
        if (key.equals("second_level_key_0")) {        // do something...    }       */
        return true;
    }


    private void navigate() {
        if(currentPageKey.equalsIgnoreCase("pref")){
            getPreferenceScreen().removeAll();
            addPreferencesFromResource(R.xml.pref);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(currentPageTitle);
        } else if (currentPageKey.contains("pref_")) {
            loadPage();
            switch (currentPageKey) {
                case "pref_inventory_equipment":
                    PreferenceCategory otherList = (PreferenceCategory) findPreference("other_slot_equipment_list_category");
                    PreferenceCategory spareList = (PreferenceCategory) findPreference("spare_equipment_list_category");
                    prefAllInventoryFragment.addEditableEquipment(otherList,spareList);
                    break;
                case "pref_inventory_bag":
                    PreferenceCategory bagList = (PreferenceCategory) findPreference("bag_list_category");
                    prefAllInventoryFragment.addBagList(bagList);
                    break;
                case "pref_character_xp":
                    BigInteger xp = tools.toBigInt(settings.getString("current_xp", String.valueOf(getContext().getResources().getInteger(R.integer.current_xp_def))));
                    prefXpFragment.checkLevel(xp);
                    break;
                case "pref_character_feat":
                    PreferenceCategory active = (PreferenceCategory) findPreference(getString(R.string.feat_active));
                    PreferenceCategory def = (PreferenceCategory) findPreference(getString(R.string.feat_def));
                    PreferenceCategory atk = (PreferenceCategory) findPreference(getString(R.string.feat_atk));
                    PreferenceCategory other = (PreferenceCategory) findPreference(getString(R.string.feat_other));
                    PreferenceCategory stance = (PreferenceCategory) findPreference(getString(R.string.feat_stance));

                    prefFeatFragment.addFeatsList(active,def,atk,other,stance);
                    setHasOptionsMenu(true);
                    break;
                case "pref_mythic_feat":
                    PreferenceCategory active_myth = (PreferenceCategory) findPreference(getString(R.string.feat_active));
                    PreferenceCategory def_myth = (PreferenceCategory) findPreference(getString(R.string.feat_def));
                    PreferenceCategory atk_myth = (PreferenceCategory) findPreference(getString(R.string.feat_atk));
                    PreferenceCategory other_myth = (PreferenceCategory) findPreference(getString(R.string.feat_other));
                    PreferenceCategory stance_myth = (PreferenceCategory) findPreference(getString(R.string.feat_stance));

                    prefMythicFragment.addMythicFeatsList(active_myth,def_myth,atk_myth,other_myth,stance_myth);
                    setHasOptionsMenu(true);
                    break;
                case "pref_character_skill":
                    PreferenceCategory rank = (PreferenceCategory) findPreference(getString(R.string.skill_mastery));
                    PreferenceCategory bonus = (PreferenceCategory) findPreference(getString(R.string.skill_bonus));
                    prefSkillFragment.addSkillsList(rank,bonus);
                    setHasOptionsMenu(true);
                    break;
                case "pref_mythic_tier":
                    prefMythicFragment.refreshMythicTierBar();
                    break;
            }
        }
    }

    private void loadPage() {
        getPreferenceScreen().removeAll();
        int xmlID = getResources().getIdentifier(currentPageKey, "xml", getContext().getPackageName());
        addPreferencesFromResource(xmlID);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(currentPageTitle);
    }

    private void action(Preference preference) {
        switch (preference.getKey()) {
            case "reset_para":
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(preference.getTitle());
                ((ContentFrameLayout) getActivity().findViewById(android.R.id.content)).removeAllViews();
                prefResetScreenFragment.addResetScreen();
                break;
            case "show_equipment":
                aquene.getInventory().showEquipment(getActivity(), true);
                break;
            case "add_gold":
                preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                        int gold = tools.toInt(settings.getString("money_gold", String.valueOf(getContext().getResources().getInteger(R.integer.money_gold_def))));
                        settings.edit().putString("money_gold", String.valueOf(gold + tools.toInt(o.toString()))).apply();
                        settings.edit().putString("add_gold", String.valueOf(0)).apply();
                        getPreferenceScreen().removeAll();
                        addPreferencesFromResource(R.xml.pref_inventory_money); //pour refresh le current
                        return true;
                    }
                });
                break;
            case "add_current_xp":
                preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                        BigInteger xp = tools.toBigInt(settings.getString("current_xp", String.valueOf(getContext().getResources().getInteger(R.integer.current_xp_def))));
                        BigInteger addXp = tools.toBigInt(o.toString());
                        settings.edit().putString("current_xp", xp.add(addXp).toString()).apply();
                        settings.edit().putString("add_current_xp", String.valueOf(0)).apply();
                        prefXpFragment.checkLevel(xp, addXp);
                        navigate();
                        return true;
                    }
                });
                break;
            case "current_xp":
                preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        settings.edit().putString(preference.getKey(), o.toString()).apply();
                        prefXpFragment.checkLevel(tools.toBigInt(o.toString()));
                        navigate();
                        return true;
                    }
                });
                break;
            case "create_bag_item":
                prefAllInventoryFragment.createBagItem();
                break;
            case "create_equipment":
                prefAllInventoryFragment.createEquipment();
                break;
            case "reset_temp":
                resetTemp();
                navigate();
                break;
            case "sleep":
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(preference.getTitle().toString());
                ((ContentFrameLayout) getActivity().findViewById(android.R.id.content)).removeAllViews();
                prefSleepScreenFragment.addSleepScreen();
                break;
            case "infos":
                prefInfoScreenFragment.showInfo();
                break;
            case "spend_myth_point":
                if( aquene.getResourceValue(mC,"mythic_points")>0) {
                    new AlertDialog.Builder(mC)
                            .setTitle("Demande de confirmation")
                            .setMessage("Confirmes-tu la dépense d'un point mythique ?")
                            .setIcon(android.R.drawable.ic_menu_help)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    aquene.getAllResources().getResource("mythic_points").spend(1);
                                    tools.customToast(mC,"Il te reste "+aquene.getResourceValue(mC,"mythic_points")+" point(s) mythique(s)","center");
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    tools.customToast(mC,"Tu n'as plus de point mythique","center");
                }
                break;
            case "spend_leg_point":
                if( aquene.getResourceValue(mC,"legendary_points")>0) {
                    new AlertDialog.Builder(mC)
                            .setTitle("Demande de confirmation")
                            .setMessage("Confirmes-tu la dépense d'un point légendaire ?")
                            .setIcon(android.R.drawable.ic_menu_help)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    aquene.getAllResources().getResource("legendary_points").spend(1);
                                    tools.customToast(mC,"Il te reste "+aquene.getResourceValue(mC,"legendary_points")+" point(s) légendaire(s)","center");
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    tools.customToast(mC,"Tu n'as plus de point légendaire","center");
                }
                break;
        }
    }


    private void resetTemp() {
        List<String> allTempList = Arrays.asList("bonus_temp_jet_att", "bonus_temp_jet_dmg", "bonus_temp_ca", "bonus_temp_save", "bonus_temp_rm", "bonus_ki_armor","mythiccapacity_absorption");
        for (String temp : allTempList) {
            settings.edit().putString(temp, "0").apply();
        }
        settings.edit().putBoolean("switch_temp_rapid", false).apply();
        settings.edit().putBoolean("switch_blinding_speed", false).apply();
    }
}