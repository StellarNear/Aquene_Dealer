package stellarnear.aquene_dealer.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;

import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 26/12/2017.
 */

public class HelpActivity extends AppCompatActivity {
    Perso aquene = MainActivity.aquene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (settings.getBoolean("switch_fullscreen_mode",getApplicationContext().getResources().getBoolean(R.bool.switch_fullscreen_mode_DEF))) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help_activity);

        String title=getString(R.string.help_activity); //todo stacking des sub directeory genre Help center : Standard action : Round : Attack
        SpannableString titleSpan = new SpannableString(title);
        titleSpan.setSpan(new ForegroundColorSpan(getColor(R.color.textColorPrimary)),0,title.length(),0);
        titleSpan.setSpan(new RelativeSizeSpan(1.5f)  ,0,getString(R.string.help_activity).length(),0);
        Toolbar mActionBarToolbarhelp = (Toolbar) findViewById(R.id.toolbarHelp);
        setSupportActionBar(mActionBarToolbarhelp);
        getSupportActionBar().setTitle(titleSpan);
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkOrientStart(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    @Override
    protected void onDestroy() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        finish();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if (display.getRotation()==Surface.ROTATION_0) {
            Intent intent_main = new Intent(HelpActivity.this, MainActivity.class);
            startActivity(intent_main);
        }
        if (display.getRotation()==Surface.ROTATION_180) {
            Intent intent_main = new Intent(HelpActivity.this, MainActivity.class);
            startActivity(intent_main);
        }
    }

    private void checkOrientStart(int screenOrientation) {
        if (getRequestedOrientation()!=screenOrientation) {
            setRequestedOrientation(screenOrientation);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                }
            }, 2000);

        }
    }
}
