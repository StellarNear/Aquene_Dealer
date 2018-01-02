package stellarnear.aquene_dealer.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 26/12/2017.
 */

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);

    }

    @Override
    protected void onResume(){
        super.onResume();
        checkOrientStart(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
        Runtime.getRuntime().gc();
        System.gc();
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                Intent intent_main = new Intent(HelpActivity.this, MainActivity.class);
                startActivity(intent_main);
                break;

            case Surface.ROTATION_90:
                Intent intent_stance = new Intent(HelpActivity.this, StanceActivity.class);
                startActivity(intent_stance);
                break;

            case Surface.ROTATION_270:
                //on y est deja
                break;
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
            }, 2500);

        }
    }
}
