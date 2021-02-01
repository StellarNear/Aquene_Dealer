package stellarnear.aquene_companion.Divers.SettingsFragments;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceCategory;
import android.text.InputType;

import stellarnear.aquene_companion.Activities.MainActivity;
import stellarnear.aquene_companion.Divers.EditTextPreference;
import stellarnear.aquene_companion.Divers.Tools;
import stellarnear.aquene_companion.Perso.Perso;
import stellarnear.aquene_companion.Perso.Skill;


public class PrefSkillFragment {
        private Perso aquene= MainActivity.aquene;
        private Activity mA;
        private Context mC;
        private Tools tools=Tools.getTools();
        public PrefSkillFragment(Activity mA,Context mC){
            this.mA=mA;
            this.mC=mC;
        }


    public void addSkillsList(PreferenceCategory rank,PreferenceCategory bonus ) {
        for (Skill skill : aquene.getAllSkills().getSkillsList()) {
            EditTextPreference pref = new EditTextPreference(mC, InputType.TYPE_CLASS_TEXT);
            pref.setKey(skill.getId() + "_rank");
            pref.setTitle(skill.getName());
            int rankDefId = mC.getResources().getIdentifier(skill.getId() + "_rankDEF", "integer", mC.getPackageName());
            int rankDef = mC.getResources().getInteger(rankDefId);
            pref.setDefaultValue(String.valueOf(rankDef));
            pref.setSummary("Valeur : %s");
            rank.addPreference(pref);
            EditTextPreference pref_bonus = new EditTextPreference(mC, InputType.TYPE_CLASS_TEXT);
            pref_bonus.setKey(skill.getId() + "_bonus");
            pref_bonus.setTitle(skill.getName());
            int bonusDefId = mC.getResources().getIdentifier(skill.getId() + "_bonusDEF", "integer", mC.getPackageName());
            int bonusDef = mC.getResources().getInteger(bonusDefId);
            pref_bonus.setDefaultValue(String.valueOf(bonusDef));
            pref_bonus.setSummary("Valeur : %s");
            bonus.addPreference(pref_bonus);
        }
       
    }
}
