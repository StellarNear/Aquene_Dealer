package stellarnear.aquene_companion.Divers.SettingsFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import stellarnear.aquene_companion.Activities.MainActivity;
import stellarnear.aquene_companion.Activities.SaveSharedPreferencesActivity;
import stellarnear.aquene_companion.Divers.PostData;
import stellarnear.aquene_companion.Divers.PostDataElement;
import stellarnear.aquene_companion.Divers.Tools;
import stellarnear.aquene_companion.Perso.Perso;
import stellarnear.aquene_companion.R;

public class SettingsFragment extends CustomPreferenceFragment {
    private Activity mA;
    private Context mC;
    private List<String> histoPrefKeys = new ArrayList<>();
    private List<String> histoTitle = new ArrayList<>();

    private String currentPageKey;
    private String currentPageTitle;

    private Tools tools = Tools.getTools();
    private PrefAllInventoryFragment prefAllInventoryFragment;
    private PrefXpFragment prefXpFragment;
    private PrefFeatFragment prefFeatFragment;
    private PrefCapaFragment prefCapaFragment;
    private PrefMythicFeatFragment prefMythicFeatFragment;
    private PrefMythicCapaFragment prefMythicCapaFragment;
    private PrefSkillFragment prefSkillFragment;
    private PrefInfoScreenFragment prefInfoScreenFragment;

    private Perso aquene = MainActivity.aquene;
    private SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    if (key.contains("switch_capacity_")){aquene.getAllResources().refreshCapaListResources();}
                }
            };

    @Override
    protected void onCreateFragment() {
        settings.registerOnSharedPreferenceChangeListener(listener);
        this.mA=getActivity();
        this.mC=getContext();
        addPreferencesFromResource(R.xml.pref);
        findPreference("pref_stats").setSummary("Record actuel : "+aquene.getAllAttacks().getAttack("attack_flurry").getHighscore());
        this.histoPrefKeys.add("pref");
        this.histoTitle.add(getResources().getString(R.string.setting_activity));
        this.prefAllInventoryFragment =new PrefAllInventoryFragment(mA,mC);
        this.prefAllInventoryFragment.setRefreshEventListener(new PrefAllInventoryFragment.OnRefreshEventListener() {
            @Override
            public void onEvent() {
                navigate();
            }
        });
        this.prefXpFragment = new PrefXpFragment(mA,mC);
        this.prefXpFragment.checkLevel(tools.toBigInt(settings.getString("current_xp", String.valueOf(getContext().getResources().getInteger(R.integer.current_xp_def)))));
        this.prefFeatFragment=new PrefFeatFragment(mA,mC);
        this.prefCapaFragment=new PrefCapaFragment(mA,mC);
        this.prefMythicFeatFragment =new PrefMythicFeatFragment(mA,mC);
        this.prefMythicCapaFragment =new PrefMythicCapaFragment(mA,mC);
        this.prefSkillFragment=new PrefSkillFragment(mA,mC);
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
    protected void onPreferenceTreeClickFragment(PreferenceScreen preferenceScreen, Preference preference) throws Exception {
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
    }

    @Override
    protected void onDestroyFragment() {
        settings.unregisterOnSharedPreferenceChangeListener(listener);
    }


    private void navigate() {
        if(currentPageKey.equalsIgnoreCase("pref")){
            getPreferenceScreen().removeAll();
            addPreferencesFromResource(R.xml.pref);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(currentPageTitle);
            findPreference("pref_stats").setSummary("Record actuel : "+aquene.getAllAttacks().getAttack("attack_flurry").getHighscore());
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
                case "pref_character_capa":
                    prefCapaFragment.addCapaList(getPreferenceScreen());
                    setHasOptionsMenu(true);
                    break;
                case "pref_mythic_feat":
                    PreferenceCategory active_myth = (PreferenceCategory) findPreference(getString(R.string.feat_active));
                    PreferenceCategory def_myth = (PreferenceCategory) findPreference(getString(R.string.feat_def));
                    PreferenceCategory atk_myth = (PreferenceCategory) findPreference(getString(R.string.feat_atk));
                    PreferenceCategory other_myth = (PreferenceCategory) findPreference(getString(R.string.feat_other));
                    PreferenceCategory stance_myth = (PreferenceCategory) findPreference(getString(R.string.feat_stance));

                    prefMythicFeatFragment.addMythicFeatsList(active_myth,def_myth,atk_myth,other_myth,stance_myth);
                    setHasOptionsMenu(true);
                    break;
                case "pref_mythic_capa":
                    prefMythicCapaFragment.addMythicCapaList(getPreferenceScreen());
                    setHasOptionsMenu(true);
                    break;
                case "pref_character_skill":
                    PreferenceCategory rank = (PreferenceCategory) findPreference(getString(R.string.skill_mastery));
                    PreferenceCategory bonus = (PreferenceCategory) findPreference(getString(R.string.skill_bonus));
                    prefSkillFragment.addSkillsList(rank,bonus);
                    setHasOptionsMenu(true);
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

    private void action(Preference preference) throws Exception {
        switch (preference.getKey()) {
            case "show_equipment":
                aquene.getInventory().showEquipment(getActivity(), getContext(),true);
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
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(mC);
                final EditText edittext = new EditText(mA);
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

                alert.setMessage("Nombre de points d'experiences à ajouter");
                alert.setTitle("Ajout d'experience");
                alert.setView(edittext);
                alert.setIcon(R.drawable.ic_upgrade);
                alert.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                        BigInteger xp = tools.toBigInt(settings.getString("current_xp", String.valueOf(getContext().getResources().getInteger(R.integer.current_xp_def))));
                        BigInteger addXp = tools.toBigInt(edittext.getText().toString());
                        settings.edit().putString("current_xp", xp.add(addXp).toString()).apply();
                        prefXpFragment.checkLevel(xp, addXp);
                        navigate();
                    }
                });

                alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // rien
                    }
                });
                alert.show();
                edittext.post(new Runnable() {
                    public void run() {
                        edittext.setFocusableInTouchMode(true);
                        edittext.requestFocusFromTouch();
                        InputMethodManager lManager = (InputMethodManager) mA.getSystemService(Context.INPUT_METHOD_SERVICE);
                        lManager.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);
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
                aquene.resetTemp();
                navigate();
                break;
            case "infos":
                prefInfoScreenFragment.showInfo();
                break;
            case "spend_myth_point":
                if( aquene.getCurrentResourceValue("resource_mythic_points")>0) {
                    new AlertDialog.Builder(mC)
                            .setTitle("Demande de confirmation")
                            .setMessage("Confirmes-tu la dépense d'un point mythique ?")
                            .setIcon(android.R.drawable.ic_menu_help)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    aquene.getAllResources().getResource("resource_mythic_points").spend(1);
                                    new PostData(mC,new PostDataElement("Utilisation d'un pouvoir mythique","Dépense d' un point mythique"));
                                    tools.customToast(mC,"Il te reste "+aquene.getCurrentResourceValue("resource_mythic_points")+" point(s) mythique(s)","center");
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    tools.customToast(mC,"Tu n'as plus de point mythique","center");
                }
                break;
            case "spend_leg_point":
                if( aquene.getCurrentResourceValue("resource_legendary_points")>0) {
                    new AlertDialog.Builder(mC)
                            .setTitle("Demande de confirmation")
                            .setMessage("Confirmes-tu la dépense d'un point légendaire ?")
                            .setIcon(android.R.drawable.ic_menu_help)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    aquene.getAllResources().getResource("resource_legendary_points").spend(1);
                                    new PostData(mC,new PostDataElement("Surcharge légendaire du d20","-1pt légendaire"));
                                    tools.customToast(mC,"Il te reste "+aquene.getCurrentResourceValue("resource_legendary_points")+" point(s) légendaire(s)","center");
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    tools.customToast(mC,"Tu n'as plus de point légendaire","center");
                }
                break;
            case "appli_refresh":
                new AlertDialog.Builder(mC)
                        .setTitle("Demande de confirmation")
                        .setMessage("Confirmes-tu l'action de réinitialiser les attributs du personnage ?")
                        .setIcon(android.R.drawable.ic_menu_help)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                aquene.reset();
                                tools.customToast(mC,"Rafraîchissement éffectué","center");
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                break;
            case "appli_reset_stuff":
                new AlertDialog.Builder(mC)
                        .setTitle("Demande de confirmation")
                        .setMessage("Confirmes-tu l'action de réinitialiser l'équipement du personnage ?")
                        .setIcon(android.R.drawable.ic_menu_help)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                aquene.getInventory().getAllEquipments().reset();
                                aquene.getAllResources().reset();
                                tools.customToast(mC,"Rafraîchissement éffectué","center");
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                break;
            case "appli_reset_bag":
                new AlertDialog.Builder(mC)
                        .setTitle("Demande de confirmation")
                        .setMessage("Confirmes-tu l'action de réinitialiser le sac du personnage ?")
                        .setIcon(android.R.drawable.ic_menu_help)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                aquene.getInventory().getBag().reset();
                                tools.customToast(mC,"Rafraîchissement éffectué","center");
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                break;
            case "appli_reset_stats":
                new AlertDialog.Builder(mC)
                        .setTitle("Demande de confirmation")
                        .setMessage("Confirmes-tu l'action de réinitialiser les statistiques du personnage ?")
                        .setIcon(android.R.drawable.ic_menu_help)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                aquene.getStats().reset();
                                settings.edit().putString("highscore","0").apply();
                                tools.customToast(mC,"Reset éffectué","center");
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                break;
            case "appli_reset_hall":
                new AlertDialog.Builder(mC)
                        .setTitle("Demande de confirmation")
                        .setMessage("Confirmes-tu l'action de réinitialiser le Panthéon personnage ?")
                        .setIcon(android.R.drawable.ic_menu_help)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                aquene.getHallOfFame().reset();
                                tools.customToast(mC,"Reset éffectué","center");
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                break;
            case "export_save":
                Intent intentSave = new Intent(mC, SaveSharedPreferencesActivity.class);
                intentSave.putExtra("ACTION_TYPE", "SAVE");
                intentSave.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mC.startActivity(intentSave);
                break;
            case "import_save":
                Intent intentLoad = new Intent(mC, SaveSharedPreferencesActivity.class);
                intentLoad.putExtra("ACTION_TYPE", "LOAD");
                intentLoad.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mC.startActivity(intentLoad);
                break;
            case "send_report":
                log.sendReport(getActivity());
        }
    }
}