package stellarnear.aquene_dealer.Divers.SettingsFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import stellarnear.aquene_dealer.Activities.MainActivity;
import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

public class PrefSleepScreenFragment {
    private Perso aquene=MainActivity.aquene;
    private Activity mA;
    private Context mC;

    public PrefSleepScreenFragment(Activity mA, Context mC) {
        this.mA=mA;
        this.mC=mC;
    }

    public void addSleepScreen() {
        View window = mA.findViewById(android.R.id.content);
        window.setBackgroundResource(R.drawable.sleep_background);
        new AlertDialog.Builder(mC)
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
                        Intent intent = new Intent(mA, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        mA.startActivity(intent);
                    }
                })
                .show();
    }


    private void sleep() {
        final Tools tools = new Tools();
        tools.customToast(mC, "Fais de beaux rêves !", "center");
        int time = 2000; // in milliseconds
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.aquene.getAllResources().sleepReset();
                resetTemp();
                tools.customToast(mC, "Une nouvelle journée pleine de mandales et d'acrobaties t'attends.", "center");
                Intent intent = new Intent(mA, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mA.startActivity(intent);
            }
        }, time);
    }

    private void resetTemp() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mC);
        List<String> allTempList = Arrays.asList("bonus_temp_jet_att", "bonus_temp_jet_dmg", "bonus_temp_ca", "bonus_temp_save", "bonus_temp_rm", "bonus_ki_armor","mythiccapacity_absorption");
        for (String temp : allTempList) {
            prefs.edit().putString(temp, "0").apply();
        }
        prefs.edit().putBoolean("switch_temp_rapid", false).apply();
        prefs.edit().putBoolean("switch_blinding_speed", false).apply();

    }

}
