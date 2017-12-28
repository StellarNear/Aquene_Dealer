package stellarnear.aquene_dealer.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.Perso.Posture;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 26/12/2017.
 */

public class StanceActivity extends AppCompatActivity {

    Map<RadioButton,Posture> Map_radio_buton_Stance = new HashMap<RadioButton, Posture>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stance_activity);
        checkOrientStart(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Perso Aquene = new Perso(getApplicationContext());

        LinearLayout all_rows_stances = findViewById(R.id.stance_linear);
        all_rows_stances.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        all_rows_stances.setWeightSum(12);
        all_rows_stances.setGravity(Gravity.CENTER_HORIZONTAL);

        RadioGroup select_stance_att = new RadioGroup(this);
        setParam(select_stance_att);
        LinearLayout select_stance_att_names = new LinearLayout(this);
        setParam(select_stance_att_names);

        all_rows_stances.addView(select_stance_att);
        all_rows_stances.addView(select_stance_att_names);

        RadioGroup select_stance_def = new RadioGroup(this);
        setParam(select_stance_def);
        LinearLayout select_stance_def_names = new LinearLayout(this);
        setParam(select_stance_def_names);

        all_rows_stances.addView(select_stance_def);
        all_rows_stances.addView(select_stance_def_names);

        RadioGroup select_stance_autre = new RadioGroup(this);

        setParam(select_stance_autre);
        LinearLayout select_stance_autre_names = new LinearLayout(this);
        setParam(select_stance_autre_names);

        all_rows_stances.addView(select_stance_autre);
        all_rows_stances.addView(select_stance_autre_names);

        for (Posture stance : Aquene.getStances().getStancesList()){
            RadioButton icon =new RadioButton(this);
            icon.setButtonDrawable(null);
            icon.setBackground(stance.getImg());
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
            params.width=(int) getResources().getDimension(R.dimen.stance_icon);
            params.height=(int) getResources().getDimension(R.dimen.stance_icon);
            Log.d("-State-","W"+params.width + "H"+params.height);

            icon.setLayoutParams(params);

            TextView name = new TextView(this);
            name.setText(stance.getName());
            name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1));
            //name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            name.setTextColor(Color.DKGRAY);

            if (stance.getType().equals("attaque")){
                select_stance_att.addView(icon);
                select_stance_att_names.addView(name);
            } else if (stance.getType().equals("defense")) {
                select_stance_def.addView(icon);
                select_stance_def_names.addView(name);
            } else {
                select_stance_autre.addView(icon);
                select_stance_autre_names.addView(name);
            }
/*
            if (stance.getType().equals("attaque")){
                select_stance_att.addView(icon,elementParam(stance.getImg()));
                select_stance_att_names.addView(name,elementParam(stance.getImg()));
            } else if (stance.getType().equals("defense")) {
                select_stance_def.addView(icon,elementParam(stance.getImg()));
                select_stance_def_names.addView(name,elementParam(stance.getImg()));
            } else {
                select_stance_autre.addView(icon,elementParam(stance.getImg()));
                select_stance_autre_names.addView(name,elementParam(stance.getImg()));
            }*/



            Map_radio_buton_Stance.put(icon,stance);
        }

        /*
        select_stance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("-STATE-","on passe dans le change radio");
                // checkedId is the RadioButton
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    refreshText(Map_radio_buton_Stance.get(checkedRadioButton).getName());
                }

                //refreshText(Map_id_radio_buton_Stance.get(checkedId).getName());
            }
        });  */


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

    private void setParam(RadioGroup radio) {
        radio.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,3);
        layoutParams.height=0;
        radio.setLayoutParams(layoutParams);
        radio.setWeightSum(6);
        radio.requestLayout();
    }

    private void setParam(LinearLayout lin) {
        lin.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        layoutParams.height=0;
        lin.setLayoutParams(layoutParams);
        lin.setWeightSum(6);
        lin.requestLayout();
    }

    /*
    private LinearLayout.LayoutParams elementParam(Drawable img) {
        int padding =(int)getResources().getDimension(R.dimen.stance_icon_padding);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        layoutParams.setMargins(padding,padding,padding,padding);
        smartResize(layoutParams,img);
        return layoutParams;
    }

    private void smartResize(LinearLayout.LayoutParams layoutParams, Drawable img) {
        int max_dim;
        if (img.getIntrinsicHeight()>img.getIntrinsicWidth()){max_dim=img.getIntrinsicHeight();}else{max_dim=img.getIntrinsicWidth();}
        int padding =(int)getResources().getDimension(R.dimen.stance_icon_padding);
      //pour avoir un resize relatif à l'ecran

        //layout.getMeasuredWidth();
        Float coef = (getResources().getDimension(R.dimen.stance_icon) / max_dim);

        layoutParams.width = (int) ((img.getIntrinsicWidth()+padding)*coef);
        layoutParams.height=(int) ((img.getIntrinsicHeight()+padding)*coef);
    }   HARD WAY FAIL */
}
