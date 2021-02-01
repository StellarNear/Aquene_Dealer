package stellarnear.aquene_companion.Divers.SettingsFragments;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import java.util.HashMap;
import java.util.Map;

import stellarnear.aquene_companion.Activities.MainActivity;
import stellarnear.aquene_companion.Divers.Tools;
import stellarnear.aquene_companion.Perso.Capacity;
import stellarnear.aquene_companion.Perso.Perso;

public class PrefCapaFragment {
    private Perso aquene = MainActivity.aquene;
    private Activity mA;
    private Context mC;
    private Tools tools=Tools.getTools();
    public PrefCapaFragment(Activity mA, Context mC) {
        this.mA = mA;
        this.mC = mC;
    }

    public void addCapaList(PreferenceScreen screen) {
        screen.setOrderingAsAdded(true);
        Map<String, PreferenceCategory> map = buildCategoryList(screen);
        for (Capacity capacity : aquene.getAllCapacities().getAllCapacitiesList()) {
            putCapa(capacity,map.get(capacity.getType()));
        }
    }

    private Map<String, PreferenceCategory> buildCategoryList(PreferenceScreen screen) {
        Map<String, PreferenceCategory> mapTypeCat=new HashMap<>();
        for (Capacity capacity : aquene.getAllCapacities().getAllCapacitiesList()) {
            if(mapTypeCat.get(capacity.getType())==null){
                PreferenceCategory newType = new PreferenceCategory(mC);
                newType.setTitle(capacity.getType());
                newType.setKey(capacity.getType());
                screen.addPreference(newType);
                mapTypeCat.put(capacity.getType(),newType);
            }
        }
        return mapTypeCat;
    }


    private void putCapa(final Capacity capacity, PreferenceCategory category) {
        SwitchPreference switch_feat = new SwitchPreference(mC);
        switch_feat.setKey("switch_" + capacity.getId());
        switch_feat.setTitle(capacity.getName());
        String descr="";
        if(capacity.getDailyUse()!=0){
            descr+=capacity.getDailyUse()+"/j ";
        }
        if (capacity.getValue()!=0){
            descr+="Valeur : "+capacity.getValue();
        }
        if(!descr.equalsIgnoreCase("")){
            descr+="\n";
        }
        descr+=capacity.getDescr();
        switch_feat.setSummary(descr);
        switch_feat.setDefaultValue(true);
        category.addPreference(switch_feat);
    }
}
