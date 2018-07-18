package stellarnear.aquene_dealer.Divers;

import android.app.AlertDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Perso.Feat;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.Perso.Skill;
import stellarnear.aquene_dealer.R;

public class SettingsFragment extends PreferenceFragment {
    private List<Integer> histoXML = new ArrayList<>();
    private List<String> histoTitle = new ArrayList<>();
    private Perso aquene = MainActivity.aquene;
    private List<View> additionalsViews = new ArrayList<>();
    private Tools tools=new Tools();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    aquene.getInventory().showEquipment(getActivity());
                    break;

                case "add_current_xp":
                    preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object o) {
                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                            int xp = tools.toInt(settings.getString("current_xp", String.valueOf(getContext().getResources().getInteger(R.integer.current_xp_def))));
                            settings.edit().putString("current_xp",String.valueOf(xp+tools.toInt(o.toString()))).apply();
                            settings.edit().putString("add_current_xp",String.valueOf(0)).apply();
                            getPreferenceScreen().removeAll();
                            addPreferencesFromResource(R.xml.pref_character_xp); //pour refresh le current
                            return true;
                        }
                    });
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
                tools.customToast(getContext(), "Une nouvelle journée pleine de mandales et d'acrobaties t'attends.", "center");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }, time);
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
}