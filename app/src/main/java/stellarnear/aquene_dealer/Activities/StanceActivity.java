package stellarnear.aquene_dealer.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stellarnear.aquene_dealer.Divers.Tools;
import stellarnear.aquene_dealer.Perso.Perso;
import stellarnear.aquene_dealer.Perso.Stance;
import stellarnear.aquene_dealer.R;

/* Created by jchatron on 26/12/2017.*/

public class StanceActivity extends AppCompatActivity {
    private Map<RadioButton,String> mapRadioButtonStance = new HashMap<>();
    private Map<String,RadioButton> mapStanceRadioButton = new HashMap<>();
    private Perso aquene = MainActivity.aquene;
    private RadioButton noStance;
    private List<RadioButton> allRadioButtons=new ArrayList<>();
    private Context mC;
    private Activity mA;
    private Tools tools=new Tools();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (settings.getBoolean("switch_fullscreen_mode",getApplicationContext().getResources().getBoolean(R.bool.switch_fullscreen_mode_DEF))) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stance_activity);
        this.mC=getApplicationContext();
        this.mA=this;
        noStance = findViewById(R.id.nostance_checkbox);
        noStance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    aquene.activateStance("");
                    unCheckAllRadio(null);
                    selectActiveStance();
                }
            }
        });
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
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        finish();
        super.onDestroy();
    }

    private void selectActiveStance() {
        Stance currentStance = aquene.getAllStances().getCurrentStance();
        String title=getString(R.string.stance_activity) +" (sélectionnez une posture)";
        if(currentStance!=null)
        {
            RadioButton radioSelected = mapStanceRadioButton.get(currentStance.getId());
            radioSelected.setChecked(true);
            title=getString(R.string.stance_activity) +" (posture actuelle : "+currentStance.getName()+")";
            unCheckAllRadio(radioSelected);
        } else {
            noStance.setChecked(true);
            unCheckAllRadio(null);
        }
        SpannableString titleSpan = new SpannableString(title);
        titleSpan.setSpan(new ForegroundColorSpan(getColor(R.color.textColorPrimary)),0,title.length(),0);
        titleSpan.setSpan(new RelativeSizeSpan(1.5f)  ,0,getString(R.string.stance_activity).length()+1,0);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbarStance);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(titleSpan);
    }

    private void createGridSelector(LinearLayout allRowsStances) {
        LinearLayout line1 = line(); LinearLayout line2 = line(); LinearLayout line3 = line();
        allRowsStances.addView(line1);allRowsStances.addView(line2);allRowsStances.addView(line3);

        LinearLayout selectStanceAtt = createRadioGroup();
        LinearLayout selectStanceAttNames = new LinearLayout(this);
        selectStanceAttNames.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1));
        selectStanceAttNames.setGravity(Gravity.CENTER);
        line1.addView(selectStanceAtt); line1.addView(selectStanceAttNames);

        LinearLayout selectStanceDef = createRadioGroup();
        LinearLayout selectStanceDefNames = new LinearLayout(this);
        selectStanceDefNames.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1));
        selectStanceDefNames.setGravity(Gravity.CENTER);
        line2.addView(selectStanceDef); line2.addView(selectStanceDefNames);

        LinearLayout selectStanceElse = createRadioGroup();
        LinearLayout selectStanceElseNames = new LinearLayout(this);
        selectStanceElseNames.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1));
        selectStanceElseNames.setGravity(Gravity.CENTER);
        line3.addView(selectStanceElse); line3.addView(selectStanceElseNames);

        for (Stance stance : aquene.getAllStances().getStancesList()){
            LinearLayout box=box();

            final RadioButton icon =new RadioButton(this);
            icon.setButtonDrawable(null);

            Drawable selectorImg = getDrawable( getResources().getIdentifier(stance.getId()+"_stance_selector", "drawable", getPackageName()));
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.width=(int) getResources().getDimension(R.dimen.stance_icon);
            params.height=(int) getResources().getDimension(R.dimen.stance_icon);
            icon.setLayoutParams(params);
            icon.setBackground(selectorImg);

            box.addView(icon);
            TextView name = new TextView(this);
            name.setText(stance.getShortName());
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1);
            name.setLayoutParams(params2);
            name.setSingleLine(true);
            name.setGravity(Gravity.CENTER);
            tooltip(stance,name);
            name.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_size_stance_icons));

            if (stance.getType().equals(getResources().getString(R.string.stance_cat_1))){
                selectStanceAtt.addView(box);
                selectStanceAttNames.addView(name);
            } else if (stance.getType().equals(getResources().getString(R.string.stance_cat_2))) {
                selectStanceDef.addView(box);
                selectStanceDefNames.addView(name);
            } else {
                selectStanceElse.addView(box);
                selectStanceElseNames.addView(name);
            }
            allRadioButtons.add(icon);
            mapRadioButtonStance.put(icon,stance.getId());
            mapStanceRadioButton.put(stance.getId(),icon);
        }
        setButtonListner();
    }

    private void setButtonListner() {
        for (final RadioButton button : allRadioButtons){
            if(!aquene.getAllStances().getStance(mapRadioButtonStance.get(button)).isPerma()) {
                button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (compoundButton.isChecked()) {
                            unCheckAllRadio(button);
                            saveStance(button);
                            noStance.setChecked(false);
                        }
                    }
                });
            }
        }
    }

    public void tooltip(final Stance stance, TextView name) {
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toatInfo(stance.getId()+"_select",stance.getName(),stance.getDescr());
            }
        });
    }

    public void toatInfo(String imgPath, String nameTxt, String descrTxt) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast_info,(ViewGroup) findViewById(R.id.toast_RelativeLayout));

        ImageView img =  view.findViewById(R.id.toast_image);
        int imgId = getResources().getIdentifier(imgPath, "drawable", getPackageName());
        img.setImageResource(imgId);
        TextView name = view.findViewById(R.id.toast_textName);
        name.setText(nameTxt);
        TextView descr = view.findViewById(R.id.toast_textDescr);
        descr.setText(descrTxt);
        tools.toastStanceTooltip(mC,view,"long");
    }

    private void unCheckAllRadio(RadioButton selectedButton) {
        for (RadioButton button : allRadioButtons){
            if (aquene.getAllStances().getStance(mapRadioButtonStance.get(button)).isPerma()){
                button.setChecked(true);
            } else if (!button.equals(selectedButton)){
                button.setChecked(false);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if (display.getRotation()==Surface.ROTATION_0) {
            Intent intent = new Intent(mA, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if (display.getRotation()==Surface.ROTATION_180) {
            Intent intent = new Intent(mA, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    private void saveStance(RadioButton selectedButton) {
        Stance stanceActive=aquene.getAllStances().getStance(mapRadioButtonStance.get(selectedButton));
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
            }, 2000);

        }
    }

    private LinearLayout createRadioGroup() {
        LinearLayout radioG = new LinearLayout(mC);
        //applyListnerMultiRadio(radioG);
        radioG.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        radioG.setLayoutParams(layoutParams);
        radioG.setGravity(Gravity.CENTER);
        radioG.setClipChildren(false);
        return radioG;
    }

    private LinearLayout line() {
        LinearLayout line = new LinearLayout(mC);
        line.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1);
        line.setLayoutParams(layoutParams);
        line.setGravity(Gravity.CENTER);
        line.setClipChildren(false);
        return line;
    }

    private LinearLayout box() {
        LinearLayout box = new LinearLayout(mC);
        box.removeAllViews();
        box.setGravity(Gravity.CENTER);
        box.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        return box;
    }
}