package stellarnear.aquene_dealer.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import stellarnear.aquene_dealer.Divers.EditTextPreference;
import stellarnear.aquene_dealer.Divers.SettingsFragment;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.Perso.Feat;
import stellarnear.aquene_dealer.Perso.Skill;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category pref shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatActivity
{
    SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (settings.getBoolean("switch_fullscreen_mode",getApplicationContext().getResources().getBoolean(R.bool.switch_fullscreen_mode_DEF))) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingsFragment = new SettingsFragment();

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, settingsFragment)
                .commit();
    }

    //
    // Handle what happens on up button
    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                MainActivity.aquene.refresh();
                settingsFragment.onUpButton();
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        MainActivity.aquene.refresh();
        settingsFragment.onUpButton();
    }

    // ...
}