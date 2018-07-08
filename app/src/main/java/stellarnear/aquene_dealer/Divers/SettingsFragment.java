package stellarnear.aquene_dealer.Divers;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.R;

public class SettingsFragment extends PreferenceFragment {
    private int xmlId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xmlId = R.xml.pref_leg_ring;
        addPreferencesFromResource(xmlId);
    }

    public void changePrefScreen(int xmlId, String titleId) {
        this.xmlId = xmlId;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(titleId);
        getPreferenceScreen().removeAll();
        addPreferencesFromResource(xmlId);
    }

    // will be called by SettingsActivity (Host Activity)
    public void onUpButton() {
        if (xmlId == R.xml.pref_leg_ring) // in top-level
        {
            // Switch to MainActivity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else // in sub-level
        {
            changePrefScreen(R.xml.pref_leg_ring, "lolilol");
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();

        //
        // Top level PreferenceScreen
        //
        if (key.equals("top_key_0")) {
            changePrefScreen(R.xml.pref_general, "on est dessous"); // descend into second level
        }

        // ...

        //
        // Second level PreferenceScreens
        //
        if (key.equals("second_level_key_0")) {
            // do something...
        }

        // ...

        return true;
    }

}