package stellarnear.aquene_dealer.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.Perso.Posture;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 26/12/2017.
 */

public class StanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stance_activity);
        checkOrientStart(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Perso Aquene = new Perso(getApplicationContext());

        LinearLayout stance_lin = findViewById(R.id.stance_linear);

        LinearLayout all_rows_stances= new LinearLayout(this);
        all_rows_stances.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        all_rows_stances.setOrientation(LinearLayout.VERTICAL);
        all_rows_stances.setWeightSum(3);

        LinearLayout posture_row= new LinearLayout(this);

        RadioGroup select_stance = new RadioGroup(this);

        int n_stance_row=0;
        for (Posture stance : Aquene.getStances().getStancesList()){
            if (n_stance_row==0) {
                posture_row= new LinearLayout(this);
                posture_row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1));
                posture_row.setOrientation(LinearLayout.HORIZONTAL);
                posture_row.setWeightSum(6);
                all_rows_stances.addView(posture_row);
            }
            n_stance_row+=1;

            LinearLayout element_stance= new LinearLayout(this);
            element_stance.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1));
            element_stance.setOrientation(LinearLayout.VERTICAL);
            element_stance.setGravity(Gravity.CENTER);

            ImageView icon =new ImageView(this);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.stance_icon),(int)getResources().getDimension(R.dimen.stance_icon));
            icon.setLayoutParams(parms);
            if (stance.getName().equals("Dragon")){icon.setBackgroundColor(Color.GRAY);}
            icon.setImageDrawable(stance.getImg());
            element_stance.addView(icon);

            TextView name_text = new TextView(this);
            name_text.setText(stance.getName());
            name_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            name_text.setTextColor(Color.DKGRAY);
            element_stance.addView(name_text);

            posture_row.addView(element_stance);

            if (n_stance_row>=6) {n_stance_row=0;}
        }

        stance_lin.addView(all_rows_stances);


    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                Intent intent_main = new Intent(StanceActivity.this, MainActivity.class);
                startActivity(intent_main);
                break;

            case Surface.ROTATION_90:
                //on y est deja
                break;

            case Surface.ROTATION_270:
                Intent intent_help = new Intent(StanceActivity.this, HelpActivity.class);
                startActivity(intent_help);
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
