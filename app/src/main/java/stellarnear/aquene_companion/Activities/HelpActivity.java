package stellarnear.aquene_companion.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stellarnear.aquene_companion.Divers.AllGeneralHelpInfos;
import stellarnear.aquene_companion.Divers.GeneralHelpInfo;
import stellarnear.aquene_companion.Perso.Ability;
import stellarnear.aquene_companion.Perso.Attack;
import stellarnear.aquene_companion.Perso.Capacity;
import stellarnear.aquene_companion.Perso.Feat;
import stellarnear.aquene_companion.Perso.KiCapacity;
import stellarnear.aquene_companion.Perso.MythicCapacity;
import stellarnear.aquene_companion.Perso.MythicFeat;
import stellarnear.aquene_companion.Perso.Perso;
import stellarnear.aquene_companion.Perso.Skill;
import stellarnear.aquene_companion.Perso.Stance;
import stellarnear.aquene_companion.R;

/**
 * Created by jchatron on 26/12/2017.
 */

public class HelpActivity extends AppCompatActivity {
    private Perso aquene = MainActivity.aquene;
    private Context mC;
    private Map<Button,String> mapButtonCat=new HashMap<>();
    private ViewFlipper flipper;
    private View titleLayout;
    private View menuCategories;
    private TextView titleCatText;
    private ImageView infoImg;
    private GestureDetector mGestureDetector;
    private Activity mA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (settings.getBoolean("switch_fullscreen_mode",getApplicationContext().getResources().getBoolean(R.bool.switch_fullscreen_mode_DEF))) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        this.mA=this;
        this.mC=getApplicationContext();
        setContentView(R.layout.help_activity);
        flipper = findViewById(R.id.help_activity_flipper);
        infoImg = new ImageView(mC);   infoImg.setImageDrawable(getDrawable(R.drawable.ic_info_outline_24dp));
        infoImg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,1));
        flipper.addView(infoImg);
        titleCatText = (TextView) findViewById(R.id.help_activity_title_category_text);
        menuCategories = findViewById(R.id.help_activity_button_linear);
        titleLayout = findViewById(R.id.help_activity_title_category_relat);
        ImageButton backToCatMenu = findViewById(R.id.help_activity_button_category_exit);
        backToCatMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.removeAllViews();
                flipper.addView(infoImg);
                switchMenu(titleLayout,menuCategories);
            }
        });
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(this, customGestureDetector);
        String title=getString(R.string.help_activity);
        SpannableString titleSpan = new SpannableString(title);
        titleSpan.setSpan(new ForegroundColorSpan(getColor(R.color.textColorPrimary)),0,title.length(),0);
        titleSpan.setSpan(new RelativeSizeSpan(1.5f)  ,0,getString(R.string.help_activity).length(),0);
        Toolbar mActionBarToolbarhelp = (Toolbar) findViewById(R.id.toolbarHelp);
        setSupportActionBar(mActionBarToolbarhelp);
        getSupportActionBar().setTitle(titleSpan);

        buildCategories();
    }

    private void buildCategories() {
        List<String> categories = Arrays.asList("Général","Caractéristiques","Compétences","Capacités","Capacités de Ki","Postures","Attaques","Dons","Dons Mythiques","Capacités Mythiques");
        LinearLayout buttons = findViewById(R.id.help_activity_button_linear);
        buttons.removeAllViews();
        LinearLayout line1Buttons = new LinearLayout(mC);
        line1Buttons.setOrientation(LinearLayout.HORIZONTAL);
        line1Buttons.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        buttons.addView(line1Buttons);
        LinearLayout line2Buttons = new LinearLayout(mC);
        line2Buttons.setOrientation(LinearLayout.HORIZONTAL);
        line2Buttons.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        buttons.addView(line2Buttons);
        int nButton=1;
        for (String cat : categories){
            Button button = new Button(mC);
            button.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1));
            button.setText(cat);
            button.setTextSize(15);
            button.setAllCaps(false);
            button.setBackground(getDrawable(R.drawable.button_basic_gradient));
            setButtonListner(button);
            mapButtonCat.put(button,cat);
            if (nButton<= (categories.size()/2)){
                line1Buttons.addView(button);
            } else {
                line2Buttons.addView(button);
            }
           nButton+=1;
        }
    }

    private void setButtonListner(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setBackground(getDrawable(R.drawable.button_ok_gradient));
                unselectOthers(button);
                fillFlipper(button);
                titleCatText.setText(mapButtonCat.get(button));
                switchMenu(menuCategories,titleLayout);
            }
        });
    }

    private void switchMenu(final View out,final View in) {
        final Animation inFromTop = AnimationUtils.loadAnimation(mC,R.anim.infromtopaddatkpanel);
        final Animation outToTop = AnimationUtils.loadAnimation(mC,R.anim.outtotopaddatkpanel);
        outToTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                out.setVisibility(View.GONE);
                in.setVisibility(View.VISIBLE);
                in.startAnimation(inFromTop);
            }
        });
        out.startAnimation(outToTop);
    }

    private void unselectOthers(Button button) {
        for(Button buttonToUnselect : mapButtonCat.keySet()){
            if(!buttonToUnselect.equals(button)){
                buttonToUnselect.setBackground(getDrawable(R.drawable.button_basic_gradient));
            }
        }
    }

    private void fillFlipper(Button button) {
        flipper.removeAllViews();
        ViewGroup vg= findViewById(R.id.help_info_RelativeLayout);
        if(mapButtonCat.get(button).equalsIgnoreCase("Général")){
            List<GeneralHelpInfo> listHelp = new AllGeneralHelpInfos(mC).getListGeneralHelpInfos();
            for (GeneralHelpInfo help : listHelp){
                View view = getLayoutInflater().inflate(R.layout.custom_help_info_flipper,vg,false);
                changeFields(view,help.getId(),help.getName(),"",help.getDescr());
                flipper.addView(view);
            }
        }
        if(mapButtonCat.get(button).equalsIgnoreCase("Caractéristiques")){
            for (Ability abi : aquene.getAllAbilities().getAbilitiesList()){
                View view = getLayoutInflater().inflate(R.layout.custom_help_info_flipper,vg,false);
                int typeId = getResources().getIdentifier("ability_"+abi.getType(), "string", getPackageName());
                changeFields(view,abi.getId(),abi.getName(),"Type : "+getString(typeId),abi.getDescr());
                flipper.addView(view);
            }
        }
        if(mapButtonCat.get(button).equalsIgnoreCase("Compétences")){
            for (Skill skill : aquene.getAllSkills().getSkillsList()){
                View view = getLayoutInflater().inflate(R.layout.custom_help_info_flipper,vg,false);
                changeFields(view,skill.getId(),skill.getName(),"",skill.getDescr());
                flipper.addView(view);
            }
        }
        if(mapButtonCat.get(button).equalsIgnoreCase("Capacités")){
            List<Capacity> listCapas = aquene.getAllCapacities().getAllCapacitiesList();
            for (Capacity capacity : listCapas){
                View view = getLayoutInflater().inflate(R.layout.custom_help_info_flipper,vg,false);
                changeFields(view,capacity.getId(),capacity.getName(),"",capacity.getDescr());
                flipper.addView(view);
            }
        }
        if(mapButtonCat.get(button).equalsIgnoreCase("Capacités de Ki")){
            for (KiCapacity ki : aquene.getAllKiCapacities().getAllKiCapacitiesList()){
                View view = getLayoutInflater().inflate(R.layout.custom_help_info_flipper,vg,false);
                changeFields(view,ki.getId(),ki.getName(),"Coût : "+ki.getCost(),ki.getDescr());
                flipper.addView(view);
            }
        }
        if(mapButtonCat.get(button).equalsIgnoreCase("Postures")){
            for (Stance stance : aquene.getAllStances().getStancesList()){
                View view = getLayoutInflater().inflate(R.layout.custom_help_info_flipper,vg,false);
                changeFields(view,stance.getId()+"_select",stance.getName(),"Type : "+stance.getType(),stance.getDescr());
                flipper.addView(view);
            }
        }
        if(mapButtonCat.get(button).equalsIgnoreCase("Dons")){
            for (Feat feat : aquene.getAllFeats().getFeatsList()){
                View view = getLayoutInflater().inflate(R.layout.custom_help_info_flipper,vg,false);
                int typeId = getResources().getIdentifier(feat.getType(), "string", getPackageName());
                changeFields(view,feat.getId(),feat.getName(),"Type : "+getString(typeId),feat.getDescr());
                flipper.addView(view);
            }
        }
        if(mapButtonCat.get(button).equalsIgnoreCase("Attaques")){
            for (Attack atk : aquene.getAllAttacks().getAttacksList()){
                View view = getLayoutInflater().inflate(R.layout.custom_help_info_flipper,vg,false);
                changeFields(view,atk.getId(),atk.getName(),"",atk.getDescr());
                flipper.addView(view);
            }
        }
        if(mapButtonCat.get(button).equalsIgnoreCase("Dons Mythiques")){
            for (MythicFeat mythicFeat : aquene.getAllMythicFeats().getMythicFeatsList()){
                View view = getLayoutInflater().inflate(R.layout.custom_help_info_flipper,vg,false);
                changeFields(view,mythicFeat.getId(),mythicFeat.getName(),"",mythicFeat.getDescr());
                flipper.addView(view);
            }
        }
        if(mapButtonCat.get(button).equalsIgnoreCase("Capacités Mythiques")){
            for (MythicCapacity mythicCapacity : aquene.getAllMythicCapacities().getAllMythicCapacitiesList()){
                View view = getLayoutInflater().inflate(R.layout.custom_help_info_flipper,vg,false);
                changeFields(view,mythicCapacity.getId(),mythicCapacity.getName(),"Catégorie : "+mythicCapacity.getType(),mythicCapacity.getDescr());
                flipper.addView(view);
            }
        }
    }

    private void changeFields(View view,String id,String titleTxt,String typeTxt,String descrTxt) {
        ImageView img = view.findViewById(R.id.help_info_image);
        int imgId = getResources().getIdentifier(id, "drawable", getPackageName());
        try {
            img.setImageDrawable(mC.getDrawable(imgId));
        } catch (Exception e) {
            img.setVisibility(View.GONE);
            e.printStackTrace();
        }
        TextView title = view.findViewById(R.id.help_info_textName);
        title.setText(titleTxt);
        if(!typeTxt.equalsIgnoreCase("")){
            TextView type = view.findViewById(R.id.help_info_textType);
            type.setVisibility(View.VISIBLE);
            type.setText(typeTxt);
        }
        TextView descr = view.findViewById(R.id.help_info_textDescr);
        descr.setText(descrTxt);
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() > e2.getX()) { // Swipe left (next)
                flipNext();
            }
            if (e1.getX() < e2.getX()) {// Swipe right (previous)
                flipPrevious();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void flipNext() {
        flipper.clearAnimation();
        Animation in = AnimationUtils.loadAnimation(mC,R.anim.infromright);
        Animation out =  AnimationUtils.loadAnimation(mC,R.anim.outtoleft);
        flipper.setInAnimation(in);  flipper.setOutAnimation(out);
        flipper.showNext();
    }
    private void flipPrevious() {
        flipper.clearAnimation();
        Animation in = AnimationUtils.loadAnimation(mC,R.anim.infromleft);
        Animation out =  AnimationUtils.loadAnimation(mC,R.anim.outtoright);
        flipper.setInAnimation(in);  flipper.setOutAnimation(out);
        flipper.showPrevious();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
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
            Intent intent = new Intent(mA, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }
        if (display.getRotation()==Surface.ROTATION_180) {
            Intent intent = new Intent(mA, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
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
