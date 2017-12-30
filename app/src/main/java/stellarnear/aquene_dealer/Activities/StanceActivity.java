package stellarnear.aquene_dealer.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.Perso.Stance;
import stellarnear.aquene_dealer.R;

/**
 * Created by jchatron on 26/12/2017.
 */

public class StanceActivity extends AppCompatActivity {

    Map<RadioButton,Stance> Map_radio_buton_Stance = new HashMap<RadioButton, Stance>();
    List<RadioGroup> listRadioGroups= new ArrayList<RadioGroup>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stance_activity);
        Perso Aquene = new Perso(getApplicationContext());

        LinearLayout all_rows_stances = findViewById(R.id.stance_linear);
        //all_rows_stances.setWeightSum(12);

        RadioGroup select_stance_att = new RadioGroup(this);
        listRadioGroups.add(select_stance_att);
        setListnerMultiRadio(select_stance_att);

        LinearLayout select_stance_att_names = new LinearLayout(this);

        all_rows_stances.addView(select_stance_att);
        all_rows_stances.addView(select_stance_att_names);

        RadioGroup select_stance_def = new RadioGroup(this);
        listRadioGroups.add(select_stance_def);
        setListnerMultiRadio(select_stance_def);

        LinearLayout select_stance_def_names = new LinearLayout(this);

        all_rows_stances.addView(select_stance_def);
        all_rows_stances.addView(select_stance_def_names);

        RadioGroup select_stance_else = new RadioGroup(this);
        listRadioGroups.add(select_stance_else);
        setListnerMultiRadio(select_stance_else);

        LinearLayout select_stance_else_names = new LinearLayout(this);

        all_rows_stances.addView(select_stance_else);
        all_rows_stances.addView(select_stance_else_names);

        for (Stance stance : Aquene.getStances().getStancesList()){
            RadioButton icon =new RadioButton(this);
            icon.setButtonDrawable(null);
            icon.setBackground(stance.getImg());
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.width=(int) getResources().getDimension(R.dimen.stance_icon);
            params.height=(int) getResources().getDimension(R.dimen.stance_icon);

            icon.setLayoutParams(params);

            TextView name = new TextView(this);
            name.setText(stance.getShortName());
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(params);
            params2.height= (int)( 4*  getResources().getDimension(R.dimen.text_size_stance_icons));
            name.setLayoutParams(params2);
            name.setSingleLine(true);
            name.setGravity(Gravity.CENTER);
            tooltip(stance,name);
            name.setTextSize(getResources().getDimension(R.dimen.text_size_stance_icons));
            name.setTextColor(Color.DKGRAY);

            if (stance.getType().equals(getResources().getString(R.string.stance_cat_1))){
                select_stance_att.addView(icon);
                select_stance_att_names.addView(name);
            } else if (stance.getType().equals(getResources().getString(R.string.stance_cat_2))) {
                select_stance_def.addView(icon);
                select_stance_def_names.addView(name);
            } else {
                select_stance_else.addView(icon);
                select_stance_else_names.addView(name);
            }

            Map_radio_buton_Stance.put(icon,stance);
        }
        setParam(select_stance_att);
        setParam(select_stance_att_names);
        setParam(select_stance_def);
        setParam(select_stance_def_names);
        setParam(select_stance_else);
        setParam(select_stance_else_names);
    }

    private void tooltip(final Stance stance, TextView name) {
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toatIt(stance.getDescr());
            }
        });
    }

    private void toatIt(String descr) {
        Toast toast = Toast.makeText(getApplicationContext(), descr, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }

    private void setListnerMultiRadio(RadioGroup radio_sub) {
        radio_sub.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("-State-","Changement de bouton");
                // checkedId is the RadioButton
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    //do stuff with active stance
                }
                unCheckAllRadio(group,this);
                //refreshText(Map_id_radio_buton_Stance.get(checkedId).getName());
            }
        });

    }

    private void unCheckAllRadio(RadioGroup selected_group, RadioGroup.OnCheckedChangeListener onCheckedChangeListener) {
        for (RadioGroup group : listRadioGroups){
            if (!group.equals(selected_group)){
                group.setOnCheckedChangeListener(null);
                group.clearCheck();
                group.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkOrientStart(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.height=0;
        //layoutParams.gravity=Gravity.CENTER;
        radio.setLayoutParams(layoutParams);
        //radio.setWeightSum(6);
        //radio.setBackgroundColor(Color.BLUE);
        //radio.requestLayout();
    }

    private void setParam(LinearLayout lin) {
        lin.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.height=0;
        lin.setLayoutParams(layoutParams);
        //lin.setBackgroundColor(Color.GRAY);
        //lin.setWeightSum(6);
        //lin.requestLayout();
    }

}