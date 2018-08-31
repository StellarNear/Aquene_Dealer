package stellarnear.aquene_dealer.Divers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Equipment;
import stellarnear.aquene_dealer.Perso.Feat;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.Perso.Skill;
import stellarnear.aquene_dealer.R;

public class SettingsFragment extends PreferenceFragment {
    private List<Integer> histoXML = new ArrayList<>();
    private List<String> histoTitle = new ArrayList<>();
    private Perso aquene = MainActivity.aquene;
    private List<View> additionalsViews = new ArrayList<>();
    private Tools tools = new Tools();
    private SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        addPreferencesFromResource(R.xml.pref);
        histoXML.add(R.xml.pref);
        histoTitle.add(getResources().getString(R.string.setting_activity));
    }

    public void changePrefScreen(int xmlId, String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title.toString());
        getPreferenceScreen().removeAll();
        addPreferencesFromResource(xmlId);
    }

    // will be called by SettingsActivity (Host Activity)
    public void onUpButton() {
        if (histoXML.get(histoXML.size() - 1) == R.xml.pref || histoXML.size() <= 1) // in top-level
        {
            Intent intent = new Intent(getActivity(), MainActivity.class);// Switch to MainActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

        } else // in sub-level
        {
            cleanAdditionals();
            changePrefScreen(histoXML.get(histoXML.size() - 2), histoTitle.get(histoTitle.size() - 2));
            histoXML.remove(histoXML.size() - 1);
            histoTitle.remove(histoTitle.size() - 1);
        }
    }

    private void cleanAdditionals() {
        ContentFrameLayout window = (ContentFrameLayout) getActivity().findViewById(android.R.id.content);
        for (View v : additionalsViews) {
            window.removeView(v);
        }
        additionalsViews = new ArrayList<>();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        int xmlID = getResources().getIdentifier(key, "xml", getContext().getPackageName());
        if (key.contains("pref_")) {
            histoXML.add(xmlID);
            histoTitle.add(preference.getTitle().toString());
            changePrefScreen(xmlID, preference.getTitle().toString());
            switch (key) {
                case "pref_character_feat":
                    addFeatsList();
                    break;
                case "pref_character_skill":
                    addSkillsList();
                    break;
                case "pref_inventory_equipment":
                    addEditableEquipment();
                    break;
                case "pref_inventory_bag":
                    addBagList();
                    break;
                case "pref_character_xp":
                    BigInteger xp = tools.toBigInt(settings.getString("current_xp", String.valueOf(getContext().getResources().getInteger(R.integer.current_xp_def))));
                    checkLevel(xp);
                    refreshXpBar();
                    break;
            }
        } else {
            switch (key) {
                case "infos":
                    histoXML.add(xmlID);
                    histoTitle.add(preference.getTitle().toString());
                    getPreferenceScreen().removeAll();
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(preference.getTitle().toString());
                    addInfos();
                    break;
                case "reset_para":
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(preference.getTitle().toString());
                    ((ContentFrameLayout) getActivity().findViewById(android.R.id.content)).removeAllViews();
                    addResetScreen();
                    break;
                case "sleep":
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(preference.getTitle().toString());
                    ((ContentFrameLayout) getActivity().findViewById(android.R.id.content)).removeAllViews();
                    addSleepScreen();
                    break;
                case "show_equipment":
                    aquene.getInventory().showEquipment(getActivity(), true);
                    break;
                case "add_gold":
                    preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object o) {
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
                            BigInteger xp = tools.toBigInt(settings.getString("current_xp", String.valueOf(getContext().getResources().getInteger(R.integer.current_xp_def))));
                            BigInteger addXp = tools.toBigInt(o.toString());
                            settings.edit().putString("current_xp", xp.add(addXp).toString()).apply();
                            settings.edit().putString("add_current_xp", String.valueOf(0)).apply();
                            checkLevel(xp, addXp);
                            getPreferenceScreen().removeAll();
                            addPreferencesFromResource(R.xml.pref_character_xp); //pour refresh le current
                            refreshXpBar();
                            return true;
                        }
                    });
                    break;
                case "current_xp":
                    preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object o) {
                            settings.edit().putString(preference.getKey(), o.toString()).apply();
                            checkLevel(tools.toBigInt(o.toString()));
                            getPreferenceScreen().removeAll();
                            addPreferencesFromResource(R.xml.pref_character_xp); //pour refresh le current
                            refreshXpBar();
                            return true;
                        }
                    });
                    break;

                case "create_bag_item":
                    createBagItem();
                    break;
                case "create_equipment":
                    createEquipment();
                    break;
                case "reset_temp":
                    resetTemp();
                    getPreferenceScreen().removeAll();
                    addPreferencesFromResource(R.xml.pref_temp); //pour refresh le current
                    break;
            }
        }
        /*
        // Top level PreferenceScreen
        if (key.equals("top_key_0")) {         changePrefScreen(R.xml.pref_general, preference.getTitle().toString()); // descend into second level    }

        // Second level PreferenceScreens
        if (key.equals("second_level_key_0")) {        // do something...    }       */
        return true;
    }

    private void addSleepScreen() {
        View window = getActivity().findViewById(android.R.id.content);
        window.setBackgroundResource(R.drawable.sleep_background);
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Repos")
                .setMessage("Es-tu sûre de vouloir te reposer maintenant ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sleep();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void sleep() {
        final Tools tools = new Tools();
        tools.customToast(getContext(), "Fais de beaux rêves !", "center");
        int time = 2000; // in milliseconds
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.aquene.getAllResources().sleepReset();
                resetTemp();
                tools.customToast(getContext(), "Une nouvelle journée pleine de mandales et d'acrobaties t'attends.", "center");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }, time);
    }

    private void resetTemp() {
        List<String> allTempList = Arrays.asList("bonus_temp_jet_att","bonus_temp_jet_dmg","bonus_temp_ca","bonus_temp_save","bonus_temp_rm","bonus_ki_armor");
        for (String temp : allTempList){
            settings.edit().putString(temp, "0").apply();
        }
        settings.edit().putBoolean("switch_temp_rapid", false).apply();
    }

    private void addResetScreen() {
        View window = getActivity().findViewById(android.R.id.content);
        window.setBackgroundResource(R.drawable.reset_background);
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Remise à zéro des paramètres")
                .setMessage("Es-tu sûre de vouloir réinitialiser ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearSettings();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void clearSettings() {
        int time = 1500; // in milliseconds
        final Tools tools = new Tools();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                aquene.getAllResources().sleepReset();
                aquene.getInventory().resetInventory();
                tools.customToast(getContext(), "Remise à zero des paramètres de l'application", "center");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }, time);
    }

    private void addInfos() {
        ContentFrameLayout window = (ContentFrameLayout) getActivity().findViewById(android.R.id.content);
        ImageView ivBackground = new ImageView(getContext());
        ivBackground.setImageDrawable(getResources().getDrawable(R.mipmap.logo_near));
        window.addView(ivBackground);
        additionalsViews.add(ivBackground);
        LinearLayout page_info = new LinearLayout(getContext());
        page_info.setOrientation(LinearLayout.VERTICAL);
        window.addView(page_info);
        additionalsViews.add(page_info);
        TextView version = new TextView(getContext());
        version.setText("Version actuelle : " + getResources().getString(R.string.version));
        version.setTextSize(22);
        page_info.addView(version);
        TextView time = new TextView(getContext());
        time.setText("Temps de travail nécessaire : " + getResources().getString(R.string.time_spend));
        page_info.addView(time);
        ScrollView scroll_info = new ScrollView(getContext());
        page_info.addView(scroll_info);
        final TextView texte_infos = new TextView(getContext());
        texte_infos.setSingleLine(false);
        texte_infos.setTextColor(Color.DKGRAY);
        texte_infos.setText(getResources().getString(R.string.basic_infos));
        scroll_info.addView(texte_infos);
        final Button button = new Button(getContext());
        button.setText("Patch notes");
        button.setTextSize(18);
        button.setElevation(10);
        page_info.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                texte_infos.setText(getResources().getString(R.string.patch_list));
                button.setVisibility(View.GONE);
            }
        });
    }

    private void addFeatsList() {
        PreferenceScreen screen = this.getPreferenceScreen();
        PreferenceCategory active = (PreferenceCategory) findPreference(getString(R.string.feat_active));
        active.setTitle(getString(R.string.feat_active));
        screen.addPreference(active);
        PreferenceCategory def = (PreferenceCategory) findPreference(getString(R.string.feat_def));
        def.setTitle(getString(R.string.feat_def));
        screen.addPreference(def);
        PreferenceCategory atk = (PreferenceCategory) findPreference(getString(R.string.feat_atk));
        atk.setTitle(getString(R.string.feat_atk));
        screen.addPreference(atk);
        PreferenceCategory other = (PreferenceCategory) findPreference(getString(R.string.feat_other));
        other.setTitle(getString(R.string.feat_other));
        screen.addPreference(other);
        PreferenceCategory stance = (PreferenceCategory) findPreference(getString(R.string.feat_stance));
        stance.setTitle(getString(R.string.feat_stance));
        screen.addPreference(stance);

        for (Feat feat : aquene.getAllFeats().getFeatsList()) {
            SwitchPreference switch_feat = new SwitchPreference(getContext());
            switch_feat.setKey(feat.getId());
            switch_feat.setTitle(feat.getName());
            switch_feat.setSummary(feat.getDescr());
            switch_feat.setDefaultValue(feat.isActive());
            if (feat.getType().contains("feat_active")) {
                active.addPreference(switch_feat);
            } else if (feat.getType().equals("feat_def")) {
                def.addPreference(switch_feat);
            } else if (feat.getType().contains("feat_atk")) {
                atk.addPreference(switch_feat);
            } else if (feat.getType().contains("feat_other")) {
                other.addPreference(switch_feat);
            } else if (feat.getType().contains("feat_stance")) {
                stance.addPreference(switch_feat);
            } else {
                screen.addPreference(switch_feat);
            }
        }
        setHasOptionsMenu(true);
    }

    private void addSkillsList() {
        PreferenceCategory rank = (PreferenceCategory) findPreference(getString(R.string.skill_mastery));
        PreferenceCategory bonus = (PreferenceCategory) findPreference(getString(R.string.skill_bonus));
        for (Skill skill : aquene.getAllSkills().getSkillsList()) {
            EditTextPreference pref = new EditTextPreference(getContext(), InputType.TYPE_CLASS_TEXT);
            pref.setKey(skill.getId() + "_rank");
            pref.setTitle(skill.getName());
            int rankDefId = getContext().getResources().getIdentifier(skill.getId() + "_rankDEF", "integer", getContext().getPackageName());
            int rankDef = getContext().getResources().getInteger(rankDefId);
            pref.setDefaultValue(String.valueOf(rankDef));
            pref.setSummary("Valeur : %s");
            rank.addPreference(pref);
            EditTextPreference pref_bonus = new EditTextPreference(getContext(), InputType.TYPE_CLASS_TEXT);
            pref_bonus.setKey(skill.getId() + "_bonus");
            pref_bonus.setTitle(skill.getName());
            int bonusDefId = getContext().getResources().getIdentifier(skill.getId() + "_bonusDEF", "integer", getContext().getPackageName());
            int bonusDef = getContext().getResources().getInteger(bonusDefId);
            pref_bonus.setDefaultValue(String.valueOf(bonusDef));
            pref_bonus.setSummary("Valeur : %s");
            bonus.addPreference(pref_bonus);
        }
        setHasOptionsMenu(true);
    }

    private void addBagList() {
        PreferenceCategory bagList = (PreferenceCategory) findPreference("bag_list_category");
        for (final Equipment equi : aquene.getInventory().getBag().getListBag()) {
            Preference pref = new Preference(getContext());
            pref.setKey("bag_" + equi.getName());
            pref.setTitle(equi.getName());
            String txt = "Valeur : " + equi.getValue();
            if (!equi.getSlotId().equalsIgnoreCase("")) {
                txt += "\nTag : " + equi.getSlotId();
            }
            if (!equi.getDescr().equalsIgnoreCase("")) {
                txt += "\n" + equi.getDescr();
            }
            pref.setSummary(txt);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(getContext())
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .setTitle("Suppression de l'objet")
                            .setMessage("Es-tu sûre de vouloir jeter cet objet ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    aquene.getInventory().getBag().remove(equi);
                                    getPreferenceScreen().removeAll();
                                    addPreferencesFromResource(R.xml.pref_inventory_bag);
                                    addBagList();
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                    return false;
                }
            });
            bagList.addPreference(pref);
        }
    }

    private void createBagItem() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View creationView = inflater.inflate(R.layout.custom_toast_bag_item_creation, null);

        CustomAlertDialog creationItemAlert = new CustomAlertDialog(getActivity(), getContext(), creationView);
        creationItemAlert.setPermanent(true);
        creationItemAlert.addConfirmButton("Créer");
        creationItemAlert.addCancelButton("Annuler");
        creationItemAlert.setAcceptEventListener(new CustomAlertDialog.OnAcceptEventListener() {
            @Override
            public void onEvent() {
                String name = ((EditText) creationView.findViewById(R.id.name_item_creation)).getText().toString();
                String value = ((EditText) creationView.findViewById(R.id.value_item_creation)).getText().toString()+ " po";
                String tag = ((EditText) creationView.findViewById(R.id.tag_item_creation)).getText().toString();
                String descr = ((EditText) creationView.findViewById(R.id.descr_item_creation)).getText().toString();
                Equipment equi = new Equipment(name, descr, value, "", tag, false);
                aquene.getInventory().getBag().createItem(equi);
                getPreferenceScreen().removeAll();
                addPreferencesFromResource(R.xml.pref_inventory_bag);
                addBagList();
                tools.customToast(getContext(), equi.getName() + " ajouté !");
            }
        });
        creationItemAlert.showAlert();
        final EditText editName = ((EditText) creationView.findViewById(R.id.name_item_creation));
        editName.post(new Runnable() {
            public void run() {
                editName.setFocusableInTouchMode(true);
                editName.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }
    private void addEditableEquipment(){
        addOtherSlotEquipment();
        addSpareEquipmentList();
    }

    private void addOtherSlotEquipment() {
        PreferenceCategory spareList = (PreferenceCategory) findPreference("other_slot_equipment_list_category");
        for (final Equipment equi : aquene.getInventory().getAllEquipments().getSlotListEquipment("other_slot")) {
            Preference pref = new Preference(getContext());
            pref.setKey("equipment_" + equi.getName());
            pref.setTitle(equi.getName());
            String txt = "Valeur : " + equi.getValue();
            if (!equi.getSlotId().equalsIgnoreCase("")) {
                txt += "\nEmplacement : " + translateSlotName(equi.getSlotId());
            }
            if (!equi.getDescr().equalsIgnoreCase("")) {
                txt += "\n" + equi.getDescr();
            }
            pref.setSummary(txt);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(getContext())
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .setTitle("Suppression de l'équipement")
                            .setMessage("Es-tu sûre de vouloir jeter cet équipement ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    aquene.getInventory().getAllEquipments().remove(equi);
                                    getPreferenceScreen().removeAll();
                                    addPreferencesFromResource(R.xml.pref_inventory_equipment);
                                    addEditableEquipment();
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                    return false;
                }
            });
            spareList.addPreference(pref);
        }
    }

    private void addSpareEquipmentList() {
        PreferenceCategory spareList = (PreferenceCategory) findPreference("spare_equipment_list_category");
        for (final Equipment equi : aquene.getInventory().getAllEquipments().getAllSpareEquipment()) {
            Preference pref = new Preference(getContext());
            pref.setKey("equipment_" + equi.getName());
            pref.setTitle(equi.getName());
            String txt = "Valeur : " + equi.getValue();
            if (!equi.getSlotId().equalsIgnoreCase("")) {
                txt += "\nEmplacement : " + translateSlotName(equi.getSlotId());
            }
            if (!equi.getDescr().equalsIgnoreCase("")) {
                txt += "\n" + equi.getDescr();
            }
            pref.setSummary(txt);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(getContext())
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .setTitle("Suppression de l'équipement")
                            .setMessage("Es-tu sûre de vouloir jeter cet équipement ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    aquene.getInventory().getAllEquipments().remove(equi);
                                    getPreferenceScreen().removeAll();
                                    addPreferencesFromResource(R.xml.pref_inventory_equipment);
                                    addEditableEquipment();
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                    return false;
                }
            });
            spareList.addPreference(pref);
        }
    }

    private String translateSlotName(String slotId) {
        String val="";
        String[] vals= getContext().getResources().getStringArray(R.array.slot_choice_val);
        String[] name= getContext().getResources().getStringArray(R.array.slot_choice_name);

        for (int i=0; i < vals.length;i++){
            if(vals[i].equalsIgnoreCase(slotId)){val=name[i]; break;}
        }
        return val;
    }

    private void createEquipment() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View selectItem = inflater.inflate(R.layout.equipment_dialog, null);
        ((TextView)selectItem.findViewById(R.id.equipment_dialog_main_title)).setText("Selectionnes l'emplacement");
        selectItem.findViewById(R.id.bag).setVisibility(View.GONE);
        selectItem.findViewById(R.id.equipment_dialog_back_arrow).setVisibility(View.GONE);
        final CustomAlertDialog selectItemAlert = new CustomAlertDialog(getActivity(), getContext(), selectItem);
        selectItemAlert.setPermanent(true);
        selectItemAlert.addCancelButton("Annuler");
        for (final String slot : getContext().getResources().getStringArray(R.array.slot_choice_val)){
            int resID = getContext().getResources().getIdentifier(slot, "id", getContext().getPackageName());
            ImageView img = (ImageView) selectItem.findViewById(resID);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItemAlert.dismissAlert();
                    createEquipment(slot);
                }
            });
        }
        selectItemAlert.showAlert();
    }
    private void createEquipment(final String slot){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View creationView = inflater.inflate(R.layout.custom_toast_equipment_creation, null);
        CustomAlertDialog creationEquipmentAlert = new CustomAlertDialog(getActivity(), getContext(), creationView);
        creationEquipmentAlert.setPermanent(true);
        creationEquipmentAlert.addConfirmButton("Créer");
        creationEquipmentAlert.addCancelButton("Annuler");
        creationEquipmentAlert.setAcceptEventListener(new CustomAlertDialog.OnAcceptEventListener() {
            @Override
            public void onEvent() {
                Boolean equiped = false;
                if (slot.equalsIgnoreCase("other_slot")){ equiped=true;}
                String name = ((EditText) creationView.findViewById(R.id.name_equipment_creation)).getText().toString();
                String value = ((EditText) creationView.findViewById(R.id.value_equipment_creation)).getText().toString()+ " po";
                String descr = ((EditText) creationView.findViewById(R.id.descr_equipment_creation)).getText().toString();
                Equipment equi = new Equipment(name, descr, value, "equipment_"+slot+"_def", slot, equiped);
                aquene.getInventory().getAllEquipments().createEquipment(equi);
                getPreferenceScreen().removeAll();
                addPreferencesFromResource(R.xml.pref_inventory_equipment);
                addEditableEquipment();
                tools.customToast(getContext(), equi.getName() + " ajouté !");
            }
        });
        creationEquipmentAlert.showAlert();
        final EditText editName = ((EditText) creationView.findViewById(R.id.name_equipment_creation));
        editName.post(new Runnable() {
            public void run() {
                editName.setFocusableInTouchMode(true);
                editName.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }


    private void refreshXpBar() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final View mainView = ((ContentFrameLayout) getActivity().findViewById(android.R.id.content));
                mainView.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView percent = mainView.findViewById(R.id.xp_bar_percent);
                        ImageView backgroundBar = mainView.findViewById(R.id.xp_bar_background);
                        ViewGroup.LayoutParams para= (ViewGroup.LayoutParams) backgroundBar.getLayoutParams();
                        ImageView overlayBar = mainView.findViewById(R.id.xp_bar_overlay);
                        int oriWidth=overlayBar.getMeasuredWidth();
                        int oriHeight=overlayBar.getMeasuredHeight();
                        int currentXp = tools.toInt(settings.getString("current_xp",String.valueOf(getContext().getResources().getInteger(R.integer.current_xp_def))));
                        int nextLvlXp = tools.toInt(settings.getString("next_level",String.valueOf(getContext().getResources().getInteger(R.integer.next_level_def))));
                        int previousLvlXp = tools.toInt(settings.getString("previous_level",String.valueOf(getContext().getResources().getInteger(R.integer.previous_level_def))));
                        Double coef = (double) (currentXp-previousLvlXp)/(nextLvlXp-previousLvlXp);
                        if(coef<0d){coef=0d;}
                        if(coef>1d){coef=1d;}
                        percent.setText(String.valueOf((int) (100*coef))+"%");
                        para.width=(int) (coef*oriWidth);
                        para.height=oriHeight;
                        backgroundBar.setLayoutParams(para);
                    }
                });

            }
        }, 50); //pour attendre le changement de preference visiblement ce n'est pas instantané
    }

    private void checkLevel(BigInteger currentXp, BigInteger... addXpInput) {
        BigInteger addXp = addXpInput.length > 0 ? addXpInput[0] : BigInteger.ZERO;
        List<String> listLvlXp = Arrays.asList(getResources().getStringArray(R.array.xp_lvl_needed));
        List<Integer> listLvl = new ArrayList<>();
        List<BigInteger> listXp = new ArrayList<>();
        for (String line : listLvlXp) {
            listLvl.add(tools.toInt(line.substring(0, line.indexOf(":"))));
            listXp.add(tools.toBigInt(line.substring(line.indexOf(":") + 1, line.length())));
        }

        int newLvl = 0;
        for (BigInteger xp : listXp) {
            if ((currentXp.add(addXp)).compareTo(xp) >= 0) {
                newLvl = listLvl.get(listXp.indexOf(xp));
            }
        }

        Integer currentLvl = tools.toInt(settings.getString("ability_lvl", String.valueOf(getContext().getResources().getInteger(R.integer.ability_lvl_def))));
        if (currentLvl != newLvl) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
            BigInteger previousLvlXp = listXp.get(listLvl.indexOf(newLvl));
            BigInteger nextLvlXp = listXp.get(listLvl.indexOf(newLvl+1));

            settings.edit().putString("previous_level", previousLvlXp.toString()).apply();
            settings.edit().putString("next_level", nextLvlXp.toString()).apply();
            settings.edit().putString("ability_lvl", String.valueOf(newLvl)).apply();
            tools.playVideo(getActivity(),getContext(),"/raw/saiyan");
            tools.customToast(getContext(), "Bravo tu as atteint le niveau "+String.valueOf(newLvl));
        }
    }

}