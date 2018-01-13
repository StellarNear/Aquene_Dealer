package stellarnear.aquene_dealer.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
    Map<Integer,String> mapRadioButtonStance = new HashMap<>();
    Map<String,Integer> mapStanceRadioButton = new HashMap<>();
    List<RadioGroup> listRadioGroups= new ArrayList<RadioGroup>();
    Perso aquene = MainActivity.aquene;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (settings.getBoolean("switch_fullscreen_mode",getApplicationContext().getResources().getBoolean(R.bool.switch_fullscreen_modeDEF))) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stance_activity);

        LinearLayout all_rows_stances = findViewById(R.id.stance_linear);

        createGridSelector(all_rows_stances);

        selectActiveStance();
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkOrientStart(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
        Runtime.getRuntime().gc();
        System.gc();
    }

    private void selectActiveStance() {
        Stance currentStance = aquene.getAllStances().getCurrentStance();
        String title=getString(R.string.stance_activity) +" (sélectionnez une posture)";
        if(currentStance!=null)
        {
            RadioButton radioSelected = findViewById(mapStanceRadioButton.get(currentStance.getId()));
            radioSelected.setChecked(true);
            title=getString(R.string.stance_activity) +" (posture actuelle : "+currentStance.getName()+")";
        }
        SpannableString titleSpan = new SpannableString(title);
        titleSpan.setSpan(new ForegroundColorSpan(getColor(R.color.textColorPrimary)),0,title.length(),0);
        titleSpan.setSpan(new RelativeSizeSpan(1.5f)  ,0,getString(R.string.stance_activity).length()+1,0);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbarStance);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(titleSpan);
    }

    private void createGridSelector(LinearLayout allRowsStances) {
        RadioGroup selectStanceAtt = new RadioGroup(this);
        listRadioGroups.add(selectStanceAtt);
        setListnerMultiRadio(selectStanceAtt);

        LinearLayout selectStanceAttNames = new LinearLayout(this);

        allRowsStances.addView(selectStanceAtt);
        allRowsStances.addView(selectStanceAttNames);

        RadioGroup selectStanceDef = new RadioGroup(this);
        listRadioGroups.add(selectStanceDef);
        setListnerMultiRadio(selectStanceDef);

        LinearLayout selectStanceDefNames = new LinearLayout(this);

        allRowsStances.addView(selectStanceDef);
        allRowsStances.addView(selectStanceDefNames);

        RadioGroup selectStanceElse = new RadioGroup(this);
        listRadioGroups.add(selectStanceElse);
        setListnerMultiRadio(selectStanceElse);

        LinearLayout selectStanceElseNames = new LinearLayout(this);

        allRowsStances.addView(selectStanceElse);
        allRowsStances.addView(selectStanceElseNames);


        for (Stance stance : aquene.getAllStances().getStancesList()){
            RadioButton icon =new RadioButton(this);
            icon.setButtonDrawable(null);

            Drawable selectorImg = stance.getDrawableSelector();

            icon.setBackground(selectorImg);
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin=(int) getResources().getDimension(R.dimen.stance_icon_padding);
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
                selectStanceAtt.addView(icon);
                selectStanceAttNames.addView(name);
            } else if (stance.getType().equals(getResources().getString(R.string.stance_cat_2))) {
                selectStanceDef.addView(icon);
                selectStanceDefNames.addView(name);
            } else {
                selectStanceElse.addView(icon);
                selectStanceElseNames.addView(name);
            }
            mapRadioButtonStance.put(icon.getId(),stance.getId());
            mapStanceRadioButton.put(stance.getId(),icon.getId());
        }
        setParam(selectStanceAtt);
        setParam(selectStanceAttNames);
        setParam(selectStanceDef);
        setParam(selectStanceDefNames);
        setParam(selectStanceElse);
        setParam(selectStanceElseNames);
    }

    public void tooltip(final Stance stance, TextView name) {
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toatIt(stance.getSelected_image_path(),stance.getName(),stance.getDescr());
            }
        });
    }

    public void toatIt(String imgPath,String nameTxt,String descrTxt) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.toast_RelativeLayout));

        ImageView img =  view.findViewById(R.id.toast_image);
        int imgId = getResources().getIdentifier(imgPath, "drawable", getPackageName());
        img.setImageResource(imgId);
        TextView name = view.findViewById(R.id.toast_textName);
        name.setText(nameTxt);
        TextView descr = view.findViewById(R.id.toast_textDescr);
        descr.setText(descrTxt);

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_LONG);

        toast.setView(view);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();

    }

    private void setListnerMultiRadio(RadioGroup radioSub) {
        radioSub.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("-State-","Changement de bouton");
                unCheckAllRadio(group,this);
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    //do stuff with active stance
                    saveStance(checkedRadioButton);
                }
            }
        });

    }

    private void unCheckAllRadio(RadioGroup selectedGroup, RadioGroup.OnCheckedChangeListener onCheckedChangeListener) {
        for (RadioGroup group : listRadioGroups){
            if (!group.equals(selectedGroup)){
                group.setOnCheckedChangeListener(null);
                group.clearCheck();
                group.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if (display.getRotation()==Surface.ROTATION_0) {
            Intent intentMain = new Intent(StanceActivity.this, MainActivity.class);
            startActivity(intentMain);
        }
        if (display.getRotation()==Surface.ROTATION_180) {
            Intent intentMain = new Intent(StanceActivity.this, MainActivity.class);
            startActivity(intentMain);
        }
    }

    private void saveStance(RadioButton selectedButton) {
        Stance stanceActive=aquene.getAllStances().getStance(mapRadioButtonStance.get(selectedButton.getId()));
        aquene.activateStance(stanceActive.getId());
        String title = getString(R.string.stance_activity) +" (posture actuelle : "+stanceActive.getName()+")";
        SpannableString titleSpan = new SpannableString(title);
        titleSpan.setSpan(new ForegroundColorSpan(getColor(R.color.textColorPrimary)),0,title.length(),0);
        titleSpan.setSpan(new RelativeSizeSpan(1.5f)  ,0,getString(R.string.stance_activity).length()+1,0);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbarStance);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(titleSpan);
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
        radio.setLayoutParams(layoutParams);
    }

    private void setParam(LinearLayout lin) {
        lin.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lin.setLayoutParams(layoutParams);
    }

}