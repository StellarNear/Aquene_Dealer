package stellarnear.aquene_dealer.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v7.widget.ContentFrameLayout;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import stellarnear.aquene_dealer.Divers.EditTextPreference;
import stellarnear.aquene_dealer.Perso.Feat;
import stellarnear.aquene_dealer.Perso.Skill;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<PreferenceActivity.Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || CombatPreferenceFragment.class.getName().equals(fragmentName)
                || SkillsPreferenceFragment.class.getName().equals(fragmentName)
                || InfosPreferenceFragment.class.getName().equals(fragmentName)
                || RazPreferenceFragment.class.getName().equals(fragmentName)
                || SleepPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     page générale
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // remove dividers
            View rootView = getView();
            ListView list = (ListView) rootView.findViewById(android.R.id.list);
            list.setDivider(getActivity().getDrawable(R.drawable.divider_pref));
        }
    }

    /**
     page de compétence
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SkillsPreferenceFragment extends PreferenceFragment {
        Perso aquene=MainActivity.aquene;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_skill);
            PreferenceCategory rank = (PreferenceCategory) findPreference(getString(R.string.skill_mastery));
            PreferenceCategory bonus = (PreferenceCategory) findPreference(getString(R.string.skill_bonus));

            for (Skill skill : aquene.getAllSkills().getSkillsList()){
                EditTextPreference pref = new EditTextPreference(getContext(), InputType.TYPE_CLASS_TEXT);
                pref.setKey(skill.getId()+"_rank");
                pref.setTitle(skill.getName());
                pref.setDefaultValue(String.valueOf(skill.getVal()));
                pref.setSummary("Valeur : %s");
                rank.addPreference(pref);

                EditTextPreference pref_bonus = new EditTextPreference(getContext(), InputType.TYPE_CLASS_TEXT);
                pref_bonus.setKey(skill.getId()+"_bonus");
                pref_bonus.setTitle(skill.getName());
                pref_bonus.setDefaultValue(String.valueOf(skill.getBonus()));
                pref_bonus.setSummary("Valeur : %s");
                bonus.addPreference(pref_bonus);
            }
            setHasOptionsMenu(true);
        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     page de combat
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CombatPreferenceFragment extends PreferenceFragment {
        Perso aquene=MainActivity.aquene;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_combat);
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

            for (Feat feat : aquene.getAllFeats().getFeatsList()){
                SwitchPreference switch_feat = new SwitchPreference(getContext());
                switch_feat.setKey(feat.getId());
                switch_feat.setTitle(feat.getName());
                switch_feat.setSummary(feat.getDescr());
                switch_feat.setDefaultValue(feat.isActive());
                if(feat.getType().contains("feat_active")){
                    active.addPreference(switch_feat);
                } else if(feat.getType().equals("feat_def")){
                    def.addPreference(switch_feat);
                } else if(feat.getType().contains("feat_atk")){
                    atk.addPreference(switch_feat);
                } else if(feat.getType().contains("feat_other")){
                    other.addPreference(switch_feat);
                } else if(feat.getType().contains("feat_stance")){
                    stance.addPreference(switch_feat);
                } else {
                    screen.addPreference(switch_feat);
                }
            }

            setHasOptionsMenu(true);

        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    /**
     page d'info settings
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class InfosPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            ContentFrameLayout window =(ContentFrameLayout) getActivity().findViewById(android.R.id.content);

            ImageView ivBackground = new ImageView(getContext());
            ivBackground.setImageDrawable(getResources().getDrawable(R.mipmap.logo_near));

            window.addView(ivBackground);


            LinearLayout page_info = new LinearLayout(getContext());
            page_info.setOrientation(LinearLayout.VERTICAL);
            window.addView(page_info);


            TextView Yfa_version = new TextView(getContext());
            Yfa_version.setText("Version actuelle : "+getResources().getString(R.string.version));
            Yfa_version.setTextSize(22);

            page_info.addView(Yfa_version);

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
    }


    /**
     page de reset settings
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class RazPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            View window = getActivity().findViewById(android.R.id.content);
            window.setBackgroundResource(R.drawable.reset_background);


            int time=2000; // in milliseconds

            Handler h=new Handler();

            h.postDelayed(new Runnable() {

                              @Override
                              public void run() {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            String descr="Remise à zero des paramètres de l'application";
            Toast toast = Toast.makeText(getContext(), descr, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL,0,0);
            toast.show();
            startActivity(new Intent(getActivity(), MainActivity.class));
                              }

            },time);

        }
    }


    /**
     page de dodo
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SleepPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            View window = getActivity().findViewById(android.R.id.content);
            window.setBackgroundResource(R.drawable.sleep_background);

            String descr="Fais de beaux rêves !";
            Toast toast = Toast.makeText(getContext(), descr, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL,0,0);
            toast.show();



            int time=2000; // in milliseconds

            Handler h=new Handler();

            h.postDelayed(new Runnable() {

                @Override
                public void run() {


            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = prefs.edit();
            //editor.putString("n_rank_4_conv",prefs.getString("n_rank_4_start_conv",getString(R.string.n_rank_4_def_conv)));
            editor.commit();

            String descr="Une nouvelle journée pleine de sortilèges t'attends.";
            Toast toast = Toast.makeText(getContext(), descr, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL,0,0);
            toast.show();
            startActivity(new Intent(getActivity(), MainActivity.class));
                }

            },time);

        }

        public Integer to_int(String key){
            Integer value;
            try {
                value = Integer.parseInt(key);
            } catch (Exception e){
                value=0;
            }
            return value;
        }
    }



}
