package stellarnear.aquene_dealer.Divers.SettingsFragments;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceCategory;
import android.preference.SwitchPreference;

import java.util.List;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Divers.AllMonkPowersInfos;
import stellarnear.aquene_dealer.Divers.MonkPowerInfo;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.Perso.KiCapacity;
import stellarnear.aquene_dealer.Perso.Perso;

public class PrefCapaFragment {
    private Perso aquene = MainActivity.aquene;
    private Activity mA;
    private Context mC;
    private Tools tools = new Tools();

    public PrefCapaFragment(Activity mA, Context mC) {
        this.mA = mA;
        this.mC = mC;
    }

    public void addCapaList(PreferenceCategory monk, PreferenceCategory ki) {
           for (KiCapacity capacity : aquene.getAllKiCapacitiesList()) {
            SwitchPreference switch_feat = new SwitchPreference(mC);
            switch_feat.setKey("switch_"+capacity.getId());
            switch_feat.setTitle(capacity.getName());
            switch_feat.setSummary(capacity.getDescr());
            switch_feat.setDefaultValue(true);

               ki.addPreference(switch_feat);
        }

        List<MonkPowerInfo> listPowers = new AllMonkPowersInfos(mC).getListMonkPowersInfos();
        for (MonkPowerInfo power : listPowers){
            SwitchPreference switch_feat = new SwitchPreference(mC);
            switch_feat.setKey("switch_"+power.getId());
            switch_feat.setTitle(power.getName());
            switch_feat.setSummary(power.getDescr());
            switch_feat.setDefaultValue(true);

            monk.addPreference(switch_feat);
        }

    }
}
