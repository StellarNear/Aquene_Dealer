package stellarnear.aquene_dealer.Divers.SettingsFragments;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceCategory;
import android.preference.SwitchPreference;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.Perso.MythicFeat;
import stellarnear.aquene_dealer.Perso.Perso;

public class PrefMythicFragment {
    private Perso aquene = MainActivity.aquene;
    private Activity mA;
    private Context mC;
    private Tools tools = new Tools();

    public PrefMythicFragment(Activity mA, Context mC) {
        this.mA = mA;
        this.mC = mC;
    }

    public void addMythicFeatsList(PreferenceCategory active, PreferenceCategory def,PreferenceCategory atk ,PreferenceCategory other ,PreferenceCategory stance) {
           for (MythicFeat feat : aquene.getAllMythicFeats().getMythicFeatsList()) {
            SwitchPreference switch_feat = new SwitchPreference(mC);
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
            }
        }
    }

    public void refreshMythicTierBar() {
    }
}
